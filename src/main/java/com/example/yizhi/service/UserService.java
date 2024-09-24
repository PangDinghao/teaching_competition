package com.example.yizhi.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.dto.UserDTO;

import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {

    UserDTO login(User user);

    boolean register(UserDTO userDTO);

    Page<UserDTO> getUserDtoPage(Page<User> pageUser);

    List<UserDTO> getUserDtoList(List<User> userList);

    boolean SaveImportUser(List<UserDTO> users);
}
