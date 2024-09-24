package com.example.yizhi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 53595
 * @date 2024/03/31
 * @description 参赛作品文件实体类
 */
@Data
public class Work_file {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //作品id
    private Long workId;
    //文件类型id
    private Long workTypeid;

    //文件名称
    private String fileName;
}
