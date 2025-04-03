-- 将旧的category表迁移到新的product_category表
RENAME TABLE category TO product_category_old;

-- 创建新的product_category表
CREATE TABLE product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    icon VARCHAR(255) COMMENT '分类图标',
    description VARCHAR(255) COMMENT '分类描述',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '商品分类表';

-- 将旧表数据迁移到新表
INSERT INTO product_category (id, name, sort, deleted, create_time, update_time)
SELECT id, name, sort, deleted, createdTime, updatedTime FROM product_category_old;

-- 更新分类表数据，添加默认图标
UPDATE product_category SET icon = CONCAT('icon-', name) WHERE icon IS NULL;

-- 更新商品表，添加新字段
ALTER TABLE product
    ADD COLUMN code VARCHAR(50) AFTER name,
    ADD COLUMN market_price DECIMAL(10,2) AFTER price,
    ADD COLUMN cost_price DECIMAL(10,2) AFTER market_price,
    ADD COLUMN stock_warning INT DEFAULT 10 AFTER stock,
    ADD COLUMN images TEXT AFTER image_url,
    ADD COLUMN brief VARCHAR(255) AFTER images,
    ADD COLUMN specifications TEXT AFTER description,
    ADD COLUMN sales INT NOT NULL DEFAULT 0 AFTER specifications,
    ADD COLUMN is_recommended TINYINT(1) DEFAULT 0 AFTER sales,
    ADD COLUMN is_hot TINYINT(1) DEFAULT 0 AFTER is_recommended,
    ADD COLUMN is_new TINYINT(1) DEFAULT 0 AFTER is_hot,
    CHANGE status is_enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '商品状态：0-下架 1-上架',
    ADD COLUMN sort INT NOT NULL DEFAULT 0 AFTER is_enabled,
    ADD COLUMN version INT NOT NULL DEFAULT 0 AFTER sort,
    ADD KEY idx_code (code),
    ADD KEY idx_enabled_sort (is_enabled, sort);

-- 更新默认商品编码
UPDATE product SET code = CONCAT('SP', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(id, 3, '0')) WHERE code IS NULL;

-- 更新订单表，添加已完成状态和索引
ALTER TABLE orders
    MODIFY COLUMN status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付 1-已支付 2-已取消 3-已退款 4-已完成',
    ADD KEY idx_status (status);

-- 更新订单详情表，添加产品图片和小计字段
ALTER TABLE order_detail
    ADD COLUMN product_image VARCHAR(255) AFTER product_name,
    ADD COLUMN total_price DECIMAL(10,2) NOT NULL COMMENT '小计金额' AFTER quantity,
    ADD KEY idx_product (product_id);

-- 更新支付记录表，添加支付时间字段和交易号索引
ALTER TABLE payment
    ADD COLUMN pay_time DATETIME COMMENT '支付时间' AFTER status,
    ADD KEY idx_transaction_id (transaction_id);

-- 更新流程步骤表，添加排序索引
ALTER TABLE process_step
    ADD KEY idx_type_sort (type, sort);

-- 创建微信二维码登录令牌表（如果不存在）
CREATE TABLE IF NOT EXISTS wechat_qr_login_token (
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

-- 计算订单详情表中的小计金额
UPDATE order_detail SET total_price = product_price * quantity WHERE total_price IS NULL OR total_price = 0;
