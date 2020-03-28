package com.imooc.miaosha_1.vo;

import com.imooc.miaosha_1.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {

    private GoodsVo goodsVo;

    private OrderInfo orderInfo;

}
