package com.nowcode.question_answer.async.handler;

import com.nowcode.question_answer.async.EventHandler;
import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.Message;
import com.nowcode.question_answer.service.MessageService;
import com.nowcode.question_answer.service.UserService;
import com.nowcode.question_answer.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(MyUtils.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setContent("用户"+userService.getUser(model.getActorId()).getUsername()+
                "赞了你的评论，http://127.0.0.1:8080/question/"+model.getExts("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
