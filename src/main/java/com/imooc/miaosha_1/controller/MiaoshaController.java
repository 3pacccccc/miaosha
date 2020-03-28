package com.imooc.miaosha_1.controller;


import com.imooc.miaosha_1.domain.MiaoshaOrder;
import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.rabbitmq.MQSender;
import com.imooc.miaosha_1.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha_1.redis.GoodsKey;
import com.imooc.miaosha_1.redis.RedisService;
import com.imooc.miaosha_1.result.CodeMsg;
import com.imooc.miaosha_1.result.Result;
import com.imooc.miaosha_1.service.GoodsService;
import com.imooc.miaosha_1.service.MiaoshaService;
import com.imooc.miaosha_1.service.OrderService;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 解决超卖的方案：1.在sql语句上面加上and stock_count > 0 防止出现库存为负数的问题。
     * 2.在数据库用user_id跟goods_id建立联合唯一索引。解决用户重复秒杀的问题。Unique
     *
     * @param model   前端model
     * @param user    当前登录对象
     * @param goodsId 商品ID
     * @return result中的OrderInfo对象
     */
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    public Result<Integer> list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);

        // 内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setMiaoshaUser(user);
        mqSender.sendMiaoshaMessage(miaoshaMessage);
        return Result.success(0);

//        // 判断库存
//        GoodsVo goods = goodsService.getGoodsByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if (stock <= 0) {
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//        // 判断是否已经秒杀到了
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (order != null) {
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//
//        // 减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        return Result.success(orderInfo);
    }

    /**
     * 在系统初始化之后会加载此函数
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getGoodsStock());
            localOverMap.put(goodsVo.getId(), false);
        }
    }


    @RequestMapping(value = "/reseult", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute(user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

}
