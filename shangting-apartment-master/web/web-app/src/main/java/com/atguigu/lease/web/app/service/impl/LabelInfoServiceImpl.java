package com.atguigu.lease.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.lease.model.entity.LabelInfo;
import com.atguigu.lease.web.app.service.LabelInfoService;
import com.atguigu.lease.web.app.mapper.LabelInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Matthew
* @description 针对表【label_info(标签信息表)】的数据库操作Service实现
* @createDate 2023-07-26 11:12:39
*/
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo>
    implements LabelInfoService{
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Override
    public List<LabelInfo> selectListByRoomId(Long id) {
        return labelInfoMapper.selectListByRoomId(id);
    }

    @Override
    public List<LabelInfo> selectListByApartmentId(Long id) {
        return labelInfoMapper.selectListByApartmentId(id);
    }
}




