package com.silas.api.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户积分日志表
 *
 * @author Silas Yan
 * @TableName user_points_log
 */
@TableName(value = "user_points_log")
@Data
@Accessors(chain = true)
public class UserPointsLog implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 操作类型（0-新增, 1-减少）
	 */
	@TableField(value = "operate_type")
	private Integer operateType;

	/**
	 * 操作数量
	 */
	@TableField(value = "operate_quantity")
	private Integer operateQuantity;

	/**
	 * 操作描述
	 */
	@TableField(value = "operate_desc")
	private String operateDesc;

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
