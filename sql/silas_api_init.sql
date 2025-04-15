CREATE DATABASE IF NOT EXISTS silas_api_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE silas_api_platform;


DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_account            VARCHAR(50)     NOT NULL COMMENT '账号',
    user_password           VARCHAR(512)    NOT NULL COMMENT '密码',
    user_email              VARCHAR(50)     NOT NULL COMMENT '用户邮箱',
    user_phone              VARCHAR(50)     NULL     DEFAULT NULL COMMENT '用户手机号',
    user_name               VARCHAR(256)    NULL     DEFAULT NULL COMMENT '用户昵称',
    user_avatar             VARCHAR(512)    NULL     DEFAULT NULL COMMENT '用户头像',
    user_profile            VARCHAR(512)    NULL     DEFAULT NULL COMMENT '用户简介',
    user_role               VARCHAR(20)     NULL     DEFAULT 'USER' COMMENT '用户角色（USER-普通用户, ADMIN-管理员）',
    access_key              VARCHAR(255)    NULL     DEFAULT NULL COMMENT 'accessKey 访问密钥',
    secret_key              VARCHAR(255)    NULL     DEFAULT NULL COMMENT 'secretKey 私密密钥',
    points                  INT             NULL     DEFAULT 0 COMMENT '积分',
    first_login_time        DATETIME        NULL     DEFAULT NULL COMMENT '首次登录时间',
    last_sign_in_time       DATETIME        NULL     DEFAULT NULL COMMENT '最后签到时间',
    continuous_sign_in_days INT             NULL     DEFAULT 0 COMMENT '连续签到天数',
    is_disabled             TINYINT         NOT NULL DEFAULT 0 COMMENT '是否禁用（0-正常, 1-禁用）',
    is_delete               TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除（0-正常, 1-删除）',
    edit_time               DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE INDEX uk_user_account (user_account ASC) USING BTREE,
    UNIQUE INDEX uk_user_email (user_email ASC) USING BTREE,
    UNIQUE INDEX uk_user_phone (user_phone ASC) USING BTREE,
    INDEX idx_user_name (user_name ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO user
VALUES (1, 'admin', '4da52d1bde121bf42268187a66683ccb',
        '510132075@qq.com', '15279292310', '管理员', NULL, NULL,
        'ADMIN', 'admin', 'admin', 0, NULL,
        NULL, DEFAULT,
        DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

DROP TABLE IF EXISTS user_login_log;
CREATE TABLE user_login_log
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT          NOT NULL COMMENT '用户ID',
    login_ip    VARCHAR(64)     NOT NULL COMMENT '登录IP',
    login_time  DATETIME        NOT NULL COMMENT '登录时间',
    user_agent  VARCHAR(256)    NULL     DEFAULT NULL COMMENT '登录设备信息',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户登录日志表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS user_points_log;
CREATE TABLE user_points_log
(
    id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id          BIGINT          NOT NULL COMMENT '用户ID',
    operate_type     TINYINT         NOT NULL COMMENT '操作类型（0-新增, 1-减少）',
    operate_quantity INT             NOT NULL COMMENT '操作数量',
    operate_desc     VARCHAR(64)     NOT NULL COMMENT '操作描述',
    create_time      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户积分日志表'
  ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS platform_config;
CREATE TABLE platform_config
(
    id           INT UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '主键',
    config_name  VARCHAR(255)     NOT NULL DEFAULT '' COMMENT '配置名称',
    config_key   VARCHAR(64)      NOT NULL DEFAULT '' COMMENT '配置KEY',
    config_value VARCHAR(255)     NOT NULL DEFAULT '' COMMENT '配置内容',
    config_type  TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '配置类型（0-文本, 1-数字, 2-JSON对象, 3-JSON数组）',
    status       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态（0-启用, 1-禁用）',
    is_delete    TINYINT          NOT NULL DEFAULT 0 COMMENT '是否删除（0-正常, 1-删除）',
    edit_time    DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    create_time  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='平台配置表';


DROP TABLE IF EXISTS api_info;
CREATE TABLE api_info
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    api_name        VARCHAR(128)    NOT NULL COMMENT '接口名称',
    api_url         VARCHAR(512)    NOT NULL COMMENT '接口地址',
    api_host        VARCHAR(512)    NOT NULL COMMENT '接口主机',
    api_status      TINYINT         NOT NULL DEFAULT 0 COMMENT '接口状态（0-开启, 1-关闭）',
    api_description VARCHAR(256)    NULL     DEFAULT NULL COMMENT '接口描述',
    request_method  VARCHAR(128)    NULL     DEFAULT NULL COMMENT '请求方法',
    request_header  TEXT            NULL     DEFAULT NULL COMMENT '请求头',
    response_header TEXT            NULL     DEFAULT NULL COMMENT '响应头',
    user_id         BIGINT          NULL     DEFAULT NULL COMMENT '创建用户Id',
    required_points INT             NOT NULL DEFAULT 0 COMMENT '所需积分',
    invoke_count    INT             NOT NULL DEFAULT 0 COMMENT '调用次数',
    is_delete       TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除（0-正常, 1-删除）',
    edit_time       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '接口信息表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS api_invoke_log;
CREATE TABLE api_invoke_log
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    api_id          BIGINT          NOT NULL COMMENT '接口ID',
    api_name        VARCHAR(128)    NOT NULL COMMENT '接口名称',
    user_id         BIGINT          NOT NULL COMMENT '调用者ID（用户ID）',
    caller_ip       VARCHAR(128)    NOT NULL COMMENT '调用者IP',
    trace_id        VARCHAR(128)    NOT NULL COMMENT 'traceId 日志跟踪ID',
    nonce           VARCHAR(128)    NOT NULL COMMENT '当前跟踪ID对应随机字符',
    timestamp       VARCHAR(128)    NOT NULL COMMENT '当前跟踪ID对应时间戳',
    sign            VARCHAR(256)    NOT NULL COMMENT '当前跟踪ID对应签证',
    invoke_time     DATETIME        NOT NULL COMMENT '调用时间',
    request_uri     VARCHAR(512)    NOT NULL COMMENT '请求地址',
    request_path    VARCHAR(128)    NOT NULL COMMENT '请求路径',
    request_method  VARCHAR(128)    NOT NULL COMMENT '请求方法',
    request_header  TEXT            NULL     DEFAULT NULL COMMENT '请求头',
    request_param   TEXT            NULL     DEFAULT NULL COMMENT '请求参数',
    response_header TEXT            NULL     DEFAULT NULL COMMENT '响应头',
    response_body   TEXT            NULL     DEFAULT NULL COMMENT '响应体',
    invoke_status   INT             NOT NULL COMMENT '调用状态（0-成功、1-失败）',
    consume_points  INT             NOT NULL DEFAULT 0 COMMENT '消耗积分',
    time_consuming  BIGINT          NOT NULL COMMENT '耗时（单位: 毫秒）',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '接口调用日志表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS blacklist;
CREATE TABLE blacklist
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    blacklist_type  TINYINT         NOT NULL COMMENT '黑名单类型（0-IP, 1-UserId）',
    blacklist_value VARCHAR(128)    NOT NULL COMMENT '黑名单值',
    blacklist_desc  VARCHAR(256)    NOT NULL COMMENT '黑名单描述',
    is_delete       TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除（0-正常, 1-删除）',
    edit_time       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单'
  ROW_FORMAT = DYNAMIC;
