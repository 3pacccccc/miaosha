package com.imooc.miaosha_1.service;

import com.imooc.miaosha_1.dao.UserDao;
import com.imooc.miaosha_1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

}
