package com.pang.controller;

import com.pang.entity.Emp;
import com.pang.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController//让框架去扫描，去实现controller接口
@RequestMapping("emp")
@CrossOrigin  //允许跨域
@Slf4j
public class EmpController {

    @Autowired
    private EmpService empService;

    @Value("${photo.dir}")  //@Value是取配置文件的值
    private String realpath;

    //修改员工信息
    @PostMapping("update") //可以通过方法形参去接收前端的表单数据
    public Map<String, Object> update(Emp emp, MultipartFile photo) throws IOException {
        log.info("员工信息：[{}]", emp.toString());  //可以打印前台的响应信息（emp对象的id等属性）
        if (photo != null && photo.getSize() != 0) {
            log.info("头像信息：[{}]", photo.getOriginalFilename());
            //保存头像
            String newFileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            //头像上传
            photo.transferTo(new File(realpath, newFileName));
            //设置头像地址
            emp.setPath(newFileName);
        }
        Map<String, Object> map = new HashMap<>();
        try {
            //保存员工信息
            empService.update(emp);
            map.put("state", true);
            map.put("msg", "员工保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "员工保存失败");
        }
        return map;

    }


    //根据id查询员工的信息实现
    @GetMapping("findOne")
    public Emp findOne(String id) {
        log.info("查询员工的信息的id：[{}]", id);
        return empService.findOne(id);
    }

    //保存员工信息
    @PostMapping("save")
    public Map<String, Object> save(Emp emp, MultipartFile photo) throws IOException {
        log.info("员工信息：[{}]", emp.toString());  //可以打印前台的响应信息（emp对象的id等属性）
        log.info("头像信息：[{}]", photo.getOriginalFilename());
        Map<String, Object> map = new HashMap<>();
        try {
            //保存头像
            String newFileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            //头像上传
            photo.transferTo(new File(realpath, newFileName));


            //设置头像地址
            emp.setPath(newFileName);
            //保存员工信息
            empService.save(emp);
            map.put("state", true);
            map.put("msg", "员工保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "员工保存失败");
        }
        return map;

    }


    //获取员工列表的方法
    @GetMapping("findAll")
    public List<Emp> findAll() {
        return empService.findAll();
    }

    //删除员工信息
    @GetMapping("delete")
    public Map<String, Object> delete(String id) {
        log.info("删除员工的id：[{}]", id);
        Map<String, Object> map = new HashMap<>();

        try {
            //删除员工头像
            Emp emp = empService.findOne(id);
            File file = new File(realpath, emp.getPath());
            if (file.exists()) file.delete(); //博客收藏了一篇File类的介绍
            //删除员工信息
            empService.delete(id);
            map.put("state", "true");
            map.put("msg", "删除员工信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "删除员工信息失败");
        }
        return map;
    }

}
