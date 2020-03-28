package com.imooc.miaosha_1.rabbitmq;


import com.imooc.miaosha_1.domain.MiaoshaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {

    private MiaoshaUser miaoshaUser;

    private long goodsId;

}
