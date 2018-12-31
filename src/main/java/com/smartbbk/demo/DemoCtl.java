package com.smartbbk.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class DemoCtl {
    public DemoCtl(){
        System.out.println("init Hello DemoCtl");
    }
    @RequestMapping("/hello")
    public String index(){
        SimpleDateFormat sdf=    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("hello world!");
        return "Hello World! "+sdf.format(new Date());
    }
}
