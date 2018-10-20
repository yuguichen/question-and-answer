package com.nowcode.question_answer.model;


import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //ThreadLocal 线程本地变量
    //每个线程都可以拷贝一个user对象，指向内存不同，可通过一个接口访问
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
