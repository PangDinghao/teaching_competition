package com.example.yizhi.controller;

import com.example.yizhi.pojo.Group;
import com.example.yizhi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 53595
 * @date 2024/03/31
 * @description 组别
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/list")
    public List<Group> list() {
        return groupService.list();
    }

}
