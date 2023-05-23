-- 创建本项目用的数据库（这个数据库才是我们的，也就是本项目的用户信息都要存储在这个数据库中）
CREATE DATABASE `petal-oauth2-auth-db`;

-- 使用数据库
USE `petal-oauth2-auth-db`;


/* 本项目的用户表 */
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL COMMENT '主键',
                            `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '用户名',
                            `nick_name` varchar(32) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
                            `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '密码',
                            `status` tinyint(1) DEFAULT '0' COMMENT '用户状态（0正常 1停用）',
                            `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
                            `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                            `sex` tinyint(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
                            `avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
                            `create_time` date DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '最后一次修改时间',
                            `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='本项目的用户表';


INSERT INTO `sys_user` VALUES (1001, 'root', '周杰伦', '$2a$10$HebtQPbLFf3YrO6B1n8Sb.AWHAz8SZtAc48IFGm8iSXjZsym3GPii', 0, '1550324080@qq.com', '12378900001', 0, 'https://img2.baidu.com/it/u=670341883,3643142939&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);
INSERT INTO `sys_user` VALUES (1002, 'test', '蔡徐坤', '$2a$10$HebtQPbLFf3YrO6B1n8Sb.AWHAz8SZtAc48IFGm8iSXjZsym3GPii', 0, '1550324080@qq.com', '18420163207', 0, 'https://img2.baidu.com/it/u=361550957,796293689&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);
INSERT INTO `sys_user` VALUES (1003, 'admin', '蔡徐666', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC', 0, '1550324080@qq.com', '18420163207', 0, 'https://img2.baidu.com/it/u=361550957,796293689&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);
-- sys_oauth2_client

DROP TABLE IF EXISTS `sys_oauth2_client`;
CREATE TABLE `sys_oauth2_client` (
                                            `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端ID',
                                            `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源列表',
                                            `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户端密钥',
                                            `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '域',
                                            `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '认证类型',
                                            `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '重定向地址',
                                            `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色列表',
                                            `access_token_validity` int DEFAULT NULL COMMENT 'token 有效期',
                                            `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期',
                                            `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '令牌扩展字段JSON',
                                            `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否自动放行',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
                                            `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人',
                                            PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='oauth2客户端表';


INSERT INTO `sys_oauth2_client` VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth2_client` VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth2_client` VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth2_client` VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth2_client` VALUES ('client', NULL, 'client', 'server', 'client_credentials', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);



