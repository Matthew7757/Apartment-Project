package com.atguigu.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.lease.model.entity.LabelInfo;
import com.atguigu.lease.web.admin.service.LabelInfoService;
import com.atguigu.lease.web.admin.mapper.LabelInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Matthew
* @description 针对表【label_info(标签信息表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo>
    implements LabelInfoService{
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Override
    public List<LabelInfo> selectListByRoomId(Long id) {
        return labelInfoMapper.queryLabelInfoList(id);
    }
}




