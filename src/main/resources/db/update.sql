
ALTER TABLE product
ADD COLUMN category TINYINT(1) COMMENT '分类：0-白事，1-红事' AFTER name,
ADD COLUMN sub_category VARCHAR(50) COMMENT '子分类' AFTER category,
MODIFY COLUMN description TEXT COMMENT '商品富文本描述';

ALTER TABLE orders
ADD COLUMN process_step_id BIGINT COMMENT '关联的流程步骤ID',
ADD COLUMN process_info TEXT COMMENT '流程信息快照',
ADD COLUMN delivery_time DATETIME COMMENT '配送时间',
ADD COLUMN version INT DEFAULT 1 COMMENT '乐观锁版本号';
