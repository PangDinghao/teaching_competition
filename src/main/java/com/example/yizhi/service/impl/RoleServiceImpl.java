package com.example.yizhi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yizhi.mapper.RoleMapper;
import com.example.yizhi.pojo.Role;
import com.example.yizhi.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
