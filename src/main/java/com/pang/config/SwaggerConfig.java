package com.pang.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration   //加载进springboot中的配置文件中
@EnableSwagger2  //开启Swagger2
public class SwaggerConfig {

    //配置swagger的Docket的bean实例
    @Bean
    public Docket docket(Environment environment) {

/*      //设置要显示的Swagger环境
        Profile profile = Profile.of("dev","test");
        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean flag = environment.acceptsProfiles(profiles);*/

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.enable(flag) //判断是否启动swagger，如果为False，则Swagger不能再浏览器中访问
                .groupName("庞坚")
                .select()
                //RequestHandlerSelectors,配置要扫描接口的方式
                //basePackage:指定要扫描的包
                //any()：扫描全部
                //none()：不扫描
                //withClassAnnotation:扫描类上的注解，参数是一个注解的反射对象
                //withMethodAnnotation:扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.pang.controller"))
                //paths()过滤什么路径
                //.paths(PathSelectors.ant("/pang/**"))
                .build(); //.select()和.apis()和.build()是连用的
    }

    //配置swagger信息=apiInfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("庞坚", "https://blog.kuangstudy.com/", "614039502@qq.com");
        return new ApiInfo(
                "庞坚的接口文档",
                "远航",
                "v1.0",
                "https://blog.kuangstudy.com/",
                contact,
                "Apache 2.0",
                "https://",
                new ArrayList()
        );
    }
}


//实体类上加@ApiModel("提示信息")是给文档的实体类一些注释，让前端看懂
//也可以在属性上加@ApiModelProperty("提示信息")，给文档的实体属性一些注释，也是为了让前端看懂
//注释在文档接口要返回的实体类对象上
//@ApiOperation可以在方法上加注释
//@ApiParam可以在方法参数上加注释