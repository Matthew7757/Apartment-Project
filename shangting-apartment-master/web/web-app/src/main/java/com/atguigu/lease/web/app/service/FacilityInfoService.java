package com.atguigu.lease.web.app.service;

import com.atguigu.lease.model.entity.FacilityInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Matthew
* @description 针对表【facility_info(配套信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface FacilityInfoService extends IService<FacilityInfo> {

    List<FacilityInfo> selectListByRoomId(Long id);

    List<FacilityInfo> selectListByApartmentId(Long id);
}
