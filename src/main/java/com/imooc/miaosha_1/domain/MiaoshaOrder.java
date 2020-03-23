package com.imooc.miaosha_1.domain;

import lombok.Data;

@Data
public class MiaoshaOrder {

    private Long id;

    private Long userId;

    private Long  orderId;

    private Long goodsId;
}
