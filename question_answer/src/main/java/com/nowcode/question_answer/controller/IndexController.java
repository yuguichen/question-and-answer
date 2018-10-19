package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creat by YGC on 2018/10/15
 */
//@Controller
public class IndexController {

    //@RequestMapping(path = {"/","/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(){
        return "hello NowCoder";
    }

    @RequestMapping(path = {"/profile/{userID}"}) //userID为路径变量
    @ResponseBody
    public String profile(@PathVariable("userID") int userID,
                          @RequestParam("type") int type,
                          @RequestParam("key") String key){

        return String.format("profile Page of %d, t:%d key:%s",userID,type,key);
    }

    //@RequestMapping(path = {"/home"},method = {RequestMethod.GET})
    //@ResponseBody
    public String template(Model model){
        model.addAttribute("value1","this is home page!");

        List<String> colors = Arrays.asList(new String[]{"red","blue","green"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i=0;i<3;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

        User user = new User();
        user.setUsername("张三");
        model.addAttribute("user",user);
         return "home";
    }

}
