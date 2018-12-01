package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.FeedDAO;
import com.nowcode.question_answer.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId()>0;
    }

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds,int count){
        return feedDAO.selectUserFeeds(maxId,userIds,count);
    }

    public Feed getFeedById(int id){
        return feedDAO.getFeedById(id);
    }

}
