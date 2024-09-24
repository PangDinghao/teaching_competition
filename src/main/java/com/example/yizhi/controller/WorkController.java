package com.example.yizhi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yizhi.common.R;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.Work_info;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.pojo.dto.WorkDTO;
import com.example.yizhi.service.WorkService;
import com.example.yizhi.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work")
public class WorkController {
    @Autowired
    private WorkService workService;

    @Autowired
    private HttpSession session;

    //分页查询作品列表(管理员、评委功能)
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1 && currentUser.getRoleid() != 2){
            return R.error("查询失败！非管理员和评委不能查询比赛信息");
        }

        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        //作品、学校、参赛人、组别
        String workName = null;
        String school = null;
        String teamUsername = null;
        String groupid = null;


        if (params.get("workName") != null) {
            workName = params.get("workName").toString();
        }
        if (params.get("school") != null) {
            school = params.get("school").toString();
        }
        if (params.get("teamUsername") != null) {
            teamUsername = params.get("teamUsername").toString();
        }
        if (params.get("groupid") != null) {
            groupid = params.get("groupid").toString();
        }

        //条件构造器
        LambdaQueryWrapper<Work_info> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotEmpty(workName),Work_info::getWorkName, workName);
        wrapper.like(StringUtils.isNotEmpty(school),Work_info::getSchool, school);
        wrapper.like(StringUtils.isNotEmpty(teamUsername),Work_info::getTeamUsername, teamUsername);
        wrapper.eq(StringUtils.isNotEmpty(groupid),Work_info::getGroupid, groupid);

        //分页构造器
        Page<Work_info> pageInfo = new Page<>(page, pageSize);
        Page<WorkDTO> workDTOPage = new Page<>();

        Page<Work_info> PageWork = workService.page(pageInfo, wrapper);

        //获取到分页对象的真实信息(获取到组别信息)
        workDTOPage = workService.getWorkDTOPage(PageWork);

        return R.ok("分页查询成功").put("workDTOPage",workDTOPage);

    }

    //查看作品详情，获取到单个作品的详细信息(管理员、评委、教师功能)
    @GetMapping("get")
    public R get(Long id) {

        //获取到当前登录用户,判断是否登录
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }

        WorkDTO workDTO = workService.getWorkDTOById(id);

        return R.ok("查询成功").put("workDTO",workDTO);
    }

    //导出作品信息为excel表格(管理员功能)
    @GetMapping("export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1){
            return R.error("导出失败！非管理员不能导出比赛信息");
        }

        //作品、学校、参赛人
        String workName = null;
        String school = null;
        String teamUsername = null;

        if (params.get("workName") != null) {
            workName = params.get("workName").toString();
        }
        if (params.get("school") != null) {
            school = params.get("school").toString();
        }
        if (params.get("teamUsername") != null) {
            teamUsername = params.get("teamUsername").toString();
        }

        //条件构造器
        LambdaQueryWrapper<Work_info> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotEmpty(workName),Work_info::getWorkName, workName);
        wrapper.like(StringUtils.isNotEmpty(school),Work_info::getSchool, school);
        wrapper.like(StringUtils.isNotEmpty(teamUsername),Work_info::getTeamUsername, teamUsername);

        List<Work_info> workList = workService.list(wrapper);

        //转换成DTO
        List<WorkDTO> workDTOList = workService.getWorkDTOList(workList);

        // 表头数据
        List<Object> head = Arrays.asList("学校","报名组别","作品名称","参赛姓名","联系方式","时事平均分","提交时间");

        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        for (WorkDTO workDTO : workDTOList) {
            //用户数据
            List<Object> row = new ArrayList<>();
            row.add(workDTO.getSchool());
            row.add(workDTO.getGroupName());
            row.add(workDTO.getWorkName());
            row.add(workDTO.getTeamUsername());
            row.add(workDTO.getContactInfo());
            row.add(workDTO.getAveScore());
            row.add(workDTO.getCreateTime());
            sheetDataList.add(row);
        }
        // 导出数据
        ExcelUtils.export(response,"作品信息表", sheetDataList);

        return R.ok("导出成功");

    }

    //教师选手提交作品（教师功能）
    @PostMapping("submit")
    public R submit(WorkDTO wokrDTO) {

        boolean result = workService.addwokrDTO(wokrDTO);

        if(result){
            return R.ok("提交成功!");
        }else{
            return R.error("提交失败!");
        }
    }

    //用户修改比赛作品信息（用户功能）
    @PostMapping("update")
    public R update(WorkDTO wokrDTO) {

        //获取到当前登录用户,判断是否登录
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }

        boolean result = workService.updatewokrDTO(wokrDTO);

        if(result){
            return R.ok("修改成功!");
        }else{
            return R.error("修改失败!");
        }
    }


}
