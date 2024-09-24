package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String rolename;
    private String roledetail;
}
