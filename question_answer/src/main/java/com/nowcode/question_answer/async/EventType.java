package com.nowcode.question_answer.async;

/**
 * 事件类型
 */
public enum EventType {
    LIKE(0),   //点赞
    COMMENT(1),//评论
    LOGIN(2),  //登录
    MAIL(3);   //邮件

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
