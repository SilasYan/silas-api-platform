package com.silas.api.module.blacklist.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 黑名单
 *
 * @author Silas Yan
 * @TableName blacklist
 */
@TableName(value = "blacklist")
@Data
@Accessors(chain = true)
public class Blacklist implements Serializable {

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 黑名单类型（0-IP, 1-UserId）
	 */
	@TableField(value = "blacklist_type")
	private Integer blacklistType;

	/**
	 * 黑名单值
	 */
	@TableField(value = "blacklist_value")
	private String blacklistValue;

	/**
	 * 黑名单描述
	 */
	@TableField(value = "blacklist_desc")
	private String blacklistDesc;

	/**
	 * 是否删除（0-正常, 1-删除）
	 */
	@TableField(value = "is_delete")
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
