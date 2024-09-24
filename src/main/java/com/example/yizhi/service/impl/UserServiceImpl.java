package com.example.yizhi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yizhi.mapper.GroupMapper;
import com.example.yizhi.mapper.RoleMapper;
import com.example.yizhi.mapper.UserMapper;
import com.example.yizhi.pojo.Group;
import com.example.yizhi.pojo.Role;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yizhi.utils.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    HttpSession session;

    /**
     * @param user
     * @return {@link UserDTO}
     * @Description: 登录
     */
    @Override
    public UserDTO login(User user) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(User::getAccount,user.getAccount());

        // 对密码进行加密后判断
        String encryptedPassword = MD5Utils.md5(user.getPassword());
        lambdaQueryWrapper.eq(User::getPassword, encryptedPassword);

        User resultUser = userMapper.selectOne(lambdaQueryWrapper);
        UserDTO result = new UserDTO();

        if(resultUser != null){

            BeanUtils.copyProperties(resultUser, result);
            LambdaQueryWrapper<Role> lambdaQueryWrapperRole = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Group> lambdaQueryWrapperGroup = new LambdaQueryWrapper<>();

            lambdaQueryWrapperRole.eq(resultUser.getRoleid()!=0,Role::getId,resultUser.getRoleid());
            lambdaQueryWrapperGroup.eq(resultUser.getGroupid()!=0,Group::getId,resultUser.getGroupid());

            Group resultGroup = new Group();
            Role resultRole = new Role();

            if (resultUser.getGroupid()!=0 ){
                resultGroup = groupMapper.selectOne(lambdaQueryWrapperGroup);
            }


            if (resultUser.getRoleid()!=0 ){
                resultRole = roleMapper.selectOne(lambdaQueryWrapperRole);
            }

            result.setRolename(resultRole.getRolename());
            result.setRoledetail(resultRole.getRoledetail());
            result.setGroupname(resultGroup.getGroupName());
        }

        return result;

    }

    @Override
    public boolean register(UserDTO userDTO) {

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(User::getAccount,user.getAccount());

        //判断账号是否存在
        User resultUser = userMapper.selectOne(lambdaQueryWrapper);
        if(resultUser != null){
            return false;
        }

        // 对密码进行加密
        String encryptedPassword = MD5Utils.md5(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        return true;
    }
    //获取到分页对象的真实数据
    @Override
    public Page<UserDTO> getUserDtoPage(Page<User> pageUser) {
        Page<UserDTO> pageUserDTO = new Page<>();
        BeanUtils.copyProperties(pageUser, pageUserDTO);

        List<UserDTO> userDTOS = pageUser.getRecords().stream().map(user -> {
            Long groupid = user.getGroupid();
            Long roleid = user.getRoleid();
            Group group = new Group();
            Role role = new Role();

            if(roleid!= 0 && !Objects.isNull(roleid)){
                role = roleMapper.selectById(roleid);
            }
            if(groupid!= 0 && !Objects.isNull(groupid)){
                group = groupMapper.selectById(groupid);
            }

            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);

            userDTO.setRolename(role.getRolename());
            userDTO.setRoledetail(role.getRoledetail());
            userDTO.setGroupname(group.getGroupName());

            return userDTO;
        }).collect(Collectors.toList());

        pageUserDTO.setRecords(userDTOS);

        return pageUserDTO;
    }

    @Override
    public List<UserDTO> getUserDtoList(List<User> userList) {

        List<UserDTO> userDTOS = userList.stream().map(user -> {
            Long groupid = user.getGroupid();
            Long roleid = user.getRoleid();
            Group group = new Group();
            Role role = new Role();

            if(roleid!= 0 && !Objects.isNull(roleid)){
                role = roleMapper.selectById(roleid);
            }
            if(groupid!= 0 && !Objects.isNull(groupid)){
                group = groupMapper.selectById(groupid);
            }

            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);

            userDTO.setRolename(role.getRolename());
            userDTO.setRoledetail(role.getRoledetail());
            userDTO.setGroupname(group.getGroupName());

            return userDTO;
        }).collect(Collectors.toList());
        return userDTOS;
    }

    //获取角色和组的dto列表，并保存
    @Override
    public boolean SaveImportUser(List<UserDTO> users) {
        User insertUser = new User();

        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");

        users.stream().forEach(user -> {
            if(!Objects.isNull(user.getRolename())){
                LambdaQueryWrapper<Role> wrapperRole = new LambdaQueryWrapper<>();
                wrapperRole.eq(Role::getRolename,user.getRolename());
                Role role = roleMapper.selectOne(wrapperRole);

                user.setRoleid(role.getId());
                user.setRoledetail(role.getRoledetail());
            }
            if(!Objects.isNull(user.getGroupname())){
                LambdaQueryWrapper<Group> wrapperGroup = new LambdaQueryWrapper<>();

                wrapperGroup.eq(Group::getGroupName,user.getGroupname());
                Group group = groupMapper.selectOne(wrapperGroup);
                user.setGroupid(group.getId());
            }
            BeanUtils.copyProperties(user, insertUser);


            insertUser.setUpdateUser(currentUser.getId());
            insertUser.setCreateUser(currentUser.getId());
            insertUser.setCreateTime(LocalDateTime.now());
            insertUser.setUpdateTime(LocalDateTime.now());

            userMapper.insert(insertUser);
        });

        return true;
    }


}
