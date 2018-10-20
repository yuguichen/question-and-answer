package com.nowcode.question_answer.configuration;

import com.nowcode.question_answer.interceptor.PassPortInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 */

@Component
public class QuestionAndAnswerWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassPortInterceptor passPortInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passPortInterceptor);
        super.addInterceptors(registry);
    }
}
