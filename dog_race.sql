/*
 Navicat Premium Data Transfer

 Source Server         : hzy@localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : dog_race

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 23/04/2023 15:48:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` mediumint(4) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `key` varchar(500) DEFAULT NULL COMMENT 'key',
  `value` varchar(500) DEFAULT NULL COMMENT 'value',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_ad
-- ----------------------------
DROP TABLE IF EXISTS `config_ad`;
CREATE TABLE `config_ad` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ad_url` varchar(255) DEFAULT NULL COMMENT '广告地址',
  `jump_url` varchar(255) DEFAULT NULL COMMENT '点击广告跳转地址',
  `award_name` varchar(255) DEFAULT NULL COMMENT '奖励名称',
  `img_name` varchar(255) DEFAULT NULL COMMENT '奖励图片名称',
  `award_type` tinyint(1) DEFAULT NULL COMMENT '奖励类型【1:代币/AGS  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】',
  `award_num` int(11) DEFAULT NULL COMMENT '奖励数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_catch_equip
-- ----------------------------
DROP TABLE IF EXISTS `config_catch_equip`;
CREATE TABLE `config_catch_equip` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `grade` int(11) DEFAULT NULL COMMENT '捕捉装备等级',
  `equip_name` varchar(50) DEFAULT NULL COMMENT '捕捉装备名称',
  `equip_desc` varchar(50) DEFAULT NULL COMMENT '捕捉装备描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '捕捉装备图片名称',
  `u_num` int(11) DEFAULT NULL COMMENT '购买所需U数量',
  `durability` tinyint(1) NOT NULL DEFAULT '30' COMMENT '捕捉装备耐久度',
  `catch_type` tinyint(1) DEFAULT NULL COMMENT '捕捉类别（1：抛接  2：藏食  3：啃咬  4：发声）',
  `deed_type` tinyint(1) DEFAULT NULL COMMENT '作用类别',
  `extra_one` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有额外效果（0:否 1:是）',
  `extra_two` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有额外效果（0:否 1:是）',
  `is_gem` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否珍宝（0:否 1:是）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_deed
-- ----------------------------
DROP TABLE IF EXISTS `config_deed`;
CREATE TABLE `config_deed` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `deed_desc` varchar(100) DEFAULT NULL COMMENT '作用描述',
  `type` tinyint(1) DEFAULT NULL COMMENT '作用于类别（1：宠物（某品质/等级）  2：装备  3：饲料  4：宠物）',
  `grade` tinyint(1) DEFAULT NULL COMMENT '作用于类别的等级',
  `odds` decimal(10,2) DEFAULT NULL COMMENT '增加概率（百分数）',
  `dog_odds` decimal(10,2) DEFAULT NULL COMMENT '获取宠物的概率增百分比',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_dog
-- ----------------------------
DROP TABLE IF EXISTS `config_dog`;
CREATE TABLE `config_dog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dog_name` varchar(50) DEFAULT NULL COMMENT '狗狗名称',
  `dog_desc` varchar(50) DEFAULT NULL COMMENT '狗狗描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '狗狗图片名称',
  `animation_name` varchar(50) DEFAULT NULL COMMENT '狗狗动画名称',
  `dog_breed` varchar(50) DEFAULT NULL COMMENT '狗狗品种',
  `dog_grade` tinyint(1) DEFAULT NULL COMMENT '狗狗品质',
  `grow_up_num` tinyint(1) DEFAULT NULL COMMENT '狗狗成长次数',
  `initial_fighting_num` decimal(10,2) DEFAULT NULL COMMENT '狗狗初始战力值',
  `inborn_num` decimal(10,2) DEFAULT NULL COMMENT '狗狗天赋系数（百分数）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_effect
-- ----------------------------
DROP TABLE IF EXISTS `config_effect`;
CREATE TABLE `config_effect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `effect_desc` varchar(255) DEFAULT NULL COMMENT '额外描述',
  `fighting_num` int(11) DEFAULT NULL COMMENT '额外增加战力值数量',
  `muscle_num` int(11) DEFAULT NULL COMMENT '额外恢复体力数量',
  `gem_odds` decimal(10,4) DEFAULT NULL COMMENT '额外获取珍宝概率',
  `add_muscle_odds` decimal(10,4) DEFAULT NULL COMMENT '额外恢复体力百分比',
  `ratio` decimal(10,4) DEFAULT NULL COMMENT '额外效果获取概率',
  `type` tinyint(1) DEFAULT NULL COMMENT '额外获取类型（1:体力 2:珍宝 3:战力）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_fight_equip
-- ----------------------------
DROP TABLE IF EXISTS `config_fight_equip`;
CREATE TABLE `config_fight_equip` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `grade` int(11) DEFAULT NULL COMMENT '对战装备等级',
  `fight_name` varchar(50) DEFAULT NULL COMMENT '对战装备名称',
  `fight_desc` varchar(50) DEFAULT NULL COMMENT '对战装备描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '对战装备图片名字',
  `fight_type` tinyint(1) DEFAULT NULL COMMENT '对战装备类型（1:狗绳 2:狗铃 3:狗披风）',
  `is_gem` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否珍宝',
  `durability` tinyint(1) NOT NULL DEFAULT '30' COMMENT '装备耐久度',
  `fighting_addition` decimal(10,2) DEFAULT NULL COMMENT '战力加成百分比',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_forage
-- ----------------------------
DROP TABLE IF EXISTS `config_forage`;
CREATE TABLE `config_forage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `grade` int(11) DEFAULT NULL COMMENT '饲料等级',
  `forage_name` varchar(50) DEFAULT NULL COMMENT '饲料名称',
  `forage_desc` varchar(50) DEFAULT NULL COMMENT '饲料描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '饲料图片名字',
  `forage_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '饲料类型（1:普通饲料 2:珍宝饲料）',
  `is_ignore_talent` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否忽略天赋（0:否 1:是）',
  `fighting_num` decimal(10,2) DEFAULT NULL COMMENT '增加战力点数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map
-- ----------------------------
DROP TABLE IF EXISTS `config_map`;
CREATE TABLE `config_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `map_grade` int(11) DEFAULT NULL COMMENT '地图等级',
  `map_name` varchar(50) DEFAULT NULL COMMENT '地图名称',
  `img_name` varchar(50) DEFAULT NULL COMMENT '地图图片名字',
  `pet_ratio` decimal(10,2) DEFAULT NULL COMMENT '宠物出现比例',
  `equip_ratio` decimal(10,2) DEFAULT NULL COMMENT '装备出现比例',
  `gem_ratio` decimal(10,2) DEFAULT NULL COMMENT '珍宝出现比例',
  `forage_ratio` decimal(10,2) DEFAULT NULL COMMENT '饲料出现比例',
  `wild_ratio` decimal(10,2) DEFAULT NULL COMMENT '野生出现比例',
  `attribute_num` int(11) DEFAULT NULL COMMENT '宠物速度、心情、耐力、幸运总值',
  `use_ags` int(11) DEFAULT NULL COMMENT '消耗AGS',
  `use_brawn` tinyint(4) DEFAULT NULL COMMENT '消耗体力',
  `lock_level` tinyint(1) DEFAULT NULL COMMENT '解锁所需装备等级',
  `lock_money` int(11) DEFAULT NULL COMMENT '解锁装备消耗U数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_catch_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_catch_ratio`;
CREATE TABLE `config_map_catch_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `equip_grade_01` int(10) NOT NULL DEFAULT '0',
  `equip_grade_02` int(10) NOT NULL DEFAULT '0',
  `equip_grade_03` int(10) NOT NULL DEFAULT '0',
  `equip_grade_04` int(10) NOT NULL DEFAULT '0',
  `equip_grade_05` int(10) NOT NULL DEFAULT '0',
  `equip_grade_06` int(10) NOT NULL DEFAULT '0',
  `equip_grade_07` int(10) NOT NULL DEFAULT '0',
  `equip_grade_08` int(10) NOT NULL DEFAULT '0',
  `equip_grade_09` int(10) NOT NULL DEFAULT '0',
  `equip_grade_10` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_fight_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_fight_ratio`;
CREATE TABLE `config_map_fight_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `equip_grade_01` int(10) NOT NULL DEFAULT '0',
  `equip_grade_02` int(10) NOT NULL DEFAULT '0',
  `equip_grade_03` int(10) NOT NULL DEFAULT '0',
  `equip_grade_04` int(10) NOT NULL DEFAULT '0',
  `equip_grade_05` int(10) NOT NULL DEFAULT '0',
  `equip_grade_06` int(10) NOT NULL DEFAULT '0',
  `equip_grade_07` int(10) NOT NULL DEFAULT '0',
  `equip_grade_08` int(10) NOT NULL DEFAULT '0',
  `equip_grade_09` int(10) NOT NULL DEFAULT '0',
  `equip_grade_10` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_forage_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_forage_ratio`;
CREATE TABLE `config_map_forage_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `forage_grade_01` int(10) NOT NULL DEFAULT '0',
  `forage_grade_02` int(10) NOT NULL DEFAULT '0',
  `forage_grade_03` int(10) NOT NULL DEFAULT '0',
  `forage_grade_04` int(10) NOT NULL DEFAULT '0',
  `forage_grade_05` int(10) NOT NULL DEFAULT '0',
  `forage_grade_06` int(10) NOT NULL DEFAULT '0',
  `forage_grade_07` int(10) NOT NULL DEFAULT '0',
  `forage_grade_08` int(10) NOT NULL DEFAULT '0',
  `forage_grade_09` int(10) NOT NULL DEFAULT '0',
  `forage_grade_10` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_gem_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_gem_ratio`;
CREATE TABLE `config_map_gem_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `gem_forage` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '珍宝饲料产出概率',
  `gem_equip` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '珍宝装备产出概率',
  `gem_prop` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '珍宝道具产出概率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_pet_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_pet_ratio`;
CREATE TABLE `config_map_pet_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `pet_grade_01` int(10) DEFAULT NULL,
  `pet_grade_02` int(10) DEFAULT NULL,
  `pet_grade_03` int(10) DEFAULT NULL,
  `pet_grade_04` int(10) DEFAULT NULL,
  `pet_grade_05` int(10) DEFAULT NULL,
  `pet_grade_06` int(10) DEFAULT NULL,
  `pet_grade_07` int(10) DEFAULT NULL,
  `pet_grade_08` int(10) DEFAULT NULL,
  `pet_grade_09` int(10) DEFAULT NULL,
  `pet_grade_10` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_map_wild_ratio
-- ----------------------------
DROP TABLE IF EXISTS `config_map_wild_ratio`;
CREATE TABLE `config_map_wild_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `map_id` int(11) DEFAULT NULL COMMENT '地图ID',
  `wild` int(10) NOT NULL DEFAULT '0' COMMENT '野生产出概率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_prop
-- ----------------------------
DROP TABLE IF EXISTS `config_prop`;
CREATE TABLE `config_prop` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `grade` int(11) DEFAULT NULL COMMENT '道具等级',
  `prop_name` varchar(50) DEFAULT NULL COMMENT '道具名称',
  `prop_desc` varchar(50) DEFAULT NULL COMMENT '道具描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '道具图片',
  `prop_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '道具类型（1:普通道具 2:珍宝道具）',
  `use_type` tinyint(1) DEFAULT NULL COMMENT '使用场景类型（1:战斗场景 2:其他场景）',
  `attribute_type` tinyint(1) DEFAULT NULL COMMENT '道具属性类型（1:强制增加宠物2点成长  2:参赛时免扣5点体力  3:参赛时免交报名费  4:参赛时临时增加战斗力  5:参赛结束后减少宠物冷却时间  6:随机开出1～5级捕捉装备  7:随机开出1～5级品质的狗狗）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config_wild
-- ----------------------------
DROP TABLE IF EXISTS `config_wild`;
CREATE TABLE `config_wild` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `grade` int(11) DEFAULT NULL COMMENT '野生等级',
  `wild_name` varchar(50) DEFAULT NULL COMMENT '野生名称',
  `wild_desc` varchar(50) DEFAULT NULL COMMENT '野生描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '野生图片名称',
  `fighting_num` decimal(10,2) DEFAULT NULL COMMENT '增加战力点数',
  `durability` tinyint(1) DEFAULT NULL COMMENT '耐久度',
  `is_fight` tinyint(1) DEFAULT NULL COMMENT '是否可战斗使用（0:否 1:是）',
  `wild_type` tinyint(1) DEFAULT NULL COMMENT '野生类型（1:破绳子 2:破鞋子 3:破抹布 4:其他）',
  `is_ignore_talent` tinyint(1) DEFAULT NULL COMMENT '是否忽略天赋（0:否 1:是）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for log_ad
-- ----------------------------
DROP TABLE IF EXISTS `log_ad`;
CREATE TABLE `log_ad` (
  `ad_id` int(11) NOT NULL COMMENT '广告id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '观看用户Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ad_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for log_asg
-- ----------------------------
DROP TABLE IF EXISTS `log_asg`;
CREATE TABLE `log_asg` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户userId',
  `num` decimal(10,2) DEFAULT NULL COMMENT '交易数量',
  `log_type` tinyint(1) DEFAULT NULL COMMENT '记录类型【1：充值、2：提现、3：捕捉消耗、4：参赛消耗 5：签到获取 6：参赛获取 7：赞助获取 8：看广告获得】',
  `asg_type` tinyint(1) DEFAULT NULL COMMENT 'ASG类型【1：消耗 2：获得】',
  `log_type_txt` varchar(255) DEFAULT NULL COMMENT '记录类型文字描述',
  `ags_type_txt` varchar(255) DEFAULT NULL COMMENT 'ASG类型文字描述',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `address` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17837 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for log_award
-- ----------------------------
DROP TABLE IF EXISTS `log_award`;
CREATE TABLE `log_award` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '中奖人userId',
  `stake_type` tinyint(1) DEFAULT NULL COMMENT '押注类型 1:单压 2:名次 3:包围',
  `select_type` tinyint(1) DEFAULT NULL COMMENT '选择类型 独赢：0 前二：2 前三：3 前四：4',
  `game_num` varchar(50) DEFAULT NULL COMMENT '中奖局号',
  `award_desc` varchar(50) DEFAULT NULL COMMENT '中奖描述',
  `dog_num` varchar(50) DEFAULT NULL COMMENT '压的几号狗狗',
  `track_num` varchar(50) DEFAULT NULL COMMENT '压的几号赛道（和 压的几号狗狗 同理 只不过服务端计算需要使用）',
  `pour_num` int(11) DEFAULT NULL COMMENT '下注数量',
  `odds` decimal(10,2) DEFAULT NULL COMMENT '赔率',
  `money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '中奖金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for log_game_result
-- ----------------------------
DROP TABLE IF EXISTS `log_game_result`;
CREATE TABLE `log_game_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '参赛用户userId',
  `game_num` varchar(50) DEFAULT NULL COMMENT '每局局号',
  `dog_number` tinyint(1) DEFAULT NULL COMMENT '狗狗本场比赛所在赛道 即赞助号',
  `ranking` tinyint(1) DEFAULT NULL COMMENT '比赛名次',
  `is_real` tinyint(1) DEFAULT NULL COMMENT '是否真实用户（0:人机  1:真人）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for log_pay
-- ----------------------------
DROP TABLE IF EXISTS `log_pay`;
CREATE TABLE `log_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户userId',
  `money` decimal(10,2) DEFAULT NULL COMMENT '消费金额',
  `type` tinyint(1) DEFAULT NULL COMMENT '支付类型【1：购买30天签到、2：购买精力、3：购买捕捉装备】',
  `currency_type` tinyint(1) DEFAULT NULL COMMENT '货币类型【1：ASG 2：USDT】',
  `type_txt` varchar(255) DEFAULT NULL COMMENT '支付文字描述',
  `currency_txt` varchar(255) DEFAULT NULL COMMENT '货币文字描述',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_mail
-- ----------------------------
DROP TABLE IF EXISTS `oil_mail`;
CREATE TABLE `oil_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `mail_title` varchar(50) DEFAULT NULL COMMENT '邮件标题',
  `mail_content` varchar(1000) DEFAULT NULL COMMENT '邮件内容',
  `award_num` int(11) DEFAULT NULL COMMENT '签到奖励数量',
  `img_name` varchar(255) DEFAULT NULL COMMENT '图片名字',
  `is_attribute` tinyint(1) DEFAULT NULL COMMENT '是否道具/饲料/装备（0:否  1:是）',
  `award_type` tinyint(1) NOT NULL COMMENT '签到奖励类型（1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备）',
  `kind_id` int(11) DEFAULT NULL COMMENT '种类ID（例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID）',
  `is_receive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已领取（0:否 1:是）',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读（0:否 1:是）',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户UserId',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '邮件类型 1：普通邮件 2：结算邮件',
  `game_award` varchar(255) DEFAULT NULL COMMENT '比赛奖励',
  `brawn_num` tinyint(1) DEFAULT NULL COMMENT '精力数量（参赛奖励用的）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2415 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_nft
-- ----------------------------
DROP TABLE IF EXISTS `oil_nft`;
CREATE TABLE `oil_nft` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户userId',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `name` varchar(255) DEFAULT NULL COMMENT 'NFT的名称',
  `description` varchar(255) DEFAULT NULL COMMENT 'NFT的描述',
  `image` varchar(255) DEFAULT NULL COMMENT 'NFT的远程图片地址',
  `json_url` varchar(255) DEFAULT NULL COMMENT 'NFT的json文件所在位置',
  `attributes` text COMMENT 'NFT的属性，已数组形式赋值，每个属性有两个参数（trait_type属性名称，value属性数值）',
  `type` tinyint(1) DEFAULT NULL COMMENT '铸造NFT 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备 7：珍宝】',
  `gem_type` tinyint(1) DEFAULT NULL COMMENT '珍宝类型 【1：饲料 2：捕捉 3：战斗】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提取到基地【0:否1；是】',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除【0:否 1:是】',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_rake_back
-- ----------------------------
DROP TABLE IF EXISTS `oil_rake_back`;
CREATE TABLE `oil_rake_back` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户userId',
  `num` decimal(10,2) DEFAULT NULL COMMENT '返佣数量',
  `coin_code` varchar(10) DEFAULT NULL COMMENT '币种（asg,usdt），默认asg',
  `order_sn` varchar(30) DEFAULT NULL COMMENT '订单号',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `address` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=374 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `oil_sign_in`;
CREATE TABLE `oil_sign_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `day` int(11) DEFAULT NULL COMMENT '第几天签到',
  `content` varchar(50) DEFAULT NULL COMMENT '签到领取内容',
  `img_name` varchar(50) DEFAULT NULL COMMENT '签到奖励图片',
  `award_num` int(11) DEFAULT NULL COMMENT '签到奖励数量',
  `is_attribute` tinyint(1) DEFAULT NULL COMMENT '是否道具/饲料/装备（0:否  1:是）',
  `award_type` tinyint(1) DEFAULT NULL COMMENT '签到奖励类型（1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备）',
  `kind_id` int(11) DEFAULT NULL COMMENT '种类ID（例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_support_record
-- ----------------------------
DROP TABLE IF EXISTS `oil_support_record`;
CREATE TABLE `oil_support_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `game_num` varchar(50) DEFAULT NULL COMMENT '每局局号',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户userId',
  `stake_type` tinyint(1) DEFAULT NULL COMMENT '押注类型 1:单压 2:名次 3:包围',
  `select_type` tinyint(1) DEFAULT NULL COMMENT '选择类型 独赢：0 前二：2 前三：3 前四：4',
  `dog_num` varchar(20) DEFAULT NULL COMMENT '压的几号狗狗',
  `track_num` varchar(20) DEFAULT NULL COMMENT '压的几号赛道（和 压的几号狗狗 同理 只不过服务端计算需要使用）',
  `pour_num` int(11) DEFAULT NULL COMMENT '下注数量',
  `odds` decimal(10,2) DEFAULT NULL COMMENT '赔率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_token
-- ----------------------------
DROP TABLE IF EXISTS `oil_token`;
CREATE TABLE `oil_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user
-- ----------------------------
DROP TABLE IF EXISTS `oil_user`;
CREATE TABLE `oil_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `dog_coin` int(11) NOT NULL DEFAULT '0' COMMENT '狗币',
  `total_muscle_num` int(11) DEFAULT NULL COMMENT '总体力值',
  `residue_muscle_num` int(11) DEFAULT NULL COMMENT '剩余体力值',
  `days_use_brawn` int(11) NOT NULL DEFAULT '0' COMMENT '当天消耗体力点数',
  `buy_brawn_num` int(11) NOT NULL DEFAULT '3' COMMENT '当天剩余购买体力次数',
  `is_music` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启音乐',
  `is_effect` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启音效',
  `is_sign_in` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已购买签到（0:否 1:是）',
  `is_today_check` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已完成每日签到（0:否 1:是）',
  `is_have_unread` tinyint(1) NOT NULL DEFAULT '2' COMMENT '是否有未读邮件',
  `sign_in_day_num` int(11) NOT NULL DEFAULT '0' COMMENT '签到天数',
  `buy_day_num` int(11) NOT NULL DEFAULT '0' COMMENT '距离购买签到已过天数',
  `map_id` tinyint(1) NOT NULL DEFAULT '1' COMMENT '当前所在地图Id',
  `open_map_num` tinyint(1) NOT NULL DEFAULT '1' COMMENT '开启地图数量',
  `language` varchar(20) NOT NULL DEFAULT 'zh' COMMENT '游戏内语言',
  `is_free_name_edit` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否免费编辑名字（0:否 1:是）',
  `equip_hide_grade` tinyint(1) NOT NULL DEFAULT '0' COMMENT '藏食装备等级',
  `equip_gnaw_grade` tinyint(1) NOT NULL DEFAULT '0' COMMENT '啃咬装备等级',
  `equip_sound_grade` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发声装备等级',
  `win_num` int(11) NOT NULL DEFAULT '0' COMMENT '赢的次数',
  `logon_credentials` varchar(255) DEFAULT NULL COMMENT '钱包登录凭证',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `register_ip` varchar(15) DEFAULT NULL COMMENT '注册ip',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(15) DEFAULT NULL COMMENT '最后登录ip',
  `next_buy_equip_time` datetime DEFAULT NULL COMMENT '下一次购买装备时间',
  `last_resume_time` datetime DEFAULT NULL COMMENT '上一次恢复精力时间',
  `open_id` varchar(255) DEFAULT NULL COMMENT '基地openid',
  `address` varchar(255) DEFAULT NULL COMMENT '基地address',
  `role` int(11) DEFAULT NULL COMMENT '基地role',
  `role_nft` int(11) DEFAULT NULL COMMENT '基地role_nft',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10182 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_catch_equip
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_catch_equip`;
CREATE TABLE `oil_user_catch_equip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `equip_id` int(11) NOT NULL COMMENT '捕捉装备ID',
  `grade` int(11) DEFAULT NULL COMMENT '捕捉装备等级',
  `equip_name` varchar(50) DEFAULT NULL COMMENT '捕捉装备名称',
  `equip_desc` varchar(50) DEFAULT NULL COMMENT '捕捉装备描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '捕捉装备图片名称',
  `durability_max` tinyint(1) DEFAULT NULL COMMENT '装备最大耐久度',
  `durability_residue` tinyint(1) DEFAULT NULL COMMENT '装备剩余耐久度',
  `catch_type` tinyint(1) DEFAULT NULL COMMENT '捕捉类别（1：抛接  2：藏食  3：啃咬  4：发声）',
  `deed_type` tinyint(1) DEFAULT NULL COMMENT '作用类别',
  `extra_one` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有额外效果（0:否 1:是）',
  `extra_two` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有额外效果（0:否 1:是）',
  `is_gem` tinyint(1) DEFAULT NULL COMMENT '是否珍宝(0:否 1:是)',
  `is_use` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否使用中（0:否 1:是）',
  `equip_num` tinyint(1) DEFAULT NULL COMMENT '装备数量',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:是）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1837 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_dog
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_dog`;
CREATE TABLE `oil_user_dog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `dog_id` int(11) DEFAULT NULL COMMENT '狗狗ID',
  `dog_grade` int(11) DEFAULT NULL COMMENT '狗狗品质',
  `dog_name` varchar(50) DEFAULT NULL COMMENT '狗狗名称',
  `dog_desc` varchar(50) DEFAULT NULL COMMENT '狗狗描述',
  `img_name` varchar(255) DEFAULT NULL COMMENT '狗狗图片名称',
  `animation_name` varchar(255) DEFAULT NULL COMMENT '狗狗动画名称',
  `dog_breed` varchar(255) DEFAULT NULL COMMENT '狗狗品种',
  `fighting_num` decimal(10,2) DEFAULT NULL COMMENT '狗狗战力值',
  `inborn_num` decimal(10,2) DEFAULT NULL COMMENT '狗狗天赋系数',
  `max_grow_up_num` tinyint(1) DEFAULT NULL COMMENT '狗狗最大成长次数',
  `current_grow_up_num` tinyint(1) NOT NULL DEFAULT '0' COMMENT '狗狗当前成长次数',
  `speed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '狗狗速度',
  `mood` tinyint(1) NOT NULL DEFAULT '0' COMMENT '狗狗心情',
  `endurance` tinyint(1) NOT NULL DEFAULT '0' COMMENT '狗狗耐力',
  `luck` tinyint(1) NOT NULL COMMENT '狗狗幸运值',
  `is_use` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否使用中（0:否 1:是）',
  `is_game` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否参赛中（0:否 1:是）',
  `is_cool` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冷却中（0:否 1:是）',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:shi）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `cool_time` datetime DEFAULT NULL COMMENT '开始冷却时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1914 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_fight_equip
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_fight_equip`;
CREATE TABLE `oil_user_fight_equip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `fight_id` int(11) NOT NULL COMMENT '对战装备ID',
  `grade` int(11) DEFAULT NULL COMMENT '对战装备等级',
  `fight_name` varchar(50) DEFAULT NULL COMMENT '对战装备名称',
  `fight_desc` varchar(50) DEFAULT NULL COMMENT '对战装备描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '对战装备图片名称',
  `fighting_addition` decimal(10,2) DEFAULT NULL COMMENT '战力加成百分比',
  `is_gem` tinyint(1) DEFAULT NULL COMMENT '是否珍宝(0:否 1:是)',
  `fight_type` tinyint(1) DEFAULT NULL COMMENT '对战装备类型（1:狗绳 2:狗铃 3:狗披风）',
  `fight_num` tinyint(1) DEFAULT NULL COMMENT '装备数量',
  `durability_max` tinyint(1) DEFAULT NULL COMMENT '最大耐久度',
  `durability_residue` tinyint(1) DEFAULT NULL COMMENT '剩余耐久度',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:shi）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_forage
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_forage`;
CREATE TABLE `oil_user_forage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `forage_id` int(11) NOT NULL COMMENT '饲料ID',
  `grade` int(11) DEFAULT NULL COMMENT '饲料等级',
  `forage_name` varchar(50) DEFAULT NULL COMMENT '饲料名称',
  `forage_desc` varchar(50) DEFAULT NULL COMMENT '饲料描述',
  `img_name` varchar(50) DEFAULT NULL COMMENT '饲料图片名称',
  `fighting_num` decimal(10,2) DEFAULT NULL COMMENT '增加战力点数',
  `forage_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '饲料类型（1:普通饲料 2:珍宝饲料）',
  `is_ignore_talent` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否忽略天赋（0:否 1:是）',
  `forage_num` tinyint(1) DEFAULT NULL COMMENT '饲料数量',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:shi）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=770 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_prop
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_prop`;
CREATE TABLE `oil_user_prop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `prop_id` int(11) NOT NULL COMMENT '道具ID',
  `grade` int(11) DEFAULT NULL COMMENT '道具等级',
  `prop_name` varchar(50) DEFAULT NULL COMMENT '道具名称',
  `img_name` varchar(50) DEFAULT NULL COMMENT '道具图片',
  `prop_desc` varchar(50) DEFAULT NULL COMMENT '道具描述',
  `prop_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '道具类型（1:普通道具 2:珍宝道具）',
  `use_type` tinyint(1) DEFAULT NULL COMMENT '使用场景类型（1:战斗场景 2:其他场景）',
  `attribute_type` tinyint(1) DEFAULT NULL COMMENT '道具属性类型（1:强制增加宠物2点成长  2:参赛时免扣5点体力  3:参赛时免交报名费  4:参赛时临时增加战斗力  5:参赛结束后减少宠物冷却时间  6:随机开出1～5级捕捉装备  7:随机开出1～5级品质的狗狗）',
  `prop_num` tinyint(1) DEFAULT NULL COMMENT '道具数量',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:shi）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oil_user_wild
-- ----------------------------
DROP TABLE IF EXISTS `oil_user_wild`;
CREATE TABLE `oil_user_wild` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `wild_id` int(11) NOT NULL COMMENT '野生ID',
  `grade` int(11) DEFAULT NULL COMMENT '野生等级',
  `wild_name` varchar(50) DEFAULT NULL COMMENT '野生名称',
  `wild_desc` varchar(50) DEFAULT NULL COMMENT '野生描述',
  `img_name` varchar(255) DEFAULT NULL COMMENT '野生图片名称',
  `fighting_num` decimal(10,2) DEFAULT NULL COMMENT '增加战力点数',
  `durability_max` tinyint(1) DEFAULT NULL COMMENT '装备最大耐久度',
  `durability_residue` tinyint(1) DEFAULT NULL COMMENT '装备剩余耐久度',
  `is_fight` tinyint(1) DEFAULT NULL COMMENT '是否可战斗使用（0:否 1:是）',
  `wild_num` tinyint(1) DEFAULT NULL COMMENT '野生数量',
  `wild_type` tinyint(1) DEFAULT NULL COMMENT '野生类型（1:破绳子 2:破鞋子 3:破抹布 4:其他）',
  `is_ignore_talent` tinyint(1) DEFAULT NULL COMMENT '是否忽略天赋（0:否 1:是）',
  `is_nft` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否NFT（0:否 1:是）',
  `is_draw` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已提取到基地【0:否 1:是】',
  `is_freeze` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否冻结【0否 1:是】',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的ID，唯一序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4218 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token` (
  `user_id` bigint(20) NOT NULL COMMENT '主键',
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `admin_id` mediumint(8) NOT NULL COMMENT '管理员ID',
  `admin_name` varchar(10) DEFAULT NULL COMMENT '管理员名字',
  `account_number` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `mobile` varchar(15) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态  0：禁用   1：正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(15) DEFAULT NULL COMMENT '最后登录ip',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
