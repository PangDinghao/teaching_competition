package com.example.yizhi.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkDTO {
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

    //~~~~~~以上为参赛信息~~~~~~//
    //~~~~~~以下为参赛作品信息~~~~~~//

    //课堂实录
    private List<String> videaFileNames = new ArrayList<>();
    //教案
    private String teachplanFileName;
    //教学时事报告
    private String newsFileName;
    //专业人才培养方案
    private String cetFileName;
    //可成标准
    private String standardFileName;
    //教材选用说明
    private String textbookFileName;
    //文件所在路径
    private String filePath;


    //~~~~~~以上为参赛作品信息~~~~~~//
    //~~~~~~以下为id查询信息~~~~~~//

    //组别
    private String groupName;

}
