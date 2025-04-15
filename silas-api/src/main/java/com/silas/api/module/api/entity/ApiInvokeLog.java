package com.silas.api.module.api.entity;

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
 * 接口调用日志表
 *
 * @author Silas Yan
 * @TableName api_invoke_log
 */
@TableName(value = "api_invoke_log")
@Data
@Accessors(chain = true)
public class ApiInvokeLog implements Serializable {

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 接口ID
	 */
	@TableField(value = "api_id")
	private Long apiId;

	/**
	 * 接口名称
	 */
	@TableField(value = "api_name")
	private String apiName;

	/**
	 * 调用者ID（用户ID）
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 调用者IP
	 */
	@TableField(value = "caller_ip")
	private String callerIp;

	/**
	 * traceId 日志跟踪ID
	 */
	@TableField(value = "trace_id")
	private String traceId;

	/**
	 * 当前跟踪ID对应随机字符
	 */
	@TableField(value = "nonce")
	private String nonce;

	/**
	 * 当前跟踪ID对应时间戳
	 */
	@TableField(value = "timestamp")
	private String timestamp;

	/**
	 * 当前跟踪ID对应签证
	 */
	@TableField(value = "sign")
	private String sign;

	/**
	 * 调用时间
	 */
	@TableField(value = "invoke_time")
	private Date invokeTime;

	/**
	 * 请求地址
	 */
	@TableField(value = "request_uri")
	private String requestUri;

	/**
	 * 请求路径
	 */
	@TableField(value = "request_path")
	private String requestPath;

	/**
	 * 请求方法
	 */
	@TableField(value = "request_method")
	private String requestMethod;

	/**
	 * 请求头
	 */
	@TableField(value = "request_headers")
	private String requestHeaders;

	/**
	 * 请求参数
	 */
	@TableField(value = "request_params")
	private String requestParams;

	/**
	 * 响应结果
	 */
	@TableField(value = "response_result")
	private String responseResult;

	/**
	 * 调用状态（0-成功、1-失败）
	 */
	@TableField(value = "invoke_status")
	private Integer invokeStatus;

	/**
	 * 消耗积分
	 */
	@TableField(value = "consume_points")
	private Integer consumePoints;

	/**
	 * 耗时（单位: 毫秒）
	 */
	@TableField(value = "time_consuming")
	private Long timeConsuming;

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
