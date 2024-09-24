package com.example.yizhi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yizhi.common.R;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.service.UserService;
import com.example.yizhi.utils.ExcelUtils;
import com.example.yizhi.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 53595
 * @date 2024/03/31
 * @description 用户
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private HttpSession session;

    //登录
    @PostMapping("/login")
    public R login(User user) {
        //账号密码判空
        if (user.getAccount() == "" || user.getPassword() == "") {
            return R.error("用户名或密码不能为空");
        }

        UserDTO result = userService.login(user);

        //没有用户信息，登录失败
        if (result.getId() == null) {
            return R.error("用户名或密码错误");
        }

        //登录成功，将用户信息存入session
        session.setAttribute("currentUser", result);

        R r = R.ok("登录成功！");
        r.put("currentUser", result);
        return r;
    }

    //注册
    @PostMapping("/register")
    public R register(UserDTO userDTO) {
        //账号密码判空
        if (userDTO.getAccount() == "" || userDTO.getPassword() == "") {
            return R.error("用户名或密码不能为空");
        }

        //注册
        boolean register = userService.register(userDTO);

        //注册成功
        if (register) {
            return R.ok("注册成功");
        }

        //注册失败
        return R.error("注册失败,账号已存在");
    }

    //退出登录
    @PostMapping("/logout")
    public R logout(HttpSession session) {
        session.invalidate();
        return R.ok();
    }

    //分页查询(管理员功能)
    @GetMapping("/page")
    @ResponseBody
    public R page(@RequestParam Map<String, Object> params) {
        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser  = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("查询失败！非管理员不能查询用户");
        }

        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());


        int roleid = 0;
        String school = null;
        int groupid = 0;

        //身份、学校、组别
        if (params.get("roleid") != null) {
            roleid = Integer.parseInt(params.get("roleid").toString());
        }
        if (params.get("school") != null) {
            school = params.get("school").toString();
        }
        if (params.get("groupid") != null) {
            groupid = Integer.parseInt(params.get("groupid").toString());
        }

        //条件构造器
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(roleid!= 0 && !Objects.isNull(roleid),User::getRoleid, roleid);
        wrapper.like(StringUtils.isNotEmpty(school),User::getSchool, school);
        wrapper.eq(groupid!= 0 && !Objects.isNull(groupid),User::getGroupid, groupid);

        //分页构造器
        Page<User> pageInfo = new Page<>(page, pageSize);
        Page<UserDTO> userDtoPage = new Page<>();

        Page<User> pageUser = userService.page(pageInfo, wrapper);

        //获取到分页对象的真实信息
        userDtoPage = userService.getUserDtoPage(pageUser);

        R r = R.ok("分页查询成功！");
        r.put("pageUser", userDtoPage);

        return r;
    }

    //新增用户(管理员功能)
    @PostMapping("/add")
    public R add(User user) {
        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser  = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("新增失败！非管理员不能新增用户");
        }
        //判断账号是否存在
        User user1 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, user.getAccount()));
        if (user1 != null) {
            return R.error("新增失败！账号已存在");
        }

        user.setUpdateUser(currentUser.getId());
        user.setCreateUser(currentUser.getId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        boolean save = userService.save(user);
        if (save) {
            return R.ok("新增成功");
        }
        return R.error("新增失败");
    }

    //修改用户(管理员功能)
    @PostMapping("/update")
    public R update(User user) {
        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser  = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("更新失败！非管理员不能更新用户");
        }

        user.setUpdateUser(currentUser.getId());
        user.setUpdateTime(LocalDateTime.now());

        // 对密码进行加密
        String encryptedPassword = MD5Utils.md5(user.getPassword());
        user.setPassword(encryptedPassword);

        boolean update = userService.updateById(user);
        if (update) {
            return R.ok("修改成功");
        }
        return R.error("修改失败");
    }

    //删除用户(管理员功能)
    @DeleteMapping("/delete")
    public R delete(Integer id) {
        boolean remove = false;

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser =  (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("删除失败！非管理员不能删除用户");
        }

        if (remove) {
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

    //获取用户
    @GetMapping("/get")
    public R get(Integer id) {
        User user = userService.getById(id);
        if (user != null) {
            return R.ok().put("user", user);
        }
        return R.error("查询失败！");
    }

    //导出用户Excel(管理员功能)
    @GetMapping("/export")
    @ResponseBody
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("导出失败！非管理员不能导出用户");
        }

        int roleid = 0;
        String school = null;
        int groupid = 0;

        //身份、学校、组别
        if (params.get("roleid") != null) {
            roleid = Integer.parseInt(params.get("roleid").toString());
        }
        if (params.get("school") != null) {
            school = params.get("school").toString();
        }
        if (params.get("groupid") != null) {
            groupid = Integer.parseInt(params.get("groupid").toString());
        }

        //条件构造器
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(roleid!= 0 && !Objects.isNull(roleid),User::getRoleid, roleid);
        wrapper.like(StringUtils.isNotEmpty(school),User::getSchool, school);
        wrapper.eq(groupid!= 0 && !Objects.isNull(groupid),User::getGroupid, groupid);

        List<User> list = userService.list(wrapper);

        //转换成DTO
        List<UserDTO> userDtoList = userService.getUserDtoList(list);

        // 表头数据
        List<Object> head = Arrays.asList("姓名","账号","学校","角色","组别");

        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        for (UserDTO userDTO : userDtoList) {
            //用户数据
            List<Object> row = new ArrayList<>();
            row.add(userDTO.getName());
            row.add(userDTO.getAccount());
            row.add(userDTO.getSchool());
            row.add(userDTO.getRolename());
            row.add(userDTO.getGroupname());
            sheetDataList.add(row);
        }

        // 导出数据
        ExcelUtils.export(response,"用户表", sheetDataList);

        return R.ok();
    }

    //导入用户Excel(管理员功能)
    @PostMapping("/import")
    public R importUser(@RequestPart("file") MultipartFile file) throws Exception {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("导入失败！非管理员不能导入用户");
        }

        List<UserDTO> users = ExcelUtils.readMultipartFile(file, UserDTO.class);

        //获取DTO中的角色和组别信息，并查询数据库，获取id,并保存到数据库
        boolean b = userService.SaveImportUser(users);

        if (!b){
            return R.error("导入失败！");
        }
        R r = R.ok("导入成功！");

        return r;
    }
}