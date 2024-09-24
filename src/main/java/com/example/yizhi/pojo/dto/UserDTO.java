package com.example.yizhi.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.yizhi.pojo.User;
import com.example.yizhi.utils.ExcelImport;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
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

    //~~~~~~~~以下是新增的属性~~~~~~~~~
    @ExcelImport("角色")
    private String rolename;
    private  String roledetail;
    @ExcelImport("组别")
    private String groupname;

}
