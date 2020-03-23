package com.imooc.miaosha_1.controller;


import com.imooc.miaosha_1.domain.MiaoshaOrder;
import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.domain.OrderInfo;
import com.imooc.miaosha_1.result.CodeMsg;
import com.imooc.miaosha_1.service.GoodsService;
import com.imooc.miaosha_1.service.MiaoshaService;
import com.imooc.miaosha_1.service.OrderService;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        // 判断库存
        GoodsVo goods = goodsService.getGoodsByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
