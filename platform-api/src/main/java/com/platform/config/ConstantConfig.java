package com.platform.config;

import com.platform.entity.UserDogVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @program: platform
 * @description: 程序常量配置
 * @author: Yuan
 * @create: 2020-09-11 16:13
 **/
public class ConstantConfig {

    /**
     * 基地给的对应app的 appId
     */
    public static final String appId = "9b6xycH0";

    /**
     * 基地给的对应app的 appSecret
     */
    public static final String appSecret = "chdFNJ7GtWHsleZQYDvi52bU09om3RT8";

    /**
     * 基地给的对应app的 key
     */
    public static final String key = "bW3Ftjiqr7wZxYlT";

    /**
     * 与基地交互token 生成方式：appId + ':' + appSecret base64加密获得
     */
    public static String authorization = "";

    /**
     * NFT json文件 base url
     */
    public static final String nftJsonBaseUrl = "https://3300.pamperdog.club";

    /**
     * NFT 图片 base url
     */
    public static final String nftImageBaseUrl = "https://3300.pamperdog.club/";

    /**
     * 比赛开始时间
     */
    public static String gameBeginTime = "14:00:00";

    /**
     * 比赛结束时间
     */
    public static String gameEndTime = "18:00:00";

    /**
     * 匹配完成队列
     */
    public final static Queue<List<UserDogVo>> matcherQueue = new LinkedList<>();

    /**
     * 对局号队列
     */
    public final static Queue<String> gameNumQueue = new LinkedList<>();

    /**
     * 报名列表
     */
    public final static List<UserDogVo> enrollList = new ArrayList<>();

    /**
     * 匹配列表
     */
    public static List<UserDogVo> matchList = new ArrayList<>();

    /**
     * 匹配失败列表
     */
    public static List<UserDogVo> failList = new ArrayList<>();

    /**
     * 赛中狗狗列表
     */
    public static List<UserDogVo> currentList = new ArrayList<>();

    /**
     * 比赛结果狗狗列表
     */
    public static List<UserDogVo> resultList = new ArrayList<>();

    /**
     * 匹配总秒数
     */
    public static Integer matchSecondsTotal = 60;

    /**
     * 根据战力浮动20%，第一次匹配秒数
     */
    public static Integer matchSecondsFirst = 5;

    /**
     * 当前匹配秒数
     */
    public static Integer matchSecondsCurrent = 0;

    /**
     * 当前游戏状态 0: 未匹配  1: 匹配中20秒  2: 匹配成功  3: 匹配失败  4: 等待中  5: 入场中5秒  6: 押注中120秒  7: 调整士气中20秒  8: 比赛中30秒  9: 公布比赛结果5秒
     */
    public static Integer gameStatus = 0;

    /**
     * 世界状态 0：有新的比赛 1：没有比赛 2：押注中 3：士气调整中 4：比赛中 5：结算中
     */
    public static Integer worldStatus = 1;

    /**
     * 是否有比赛在进行
     */
    public static Boolean isHavePlay = false;

    /**
     * 当前游戏某状态倒计时总秒数
     */
    public static Integer currentSecond = 0;

    /**
     * 当前局号
     */
    public static String currentGameNum = "";

    /**
     * 上一局的狗狗
     */
    public static List<UserDogVo> beforeList = new ArrayList<>();

    /**
     * 当前奖金池总金额
     */
    public static BigDecimal jackpot = BigDecimal.ZERO;

    /**
     * 本场比赛所有玩家 下注总金额  单位：ASG
     */
    public static Integer currentBetTotal = 0;

    /**
     * 本场比赛所有玩家 押注赢的总金额  单位：ASG
     */
    public static BigDecimal currentWinTotal = BigDecimal.ZERO;

    /**
     * 本场比赛所有参赛玩家 赢得的场外奖金池总金额 单位：ASG
     */
    public static BigDecimal joinWinTotal = BigDecimal.ZERO;

    /**
     * 平台分红
     */
    public static BigDecimal shareOutBonus = BigDecimal.ZERO;

    /**
     * 狗狗1.0 json文件下载地址
     */
    public static String dog01JsonPath = "/www/wwwroot/dog_race/static/json_01/"; // 服务器: /www/wwwroot/dog_race/static/json_01/ 测试: /Users/huzhiyuan/Desktop/

    /**
     * 基本请求地址
     */
    public static String baseUrl = "https://v2-openapi.asg.games";

    /**
     * 登录请求地址
     */
    public static String authUrl = baseUrl + "/v1/sns/auth/create";

    /**
     * 获取代币总数
     */
    public static String coinTotal = baseUrl + "/v1/sns/coin/total";

    /**
     * 增加代币(从游戏提现代币到基地)
     */
    public static String addCoin = baseUrl + "/v1/sns/coin/inc";

    /**
     * 减少代币(从基地扣除代币)
     */
    public static String decCoin = baseUrl + "/v1/sns/coin/dec";

    /**
     * 创建nft
     */
    public static String createNFT = baseUrl + "/v1/sns/nft/create";

    /**
     * nft详情
     */
    public static String infoNFT = baseUrl + "/v1/sns/nft/info";

    /**
     * nft列表
     */
    public static String listNFT = baseUrl + "/v1/sns/nft/list";

    /**
     * 从游戏提取NFT到基地
     */
    public static String outNFT = baseUrl + "/v1/sns/nft/out";

    /**
     * 冻结NFT
     */
    public static String freezeNFT = baseUrl + "/v1/sns/nft/freeze";

    /**
     * 解冻NFT
     */
    public static String thawNFT = baseUrl + "/v1/sns/nft/thaw";

    /**
     * 销毁nft
     */
    public static String destroyNFT = baseUrl + "/v1/sns/nft/destroy";

    /**
     * 每日返佣
     */
    public static String addLog = baseUrl + "/v1/sns/coin/addLog";
}
