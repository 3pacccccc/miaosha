package com.imooc.miaosha_1.controller;


import com.imooc.miaosha_1.domain.User;
import com.imooc.miaosha_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "xiaoma");
        return "hello";
    }

    @RequestMapping("/db")
    @ResponseBody
    public String db(){
        User user = userService.getById(1);
        return user.getName();
    }

}
