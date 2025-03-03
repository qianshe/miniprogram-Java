CREATE TABLE `funeral_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '流程标题',
  `description` text COMMENT '流程描述',
  `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '流程序号',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_num` (`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='白事流程表';
