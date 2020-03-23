package com.imooc.miaosha_1.controller;


import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.service.GoodsService;
import com.imooc.miaosha_1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("to_list")
    public String list(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.listGoodsVo());
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.getGoodsByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) { // 秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) { // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}