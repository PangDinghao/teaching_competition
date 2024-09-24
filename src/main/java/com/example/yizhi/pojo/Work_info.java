package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

/**
 * @author 53595
 * @date 2024/03/30
 * @description 参赛作品信息
 */
@Data
public class Work_info {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String workName;

    private String school;

    private  String teamUsername;
    //参赛组别
    private Long groupid;
    //联系方式
    private String contactInfo;
    //平均成绩
    private double aveScore;
    //参赛报名表
    private String entryForm;
    //信息公示件
    private String infoForm;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Long createUser;
    private Long updateUser;


}
