package com.atguigu.lease.web.app.service;

import com.atguigu.lease.model.entity.GraphInfo;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Matthew
* @description 针对表【graph_info(图片信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface GraphInfoService extends IService<GraphInfo> {

    List<GraphVo> selectListByItemTypeAndId(ItemType room, Long id);

    List<GraphInfo> selectGraphInfoListByItemTypeAndId(ItemType room, Long id);
}
