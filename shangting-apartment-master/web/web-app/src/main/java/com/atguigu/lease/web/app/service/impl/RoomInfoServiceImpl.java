package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import com.atguigu.lease.common.context.LoginUserContext;
import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.model.enums.LeaseStatus;
import com.atguigu.lease.web.app.mapper.*;
import com.atguigu.lease.web.app.service.*;
import com.atguigu.lease.web.app.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.app.vo.attr.AttrValueVo;
import com.atguigu.lease.web.app.vo.fee.FeeValueVo;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.atguigu.lease.web.app.vo.room.RoomDetailVo;
import com.atguigu.lease.web.app.vo.room.RoomItemVo;
import com.atguigu.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Matthew
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Lazy
    @Autowired
    private ApartmentInfoService apartmentInfoService;
    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private AttrValueService attrValueService;
    @Autowired
    private FacilityInfoService facilityInfoService;
    @Autowired
    private LabelInfoService labelInfoService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private LeaseTermService leaseTermService;
    @Autowired
    private FeeValueService feeValueService;
    @Lazy
    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public void roomInfoPage(Page<RoomItemVo> page, RoomQueryVo queryVo) {
        roomInfoMapper.roomInfoPage(page, queryVo);
    }

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {

        String key = RedisConstant.APP_ROOM_PREFIX +id;
        RoomDetailVo roomDetailVoRedis = (RoomDetailVo)redisTemplate.opsForValue().get(key);
        if(roomDetailVoRedis ==null){
            //1.查询RoomInfo
            RoomInfo roomInfo = roomInfoMapper.selectRoomById(id);
            if (roomInfo == null) {
                return null;
            }

            //2.查询所属公寓信息
            ApartmentItemVo apartmentItemVo = apartmentInfoService.getApartmentItemVoById(roomInfo.getApartmentId());

            //3.查询graphInfoList
            List<GraphVo> graphVoList = graphInfoService.selectListByItemTypeAndId(ItemType.ROOM, id);

            //4.查询attrValueList
            List<AttrValueVo> attrvalueVoList = attrValueService.selectListByRoomId(id);

            //5.查询facilityInfoList
            List<FacilityInfo> facilityInfoList = facilityInfoService.selectListByRoomId(id);

            //6.查询labelInfoList
            List<LabelInfo> labelInfoList = labelInfoService.selectListByRoomId(id);

            //7.查询paymentTypeList
            List<PaymentType> paymentTypeList = paymentTypeService.selectListByRoomId(id);

            //8.查询leaseTermList
            List<LeaseTerm> leaseTermList = leaseTermService.selectListByRoomId(id);

            //9.查询费用项目信息
            List<FeeValueVo> feeValueVoList = feeValueService.selectListByApartmentId(roomInfo.getApartmentId());

            //10.查询房间入住状态
            LambdaQueryWrapper<LeaseAgreement> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LeaseAgreement::getRoomId, roomInfo.getId());
            queryWrapper.in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING);
            Long singedCount = leaseAgreementService.count(queryWrapper);


            BeanUtils.copyProperties(roomInfo, roomDetailVoRedis);
            roomDetailVoRedis.setIsDelete(roomInfo.getIsDeleted() == 1);
            roomDetailVoRedis.setIsCheckIn(singedCount > 0);

            roomDetailVoRedis.setApartmentItemVo(apartmentItemVo);
            roomDetailVoRedis.setGraphVoList(graphVoList);
            roomDetailVoRedis.setAttrValueVoList(attrvalueVoList);
            roomDetailVoRedis.setFacilityInfoList(facilityInfoList);
            roomDetailVoRedis.setLabelInfoList(labelInfoList);
            roomDetailVoRedis.setPaymentTypeList(paymentTypeList);
            roomDetailVoRedis.setFeeValueVoList(feeValueVoList);
            roomDetailVoRedis.setLeaseTermList(leaseTermList);

            System.out.println("湖获取房间详情："+Thread.currentThread().getName());
            redisTemplate.opsForValue().set(key,roomDetailVoRedis);
        }



        browsingHistoryService.saveHistory(LoginUserContext.getLoginUser().getUserId(), id);

        return roomDetailVoRedis;
    }

    @Override
    public BigDecimal selectMinRentByApartmentId(Long id) {
        return roomInfoMapper.selectMinRentByApartmentId(id);
    }

    @Override
    public void pageItemByApartmentId(Page<RoomItemVo> page, Long id) {
        roomInfoMapper.pageItemByApartmentId(page, id);
    }

    @Override
    public RoomInfo selectRoomById(Long roomId) {
        return roomInfoMapper.selectRoomById(roomId);
    }
}




