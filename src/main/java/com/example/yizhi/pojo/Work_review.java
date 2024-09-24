package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 53595
 * @date 2024/03/3
 * @description 作品评论实体类
 */
@Data
public class Work_review {
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
    private Long createUser;

    //评论时间
    private LocalDateTime createDatetime;
}

