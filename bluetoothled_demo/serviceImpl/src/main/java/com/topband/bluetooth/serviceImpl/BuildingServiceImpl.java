package com.topband.bluetooth.serviceImpl;

import com.topband.bluetooth.serviceImpl.base.BaseServiceImpl;
import com.topband.bluetooth.entity.Building;
import com.topband.bluetooth.mapper.BuildingMapper;
import com.topband.bluetooth.service.BuildingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 楼栋表 ServiceImpl实现类
 */
@Service
public class BuildingServiceImpl extends BaseServiceImpl<BuildingMapper, Building> implements BuildingService {


}
