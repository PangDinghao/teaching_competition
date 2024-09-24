package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.yizhi.utils.ExcelImport;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author pdh
 * @since 2024-03-30
 */
@Data
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ExcelImport("姓名")
    private String name;
    @ExcelImport("账号")
    private String account;
    @ExcelImport("学校")
    private String school;

    private String password;

    private LocalDateTime createTime;

    private Long createUser;

    private LocalDateTime updateTime;

    private Long updateUser;

    private Long roleid;

    private  Long groupid;


}
