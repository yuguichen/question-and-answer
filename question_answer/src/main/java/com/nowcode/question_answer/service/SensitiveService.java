package com.nowcode.question_answer.service;

import com.nowcode.question_answer.controller.QuestionController;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
/**
 * 通过前缀树过滤敏感词
 */
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private TreeNode rootNode = new TreeNode();//根节点

    public static void main(String args[]){
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        sensitiveService.addWord("赌博");
        System.out.println(sensitiveService.filter(" 你好色 情"));
    }

    //过滤敏感词
    public String filter(String text){
        if(StringUtils.isBlank(text))
            return text;

        String replacement = "***";
        StringBuilder result = new StringBuilder();
        TreeNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while(position<text.length()){
            char c = text.charAt(position);
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if(tempNode == null){
                position = begin+1;
                begin = position;
                result.append(c);
                tempNode = rootNode;
            } else if(tempNode.isKeyWordEnd()){
                result.append(replacement);
                position++;
                begin = position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    @Override
    //敏感词初始化
    public void afterPropertiesSet() throws Exception {
        rootNode = new TreeNode();
        try{
            //读取敏感词文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt")));
            String linetext;
            while((linetext = reader.readLine())!=null){
                linetext = linetext.trim();//任何前导和尾随空格删除
                addWord(linetext);
            }
            reader.close();
        }catch (Exception e){
            logger.error("读取敏感词失败："+e.getMessage());
        }

    }

    //向树节点中添加敏感词
    private void addWord(String lineText){
        TreeNode tempNode = rootNode;

        for(int i=0;i<lineText.length();i++){
            Character c = lineText.charAt(i);
            // 过滤空格等符号字符
            if(isSymbol(c))
                continue;
            TreeNode node = tempNode.getSubNode(c);

            if(node==null){
                node = new TreeNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if(i==lineText.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    //判断字符是否是符号
    private boolean isSymbol(char c){
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic < 0x2E80 || ic > 0x9FFF);
    }

    //定义树节点类
    private class TreeNode{
        private Map<Character,TreeNode> subNode = new HashMap<>();  //子节点
        private boolean end = false;

        public TreeNode getSubNode(Character key){
            return subNode.get(key);
        }

        public void addSubNode(Character key,TreeNode node){
            subNode.put(key,node);
        }

        public boolean isKeyWordEnd(){
            return end;
        }

        public void setKeyWordEnd(boolean end){
            this.end = end;
        }
    }

}
