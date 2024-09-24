package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("work_group")
public class Group {
    @TableId(value = "id", type = IdType.AUTO)
    private  Long id;
    private  String groupName;
}
