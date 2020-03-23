package com.imooc.miaosha_1.service;


import com.imooc.miaosha_1.dao.GoodDao;
import com.imooc.miaosha_1.domain.MiaoshaGoods;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodDao goodDao;

    public List<GoodsVo> listGoodsVo() {
        return goodDao.listGoodsVo();
    }

    public GoodsVo getGoodsByGoodsId(long goodsId) {
        return goodDao.getGoodVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        goodDao.reduceStock(g);
    }
}
