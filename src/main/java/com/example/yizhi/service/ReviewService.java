package com.example.yizhi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yizhi.pojo.Group;
import com.example.yizhi.pojo.Work_review;
import com.example.yizhi.pojo.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService extends IService<Work_review> {
    Page<ReviewDTO> getReviewDTOPage(Page<Work_review> reviewPage);

    List<ReviewDTO> getReviewDTOList(List<Work_review> list);


}
