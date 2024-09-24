package com.example.yizhi.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yizhi.pojo.Work_info;
import com.example.yizhi.pojo.dto.WorkDTO;

import java.util.List;


public interface WorkService extends IService<Work_info> {
    Page<WorkDTO> getWorkDTOPage(Page<Work_info> pageUser);

    WorkDTO getWorkDTOById(Long id);

    List<WorkDTO> getWorkDTOList(List<Work_info> workList);

    boolean addwokrDTO(WorkDTO wokrDTO);

    boolean updatewokrDTO(WorkDTO wokrDTO);
}
