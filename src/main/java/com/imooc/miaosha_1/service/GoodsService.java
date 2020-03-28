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

    public boolean reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        return goodDao.reduceStock(g) > 0;
    }

    public void resetStock(List<GoodsVo> goodsVoList) {
        for (GoodsVo goodsVo : goodsVoList) {
            MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
            miaoshaGoods.setGoodsId(goodsVo.getId());
            miaoshaGoods.setStockCount(goodsVo.getStockCount());
            goodDao.resetStock(miaoshaGoods);
        }
    }
}
