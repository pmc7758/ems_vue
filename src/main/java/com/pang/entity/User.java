package com.pang.entity;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true) //chain设置为true则set方法返回当前对象
public class User {

    private String id;
    private String username;
    private String realname;
    private String password;
    private String sex;
    private String status;
    private Date registerTime;

}