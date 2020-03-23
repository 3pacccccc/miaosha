package com.imooc.miaosha_1.dao;


import com.imooc.miaosha_1.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") int id);
}
