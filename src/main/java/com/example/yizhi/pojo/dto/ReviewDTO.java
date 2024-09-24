package com.example.yizhi.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //作品主键
    private String workid;
    //评论内容
    private String reviewDetile;

    //第一部分评分
    private double score1;

    //第二部分评分
    private double score2;

    //第三部分评分
    private double score3;

    //第四部分评分
    private double score4;

    //第五部分评分
    private double score5;

    //第六部分评分
    private double score6;

    //总分
    private double totalScore;

    //评论人id
    private String createUser;

    //评论时间
    private LocalDateTime createDatetime;

    //~~~~~~~~~以上为数据库字段~~~~~~~~~
    //以下为非数据库字段，用于传输数据

    //组别
    private String groupName;
    private String workName;
    private String school;

    //参赛者姓名
    private String teamUsername;

    //评委姓名
    private String createrUsername;
}
