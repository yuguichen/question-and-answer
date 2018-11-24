package com.nowcode.question_answer.async.handler;

import com.nowcode.question_answer.async.EventHandler;
import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.Message;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.service.MessageService;
import com.nowcode.question_answer.service.UserService;
import com.nowcode.question_answer.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class UnFollowHandler implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(MyUtils.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUser(model.getActorId());

        if(model.getEntityType() == MyUtils.ENTITY_QUESTION){
            message.setContent("用户"+user.getUsername()+"取消关注了你的问题,http://127.0.0.1:8080/question/"+model.getEntityId());
        } else if(model.getEntityType() == MyUtils.ENTITY_USER){
            message.setContent("用户"+user.getUsername()+"取消关注了你,http://127.0.0.1:8080/user/"+model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}
