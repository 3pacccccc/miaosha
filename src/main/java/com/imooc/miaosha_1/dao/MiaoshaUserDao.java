package com.imooc.miaosha_1.dao;


import com.imooc.miaosha_1.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);

    @Select("update miaosha_user set password=#{password} where id=#{id}")
    public void update(MiaoshaUser toBeUpdate);
}
