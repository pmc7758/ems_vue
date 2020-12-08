package com.pang.controller;


import com.pang.entity.User;
import com.pang.service.UserService;
import com.pang.utils.VerifyCodeUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController //相等于@respondbody和@controller两个注解的作用，@respondbody表明方法结果不需要走视图解析，会直接走一个字符串
@CrossOrigin //允许跨域
@RequestMapping("user") //这是一个
@Slf4j
public class UserController { //和业务层打交道，利用前端输入的信息作为参数调用业务层方法，由业务层调用DAO层去对数据库进行操作

    @Autowired
    private UserService userService; //自动注入


    /*
    用来处理用户登录
     */
    @PostMapping("login")  //注意是POST
    public Map<String, Object> login(@RequestBody User user) {
        log.info("当前登录用户的信息：[{}]", user.toString());
        Map<String, Object> map = new HashMap<>(); //创建一个HashMap对象
        try {
            User userDB = userService.login(user); //userService的login返回的是一个User型对象，要用一个变量接收
            map.put("state", "true");
            map.put("msg", "登录成功！");
            map.put("user", userDB);  //登录中要有一个状态返回给前端，让前端知道这个用户登录的成功状态,应该用session去认证已登录，这里简化了
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "false");
            map.put("msg", e.getMessage());
        }
        return map;
    }


    /*
    用来处理用户注册方法
     */
    @PostMapping("register")
    public Map<String, Object> register(@RequestBody User user, String code, HttpServletRequest request) { //返回值类型时Map
        log.info("用户信息：[{}]", user.toString());
        log.info("用户输入的验证码信息：[{}]", code);
        Map<String, Object> map = new HashMap<>();
        //1.调用业务方法
        try {
            String key = (String) request.getServletContext().getAttribute("code");//拿数据
            if (key.equalsIgnoreCase(code)) {  //判断验证码是否正确
                userService.register(user);
                map.put("state", true);
                map.put("msg", "注册成功！");
            } else {
                throw new RuntimeException("验证码出现错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "提示:" + e.getMessage());
        }
        return map;//前端返回一个user对象，有相关信息，Controller调用业务层相关的方法处理数据，返回一个map对象，里面有处理数据后的提示信息
    }


    /*
    生成验证码图片
     */
    @GetMapping("getImage") //也可以通过Request对象的getParameter（“指定名”）去获取，指定名要与登陆页面的参数名称一样
    public String getImageCode(HttpServletRequest request) throws IOException {
        //使用工具类生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4); //长度为4的验证码v
        //将验证码放入servletContext作用域，getServletContext()用来生成ServletContext对象，setAttribute用来在serletContext域存储数据
        request.getServletContext().setAttribute("code", code);
        //将图片转为base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 30, byteArrayOutputStream, code);
        return "data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
    }
}
