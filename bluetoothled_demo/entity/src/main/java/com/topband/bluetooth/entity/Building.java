package com.topband.bluetooth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel("Building 楼栋表")
@Data 
@TableName("d_building")
public class Building extends  BaseEntity{

  @ApiModelProperty("主键")
@TableId(type = IdType.UUID)
private String id;

  @ApiModelProperty("楼栋名称")
private String name;

  @ApiModelProperty("父Id，当前记录项目主键")
private String parentId;

  @ApiModelProperty("创建人ID.")
private String createBy;

  @ApiModelProperty("修改人ID.")
private String updateBy;

}

