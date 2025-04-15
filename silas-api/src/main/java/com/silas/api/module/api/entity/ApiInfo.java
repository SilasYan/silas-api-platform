package com.silas.api.module.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 *
 * @author Silas Yan
 * @TableName api_info
 */
@TableName(value = "api_info")
@Data
@Accessors(chain = true)
public class ApiInfo implements Serializable {

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 接口名称
	 */
	@TableField(value = "api_name")
	private String apiName;

	/**
	 * 接口地址
	 */
	@TableField(value = "api_url")
	private String apiUrl;

	/**
	 * 接口主机
	 */
	@TableField(value = "api_host")
	private String apiHost;

	/**
	 * 接口状态（0-上线, 1-下线, 2-维护）
	 */
	@TableField(value = "api_status")
	private Integer apiStatus;

	/**
	 * 接口描述
	 */
	@TableField(value = "api_description")
	private String apiDescription;

	/**
	 * 请求方法
	 */
	@TableField(value = "request_method")
	private String requestMethod;

	/**
	 * 请求头
	 */
	@TableField(value = "request_header")
	private String requestHeader;

	/**
	 * 响应头
	 */
	@TableField(value = "response_header")
	private String responseHeader;

	/**
	 * 创建用户Id
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 所需积分
	 */
	@TableField(value = "required_points")
	private Integer requiredPoints;

	/**
	 * 调用次数
	 */
	@TableField(value = "invoke_count")
	private Integer invokeCount;

	/**
	 * 是否删除（0-正常, 1-删除）
	 */
	@TableField(value = "is_delete")
	@TableLogic
	private Integer isDelete;

	/**
	 * 编辑时间
	 */
	@TableField(value = "edit_time", fill = FieldFill.UPDATE)
	private Date editTime;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}
