package com.pang.service;

import com.pang.mapper.UserDAO;
import com.pang.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User login(User user) {
        //根据用户输入的用户名进行查询
        User userDB = userDAO.findByUserName(user.getUsername());//数据库查询返回一个对象
        //看用户是否存在
        if (!ObjectUtils.isEmpty(userDB)) {
            //用户存在的情况下比较密码
            if (userDB.getPassword().equals(user.getPassword())) {
                return userDB;
            } else {
                throw new RuntimeException("密码错误");
            }
        } else {
            throw new RuntimeException("用户不存在");
        }
    }


    @Override
    public void register(User user) {
        /*由前端的request传回来一个用户信息，利用实体类getUsername（）方法去获取属性，
          属性作为参数，再调用DAO层的findByUserName方法去访问数据库，看看有这个对象吗
        */
        //根据用户输入的用户名判断用户是否存在
        User userDB = userDAO.findByUserName(user.getUsername());
        if (userDB == null) {
            //1.生成用户状态
            user.setStatus("已激活");
            //2.设置用户注册时间
            user.setRegisterTime(new Date());
            //调用DAO
            userDAO.save(user);
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }
}
