package com.example.yizhi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yizhi.common.FilePathHolder;
import com.example.yizhi.mapper.GroupMapper;
import com.example.yizhi.mapper.Work_fileMapper;
import com.example.yizhi.mapper.Work_infoMapper;
import com.example.yizhi.pojo.Group;
import com.example.yizhi.pojo.Work_file;
import com.example.yizhi.pojo.Work_info;
import com.example.yizhi.pojo.dto.UserDTO;
import com.example.yizhi.pojo.dto.WorkDTO;
import com.example.yizhi.service.WorkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WorkServiceImpl extends ServiceImpl<Work_infoMapper, Work_info> implements WorkService {

    @Autowired
    private Work_infoMapper work_infoMapper;
    @Autowired
    private Work_fileMapper work_fileMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private HttpSession session;

    //分页查询作品信息dto
    @Override
    public Page<WorkDTO> getWorkDTOPage(Page<Work_info> pageWork) {
        Page<WorkDTO> pageWorkdto = new Page<>();
        //将pageUser中的属性复制到pageWork中
        BeanUtils.copyProperties(pageWork, pageWorkdto);

        //获取到pageWorkdto中的records
        List<WorkDTO> workDtoList = pageWork.getRecords().stream().map(work -> {
            //将group_id转换成group_name
            WorkDTO workDTO = new WorkDTO();
            BeanUtils.copyProperties(work, workDTO);

            Group group = groupMapper.selectById(work.getGroupid());
            workDTO.setGroupName(group.getGroupName());

            return workDTO;
        }).collect(Collectors.toList());

        //遍历workInfo，将每一个workInfo转换成WorkDTO


        pageWorkdto.setRecords(workDtoList);

        return pageWorkdto;
    }

    //根据id获取作品信息dto
    @Override
    public WorkDTO getWorkDTOById(Long id) {
        Work_info workInfo = work_infoMapper.selectById(id);
        WorkDTO workDTO = new WorkDTO();
        BeanUtils.copyProperties(workInfo, workDTO);

        //获取到参赛组别信息
        Group group = groupMapper.selectById(workInfo.getGroupid());
        workDTO.setGroupName(group.getGroupName());

        //获取到作品文件信息实体类
        LambdaQueryWrapper<Work_file> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Work_file::getWorkId, workInfo.getId());

        List<Work_file> workFiles = work_fileMapper.selectList(wrapper);

        //将作品文件信息复制到WorkDTO中
        if (workFiles!= null) {
            //查到课堂实录文件
            for (Work_file workFile : workFiles) {
                //课堂实录
                if (workFile.getWorkTypeid() == 1) {
                    workDTO.getVideaFileNames().add(workFile.getFileName());
                }
                //教案 teachplanFileName;
                if (workFile.getWorkTypeid() == 2){
                    workDTO.setTeachplanFileName(workFile.getFileName());
                }
                //教学时事报告 newsFileName;
                if (workFile.getWorkTypeid() == 3){
                    workDTO.setNewsFileName(workFile.getFileName());
                }
                //专业人才培养方案 cetFileName;
                if (workFile.getWorkTypeid() == 4){
                    workDTO.setCetFileName(workFile.getFileName());
                }
                //可成标准 standardFileName;
                if (workFile.getWorkTypeid() == 5){
                    workDTO.setStandardFileName(workFile.getFileName());
                }
                //教材选用说明 textbookFileName
                if (workFile.getWorkTypeid() == 6) {
                    workDTO.setTextbookFileName(workFile.getFileName());
                }

            }
            //文件所在路径 filePath
            workDTO.setFilePath(FilePathHolder.getFilePath());
        }

        return workDTO;
    }

    //根据作品信息列表获取作品信息dto列表
    @Override
    public List<WorkDTO> getWorkDTOList(List<Work_info> workList) {

        //获取到pageWorkdto中的records
        List<WorkDTO> workDtoList = workList.stream().map(work -> {
            //将group_id转换成group_name
            WorkDTO workDTO = new WorkDTO();
            BeanUtils.copyProperties(work, workDTO);

            Group group = groupMapper.selectById(work.getGroupid());
            workDTO.setGroupName(group.getGroupName());

            return workDTO;
        }).collect(Collectors.toList());


        return workDtoList;
    }

    @Override
    public boolean addwokrDTO(WorkDTO wokrDTO) {
        Work_info workInfo = new Work_info();
        BeanUtils.copyProperties(wokrDTO, workInfo);

        //获取当前用户
        UserDTO currentUser  = (UserDTO) session.getAttribute("currentUser");

        workInfo.setCreateUser(currentUser.getId());
        workInfo.setUpdateUser(currentUser.getId());
        workInfo.setCreateTime(LocalDateTime.now());
        workInfo.setUpdateTime(LocalDateTime.now());

        //保存作品信息
        work_infoMapper.insert(workInfo);

        //保存作品文件信息
        Work_file workFile = new Work_file();

        //保存课堂实录文件
        if (wokrDTO.getVideaFileNames().size() > 0) {

            for (String videaFileName : wokrDTO.getVideaFileNames()) {
                workFile.setFileName(videaFileName);
                workFile.setWorkTypeid(1L);
                workFile.setWorkId(workInfo.getId());
                work_fileMapper.insert(workFile);
            }
        }
        //教案 teachplanFileName;
        if (wokrDTO.getTeachplanFileName() != null) {
            workFile.setFileName(wokrDTO.getTeachplanFileName());
            workFile.setWorkTypeid(2L);
            workFile.setWorkId(workInfo.getId());
            work_fileMapper.insert(workFile);
        }
        //教学时事报告 newsFileName;
        if (wokrDTO.getNewsFileName() != null) {
            workFile.setFileName(wokrDTO.getNewsFileName());
            workFile.setWorkTypeid(3L);
            workFile.setWorkId(workInfo.getId());
            work_fileMapper.insert(workFile);
        }
        //专业人才培养方案 cetFileName;
        if (wokrDTO.getCetFileName() != null) {
            workFile.setFileName(wokrDTO.getCetFileName());
            workFile.setWorkTypeid(4L);
            workFile.setWorkId(workInfo.getId());
            work_fileMapper.insert(workFile);
        }
        //可成标准 standardFileName;
        if (wokrDTO.getStandardFileName() != null) {
            workFile.setFileName(wokrDTO.getStandardFileName());
            workFile.setWorkTypeid(5L);
            workFile.setWorkId(workInfo.getId());
            work_fileMapper.insert(workFile);
        }
        //教材选用说明 textbookFileName
        if (wokrDTO.getTextbookFileName() != null) {
            workFile.setFileName(wokrDTO.getTextbookFileName());
            workFile.setWorkTypeid(6L);
            workFile.setWorkId(workInfo.getId());
            work_fileMapper.insert(workFile);
        }



        return true;
    }

    @Override
    public boolean updatewokrDTO(WorkDTO wokrDTO) {
        //修改思路：先根据原有id查询出文件id然后删除掉，在把新的加入
        LambdaQueryWrapper<Work_file> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Work_file::getWorkId, wokrDTO.getId());
        work_fileMapper.delete(wrapper);

        //保存作品文件信息
        Work_file workFile = new Work_file();

        //保存课堂实录文件
        if (wokrDTO.getVideaFileNames().size() > 0) {

            for (String videaFileName : wokrDTO.getVideaFileNames()) {
                workFile.setFileName(videaFileName);
                workFile.setWorkTypeid(1L);
                workFile.setWorkId(wokrDTO.getId());
                work_fileMapper.insert(workFile);
            }
        }
        //教案 teachplanFileName;
        if (wokrDTO.getTeachplanFileName() != null) {
            workFile.setFileName(wokrDTO.getTeachplanFileName());
            workFile.setWorkTypeid(2L);
            workFile.setWorkId(wokrDTO.getId());
            work_fileMapper.insert(workFile);
        }
        //教学时事报告 newsFileName;
        if (wokrDTO.getNewsFileName() != null) {
            workFile.setFileName(wokrDTO.getNewsFileName());
            workFile.setWorkTypeid(3L);
            workFile.setWorkId(wokrDTO.getId());
            work_fileMapper.insert(workFile);
        }
        //专业人才培养方案 cetFileName;
        if (wokrDTO.getCetFileName() != null) {
            workFile.setFileName(wokrDTO.getCetFileName());
            workFile.setWorkTypeid(4L);
            workFile.setWorkId(wokrDTO.getId());
            work_fileMapper.insert(workFile);
        }
        //可成标准 standardFileName;
        if (wokrDTO.getStandardFileName() != null) {
            workFile.setFileName(wokrDTO.getStandardFileName());
            workFile.setWorkTypeid(5L);
            workFile.setWorkId(wokrDTO.getId());
            work_fileMapper.insert(workFile);
        }
        //教材选用说明 textbookFileName
        if (wokrDTO.getTextbookFileName() != null) {
            workFile.setFileName(wokrDTO.getTextbookFileName());
            workFile.setWorkTypeid(6L);
            workFile.setWorkId(wokrDTO.getId());
            work_fileMapper.insert(workFile);
        }

        //work_info直接修改即可
        Work_info workInfo = new Work_info();
        BeanUtils.copyProperties(wokrDTO, workInfo);

        //获取当前用户
        UserDTO currentUser  = (UserDTO) session.getAttribute("currentUser");
        workInfo.setUpdateUser(currentUser.getId());
        workInfo.setUpdateTime(LocalDateTime.now());

        work_infoMapper.updateById(workInfo);
        return true;
    }
}
