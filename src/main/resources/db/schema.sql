-- 创建数据库
CREATE DATABASE IF NOT EXISTS funeral_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE funeral_db;

-- 角色表
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) COMMENT '角色名称',
    code VARCHAR(50) UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 用户角色关联表
CREATE TABLE user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '用户ID',
    role_id BIGINT COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
);

-- 用户表
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) UNIQUE COMMENT '用户名',
    password VARCHAR(100) COMMENT '加密后的密码',
    role TINYINT(1) DEFAULT 0 COMMENT '角色：0-普通用户 1-管理员',
    unionid VARCHAR(64) COMMENT '微信unionId',
    openid VARCHAR(64) NOT NULL COMMENT '微信openid',
    nickname VARCHAR(32) COMMENT '用户昵称',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(50) COMMENT '邮箱',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    UNIQUE KEY uk_openid (openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';

-- 商品分类表
CREATE TABLE product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    icon VARCHAR(255) COMMENT '分类图标',
    description VARCHAR(255) COMMENT '分类描述',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '商品分类表';

-- 商品表
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    code VARCHAR(50) COMMENT '商品编码',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    market_price DECIMAL(10,2) COMMENT '市场价格',
    cost_price DECIMAL(10,2) COMMENT '成本价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    stock_warning INT DEFAULT 10 COMMENT '库存预警值',
    image_url VARCHAR(255) COMMENT '商品主图',
    images TEXT COMMENT '商品图集，多个图片URL以逗号分隔',
    brief VARCHAR(255) COMMENT '商品简介',
    description TEXT COMMENT '商品详情（富文本）',
    specifications TEXT COMMENT '商品规格参数，JSON格式',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    is_recommended TINYINT(1) DEFAULT 0 COMMENT '是否推荐：0-否 1-是',
    is_hot TINYINT(1) DEFAULT 0 COMMENT '是否热销：0-否 1-是',
    is_new TINYINT(1) DEFAULT 0 COMMENT '是否新品：0-否 1-是',
    is_enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '商品状态：0-下架 1-上架',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    KEY idx_category_id (category_id),
    KEY idx_code (code),
    KEY idx_enabled_sort (is_enabled, sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '商品表';

-- 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付 1-已支付 2-已取消 3-已退款 4-已完成',
    contact_name VARCHAR(32) COMMENT '联系人姓名',
    contact_phone VARCHAR(20) COMMENT '联系人电话',
    address VARCHAR(255) COMMENT '服务地址',
    service_time DATETIME COMMENT '服务时间',
    remark VARCHAR(255) COMMENT '备注',
    delivery_type TINYINT NOT NULL DEFAULT 0 COMMENT '配送方式：0-自提 1-配送',
    qr_code_url TEXT COMMENT '二维码URL',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '订单表';

-- 订单详情表
CREATE TABLE order_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id VARCHAR(32) NOT NULL COMMENT '订单编号',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL COMMENT '购买数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    KEY idx_order (order_id),
    KEY idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '订单详情表';

-- 支付记录表
CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    transaction_id VARCHAR(64) COMMENT '微信支付交易号',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-未支付 1-支付成功 2-支付失败',
    pay_time DATETIME COMMENT '支付时间',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    KEY idx_order_no (order_no),
    KEY idx_transaction_id (transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '支付记录表';

-- 流程步骤表
CREATE TABLE process_step (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    type TINYINT NOT NULL COMMENT '流程类型：0-白事 1-红事',
    step_name VARCHAR(50) NOT NULL COMMENT '步骤名称',
    description TEXT COMMENT '详细说明',
    product_ids TEXT COMMENT '关联商品ID列表，JSON格式',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序号',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除',
    KEY idx_type_sort (type, sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '流程步骤表';

-- 购物车表
CREATE TABLE cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '总价',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车项表';

-- 微信二维码登录令牌表
CREATE TABLE wechat_qr_login_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    token VARCHAR(64) NOT NULL COMMENT '令牌值',
    openid VARCHAR(64) COMMENT '微信openid',
    user_info TEXT COMMENT '用户信息JSON',
    status TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0-未登录 1-已登录 2-已过期 3-登录失败',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    UNIQUE KEY uk_token (token),
    KEY idx_openid (openid),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '微信二维码登录令牌表'; 