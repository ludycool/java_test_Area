package com.topband.bluetooth.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.topband.bluetooth.api.aop.OperationLog;
import com.topband.bluetooth.common.enums.BusinessError;
import com.topband.bluetooth.common.model.ResultModel0;
import com.topband.bluetooth.common.util.ResultModeHelper;
import com.topband.bluetooth.common.util.StringHelper;
import com.topband.bluetooth.service.BuildingService;
import com.topband.bluetooth.entity.Building;
import com.topband.bluetooth.serviceImpl.base.QueryUtil;
import com.topband.bluetooth.common.util.ComUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
/**
 * 描述:
 * Building相关接口
 *
 * @author 
 * @create 2019-10-31 10:47
 */

@Slf4j
@Api(description = "楼栋表 接口")
@RestController
@RequestMapping("/Building")
public class BuildingController extends BaseController {

    @Autowired
    private BuildingService opService;

    static final String  tableName = "d_building";
    static final  String primaryKey = "id";
	
    /**
     * 分页条件 查询表，视图等
     *
     * @param
     * @return 分页数据
     */
    @ApiOperation("查询 楼栋表")
    @PostMapping(value = "/searchList")
	@OperationLog(description = "查询 楼栋表")
    public ResultModel0<Map<String,Object>> searchList(@RequestBody Map<String, Object> pdata) {
        String fields = "id,name,parent_id as parentId,create_by as createBy,update_by as updateBy,create_time as createTime,update_time as updateTime,del_flag as delFlag";
        IPage page = QueryUtil.getPage(pdata);
        IPage<LinkedHashMap<String, Object>> ds = opService.listMapByMap(page, fields, tableName, pdata);
        return ResultModeHelper.succeedPage(ds.getRecords(),ds.getTotal());
    }

    /**
     * 添加数据
     *
     * @param EidModle
     * @return
     */
    @ApiOperation("添加 楼栋表")
    @PostMapping(value = "/addInfo")
	@OperationLog(description = "添加 楼栋表")
    public ResultModel0<String> addInfo(@RequestBody Building EidModle) {
        EidModle.setCreateTime(new Date());
		EidModle.setUpdateTime(new Date());
        EidModle.setDelFlag(false);
        EidModle.setId(ComUtil.getId());
		opService.save(EidModle); 
        return ResultModeHelper.succeed(EidModle.getId());
    }

    /**
     * 修改数据
     *
     * @param EidModle
     * @return
     */
    @ApiOperation("修改 楼栋表")
    @PostMapping(value = "/saveInfo")
	@OperationLog(description = "修改 楼栋表")
  public ResultModel0<Boolean> saveInfo(@RequestBody Map<String, Object> EidModle) {
        EidModle.put("updateTime", new Date());
        opService.updateInById(tableName, primaryKey, EidModle);
        return ResultModeHelper.succeed(true);
    }


    /**
     * 获取单条数据
     *
     * @param id
     * @return 实体
     */
    @ApiOperation("获取单条 楼栋表")
    @GetMapping("/getInfo/{id}")
	@OperationLog(description = "获取单条 楼栋表")
    public ResultModel0<Building> getInfo(@PathVariable Serializable id) {
        Building Rmodel = opService.getById(id);
        return ResultModeHelper.succeed(Rmodel);
    }

    /**
     * 删除数据
     *
     * @param idSet
     * @return
     */
    @ApiOperation("删除 楼栋表")
    @PostMapping("/del")
	@OperationLog(description = "删除 楼栋表")
    public ResultModel0<Boolean> del(@RequestParam("idSet") String idSet) {
        if (StringHelper.isEmpty(idSet)) {
            return ResultModeHelper.fail(BusinessError.STATUS_200001.getStatus());
        }
        String[] id_Set = idSet.split(",");
        List<String> list = new ArrayList<>();
        for (int i = id_Set.length - 1; i >= 0; i--) {
            if (!StringHelper.isEmpty(id_Set[i])) {
                list.add(id_Set[i]);
            }
        }
         opService.removeByIds(list);
         return ResultModeHelper.succeed(true);
  
    }

}
