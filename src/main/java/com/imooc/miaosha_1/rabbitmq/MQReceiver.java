package com.imooc.miaosha_1.rabbitmq;


import com.imooc.miaosha_1.domain.MiaoshaOrder;
import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.redis.RedisService;
import com.imooc.miaosha_1.service.GoodsService;
import com.imooc.miaosha_1.service.MiaoshaService;
import com.imooc.miaosha_1.service.OrderService;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message: " + message);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser miaoshaUser = miaoshaMessage.getMiaoshaUser();
        long goodsId = miaoshaMessage.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsByGoodsId(goodsId);
        int stock = goodsVo.getGoodsStock();
        if (stock <= 0) {
            return;
        }

        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            return;
        }
        // 减库存，下订单，写入秒杀订单
        miaoshaService.miaosha(miaoshaUser, goodsVo);

    }
}
