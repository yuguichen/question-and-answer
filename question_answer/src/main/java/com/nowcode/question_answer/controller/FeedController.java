package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.model.Feed;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.service.FeedService;
import com.nowcode.question_answer.service.FollowService;
import com.nowcode.question_answer.service.JedisAdapt;
import com.nowcode.question_answer.utils.JedisKeyUtil;
import com.nowcode.question_answer.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapt jedisAdapt;

    /**
     * 推模式
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pushfeeds"},method = {RequestMethod.GET,RequestMethod.POST})
    public String getPushFeeds(Model model){
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapt.lrange(JedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    /**
     * 拉模式
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pullfeeds"},method = {RequestMethod.GET,RequestMethod.POST})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.getUser()==null?0:hostHolder.getUser().getId();
        List<Integer> followees = new ArrayList<>();
        if(localUserId!=0)
            followees = followService.getFollowees(localUserId,MyUtils.ENTITY_USER,Integer.MAX_VALUE);
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

}
