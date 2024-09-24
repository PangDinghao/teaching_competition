package com.example.yizhi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yizhi.common.R;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.Work_info;
import com.example.yizhi.pojo.Work_review;
import com.example.yizhi.pojo.dto.ReviewDTO;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.service.ReviewService;
import com.example.yizhi.service.UserService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private WorkService workService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    //分页查询(管理员功能)
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params)    {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1 ){
            return R.error("查询失败！非管理员不能查询评价信息");
        }

        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        //评委、作品名、学校名、参赛者名字、报名组别
        String reviewerName = null;
        String workName = null;
        String schoolName = null;
        String teamUsername = null;
        Long groupid = null;

        if(params.get("reviewerName")!= null){
            reviewerName = params.get("reviewerName").toString();
        }
        if(params.get("workName")!= null){
            workName = params.get("workName").toString();
        }
        if(params.get("schoolName")!= null){
            schoolName = params.get("schoolName").toString();
        }
        if(params.get("teamUsername")!= null){
            teamUsername = params.get("teamUsername").toString();
        }
        if(params.get("groupid")!= null && params.get("groupid").toString().length() > 0){
            groupid = params.get("groupid") == null? null : Long.parseLong(params.get("groupid").toString());
        }

        //条件构造器
        LambdaQueryWrapper<Work_info> workWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Work_review> reviewWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();

        //根据评委名字查id
        List<User> reviewers = new ArrayList<>();
        if (reviewerName!= null){

            userWrapper.like(StringUtils.isNotEmpty(reviewerName),User::getName, reviewerName);
            reviewers = userService.list(userWrapper);
            if (reviewers != null && reviewers.size() > 0) {
                // 确保reviewer不是null再进行id访问
                List<Long> reviewerIds = reviewers.stream().map(User::getId).collect(Collectors.toList());
                reviewWrapper.in(Work_review::getCreateUser, reviewerIds);
            }else{
                return R.error().put("msg", "未找到该评委");
            }
        }
        List<Work_info> works = new ArrayList<>();
        //根据条件构造查询条件
        if(StringUtils.isNotEmpty(workName)
                || StringUtils.isNotEmpty(schoolName)
                || groupid != null
                || StringUtils.isNotEmpty(teamUsername)){
            //根据作品、学校名、报名组别、参赛者名字名查id
            workWrapper.like(StringUtils.isNotEmpty(workName), Work_info::getWorkName, workName)
                    .like(StringUtils.isNotEmpty(schoolName), Work_info::getSchool, schoolName)
                    .eq(groupid != null, Work_info::getGroupid, groupid)
                    .like(StringUtils.isNotEmpty(teamUsername), Work_info::getTeamUsername, teamUsername);

            works = workService.list(workWrapper);
        }

        //根据作品id查评价\

        if (works != null && works.size() > 0){
            List<Long> workIds = works.stream().map(Work_info::getId).collect(Collectors.toList());
            reviewWrapper.in(Work_review::getWorkid, workIds);
        }else {
            return R.error().put("msg", "未找到该信息");
        }

        //分页构造器
        Page<Work_review> ReviewPage = new Page<>(page, pageSize);
        reviewService.page(ReviewPage, reviewWrapper);

        //获取分页对象的Dto对象
        Page<ReviewDTO> reviewDTOPage = reviewService.getReviewDTOPage(ReviewPage);


        return R.ok().put("pageData", reviewDTOPage);

    }

    //导出评价信息(管理员功能)
    @GetMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response)    {

        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 1 ){
            return R.error("导出失败！非管理员不能导出评价信息");
        }

        //评委、作品名、学校名、参赛者名字、报名组别
        String reviewerName = null;
        String workName = null;
        String schoolName = null;
        String teamUsername = null;
        Long groupid = null;

        if(params.get("reviewerName")!= null){
            reviewerName = params.get("reviewerName").toString();
        }
        if(params.get("workName")!= null){
            workName = params.get("workName").toString();
        }
        if(params.get("schoolName")!= null){
            schoolName = params.get("schoolName").toString();
        }
        if(params.get("teamUsername")!= null){
            teamUsername = params.get("teamUsername").toString();
        }
        if(params.get("groupid")!= null && params.get("groupid").toString().length() > 0){
            groupid = params.get("groupid") == null? null : Long.parseLong(params.get("groupid").toString());
        }

        //条件构造器
        LambdaQueryWrapper<Work_info> workWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Work_review> reviewWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();

        //根据评委名字查id
        List<User> reviewers = new ArrayList<>();
        if (reviewerName!= null){

            userWrapper.like(StringUtils.isNotEmpty(reviewerName),User::getName, reviewerName);
            reviewers = userService.list(userWrapper);
            if (reviewers != null && reviewers.size() > 0) {
                // 确保reviewer不是null再进行id访问
                List<Long> reviewerIds = reviewers.stream().map(User::getId).collect(Collectors.toList());
                reviewWrapper.in(Work_review::getCreateUser, reviewerIds);
            }else{
                return R.error().put("msg", "未找到该评委");
            }
        }
        List<Work_info> works = new ArrayList<>();
        //根据条件构造查询条件
        if(StringUtils.isNotEmpty(workName)
                || StringUtils.isNotEmpty(schoolName)
                || groupid != null
                || StringUtils.isNotEmpty(teamUsername)){
            //根据作品、学校名、报名组别、参赛者名字名查id
            workWrapper.like(StringUtils.isNotEmpty(workName), Work_info::getWorkName, workName)
                    .like(StringUtils.isNotEmpty(schoolName), Work_info::getSchool, schoolName)
                    .eq(groupid != null, Work_info::getGroupid, groupid)
                    .like(StringUtils.isNotEmpty(teamUsername), Work_info::getTeamUsername, teamUsername);

            works = workService.list(workWrapper);
        }

        //根据作品id查评价

        if (works != null && works.size() > 0){
            List<Long> workIds = works.stream().map(Work_info::getId).collect(Collectors.toList());
            reviewWrapper.in(Work_review::getWorkid, workIds);
        }

        List<Work_review> list = reviewService.list(reviewWrapper);
        //转换成Dto对象
        List<ReviewDTO> reviewDTOList = reviewService.getReviewDTOList(list);

        //导出excel
        //表头信息
        List<Object> head = Arrays.asList("学校","报名组别","评委姓名","参赛者姓名","教学实施","教案","视频资料",
                "专业人才培训方案","课程标准","教材选用","总得分","评分时间");
        //数据信息
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        for (ReviewDTO reviewDTO : reviewDTOList){
            List<Object> row = new ArrayList<>();
            row.add(reviewDTO.getSchool());
            row.add(reviewDTO.getGroupName());
            row.add(reviewDTO.getCreaterUsername());
            row.add(reviewDTO.getTeamUsername());
            row.add(reviewDTO.getScore1());
            row.add(reviewDTO.getScore2());
            row.add(reviewDTO.getScore3());
            row.add(reviewDTO.getScore4());
            row.add(reviewDTO.getScore5());
            row.add(reviewDTO.getScore6());
            row.add(reviewDTO.getTotalScore());
            row.add(reviewDTO.getReviewDetile());
            sheetDataList.add(row);
        }

        // 导出数据
        ExcelUtils.export(response,"作品评价信息表", sheetDataList);

        return R.ok().put("list", reviewDTOList);


    }

    //评委打分评价（评委功能）
    @PostMapping("/score")
    public R score(Work_review workReview)    {
        //获取到当前登录用户,并进行权限判断
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        if(session.getAttribute("currentUser") == null){
            return R.error("请先登录！");
        }
        if (currentUser.getRoleid() != 2 ){
            return R.error("评价失败！非评委不能评价作品");
        }
        //新增评价并更新work_info表的成绩
        boolean save = reviewService.save(workReview);
        if(save){
            return R.ok().put("msg", "评价成功");
        }else{
            return R.error().put("msg", "评价失败");
        }
    }



}
