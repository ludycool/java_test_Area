package com.topband.bluetooth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel("SysLog 操作日志")
@Data 
@TableName("s_sys_log")
public class SysLog extends  BaseEntity{

  @ApiModelProperty("主键")
@TableId(type = IdType.AUTO)
private long id;

  @ApiModelProperty("操作名称")
private String oname;

  @ApiModelProperty("操作内容")
private String ocontent;

  @ApiModelProperty("操作地址")
private String url;

  @ApiModelProperty("操作权限")
private String permission;

  @ApiModelProperty("ip地址")
private String ip;

  @ApiModelProperty("操作人id")
private String userId;

  @ApiModelProperty("操作人名称")
private String userName;

}

