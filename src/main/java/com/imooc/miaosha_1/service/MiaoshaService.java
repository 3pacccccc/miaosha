package com.imooc.miaosha_1.service;


import com.imooc.miaosha_1.domain.MiaoshaOrder;
import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.domain.OrderInfo;
import com.imooc.miaosha_1.redis.MiaoshaKey;
import com.imooc.miaosha_1.redis.RedisService;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success){
            return orderService.createOrder(user, goods);
        }else{
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getGoodsId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }

    public void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId){
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

    public void reset(List<GoodsVo> goodsVoList){
        goodsService.resetStock(goodsVoList);
        orderService.deleteOrders();
    }
}
