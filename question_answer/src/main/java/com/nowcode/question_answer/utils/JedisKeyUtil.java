package com.nowcode.question_answer.utils;

/**
 * 生成 redis 中的 key
 */
public class JedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    private static String BIZ_FOLLOWER = "FOLLOWER";//粉丝
    private static String BIZ_FOLLOWEE = "FOLLOWEE";//关注对象

    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }

    public static String getFolloweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId)+ SPLIT + String.valueOf(entityType);
    }

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
}
