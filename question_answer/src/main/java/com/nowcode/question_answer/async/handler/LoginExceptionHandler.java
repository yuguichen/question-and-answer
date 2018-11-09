package com.nowcode.question_answer.async.handler;

import com.nowcode.question_answer.async.EventHandler;
import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/30.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        // xxxx判断发现这个用户登陆异常
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"), "登陆IP异常", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
