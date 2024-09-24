package com.example.yizhi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yizhi.mapper.GroupMapper;
import com.example.yizhi.mapper.UserMapper;
import com.example.yizhi.mapper.Work_infoMapper;
import com.example.yizhi.mapper.Work_reviewMapper;
import com.example.yizhi.pojo.Group;
import com.example.yizhi.pojo.User;
import com.example.yizhi.pojo.Work_info;
import com.example.yizhi.pojo.Work_review;
import com.example.yizhi.pojo.dto.ReviewDTO;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.service.GroupService;
import com.example.yizhi.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl extends ServiceImpl<Work_reviewMapper, Work_review> implements ReviewService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Work_infoMapper work_infoMapper;

    @Autowired
    private HttpSession session;


    @Override
    public Page<ReviewDTO> getReviewDTOPage(Page<Work_review> reviewPage) {

        Page<ReviewDTO> reviewDTOPage = new Page<>();
        BeanUtils.copyProperties(reviewPage, reviewDTOPage);

        List<ReviewDTO> reviewDTOList = reviewPage.getRecords().stream().map(item -> {
            ReviewDTO reviewDTO = new ReviewDTO();
            BeanUtils.copyProperties(item, reviewDTO);

            //获取参赛
            String workid = item.getWorkid();
            Work_info work_info = work_infoMapper.selectById(workid);
            //获取组信息
            Group group = groupMapper.selectById(work_info.getGroupid());
            reviewDTO.setGroupName(group.getGroupName());
            //获取比赛名称
            reviewDTO.setWorkName(work_info.getWorkName());
            //获取学校名称
            reviewDTO.setSchool(work_info.getSchool());
            //获取参赛者姓名
            reviewDTO.setTeamUsername(work_info.getTeamUsername());
            //获取评委姓名
            User user = userMapper.selectById(item.getCreateUser());
            reviewDTO.setCreaterUsername(user.getName());

            return reviewDTO;
        }).collect(Collectors.toList());

        reviewDTOPage.setRecords(reviewDTOList);

        return reviewDTOPage;
    }

    @Override
    public List<ReviewDTO> getReviewDTOList(List<Work_review> list) {

        List<ReviewDTO> reviewDTOList = list.stream().map(item -> {
            ReviewDTO reviewDTO = new ReviewDTO();
            BeanUtils.copyProperties(item, reviewDTO);

            //获取参赛
            String workid = item.getWorkid();
            Work_info work_info = work_infoMapper.selectById(workid);
            //获取组信息
            Group group = groupMapper.selectById(work_info.getGroupid());
            reviewDTO.setGroupName(group.getGroupName());
            //获取比赛名称
            reviewDTO.setWorkName(work_info.getWorkName());
            //获取学校名称
            reviewDTO.setSchool(work_info.getSchool());
            //获取参赛者姓名
            reviewDTO.setTeamUsername(work_info.getTeamUsername());
            //获取评委姓名
            User user = userMapper.selectById(item.getCreateUser());
            reviewDTO.setCreaterUsername(user.getName());

            return reviewDTO;
        }).collect(Collectors.toList());

        return reviewDTOList;
    }

    @Override
    public boolean save(Work_review work_review) {
        work_review.setTotalScore(work_review.getScore1() + work_review.getScore2() +
                work_review.getScore3() + work_review.getScore4() +
                work_review.getScore5() + work_review.getScore6());
        // 获取当前登录用户
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        work_review.setCreateUser(user.getId());
        work_review.setCreateDatetime(LocalDateTime.now());

        // 保存评审信息
        boolean result = super.save(work_review);
        // 更新参赛信息的总成绩
        Work_info work_info = work_infoMapper.selectById(work_review.getWorkid());

        work_info.setAveScore(
                work_review.getScore1() +work_review.getScore2()+
                        work_review.getScore3() +work_review.getScore4()+
                        work_review.getScore5()+work_review.getScore6());

        work_infoMapper.updateById(work_info);



        return true;
    }
}
