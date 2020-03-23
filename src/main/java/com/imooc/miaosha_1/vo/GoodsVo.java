package com.imooc.miaosha_1.vo;

import com.imooc.miaosha_1.domain.Goods;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsVo extends Goods {

    private Double miaoshaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;
}
