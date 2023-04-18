package com.platform.scheduled;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.CollectorsUtils;
import com.platform.util.CommonUtil;
import com.platform.util.MD5Util;
import com.platform.util.NameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.platform.config.ConstantConfig.*;
import static com.platform.util.CommonUtil.getToday;
import static com.platform.util.CommonUtil.getYesterday;

/**
 * @program: platform
 * @description: 定时任务
 * @author: Yuan
 * @create: 2020-09-12 15:28
 **/
@Component
@Scope("singleton")
public class ScheduledTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiConfigDogService configDogService;
    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiMailService mailService;
    @Autowired
    private ApiLogAdService logAdService;
    @Autowired
    private ApiLogGameResultService logGameResultService;
    @Autowired
    private ApiSupportRecordService supportRecordService;
    @Autowired
    private ApiLogAwardService logAwardService;
    @Autowired
    private ApiConfigService configService;
    @Autowired
    private ApiLogASGService asgService;
    @Autowired
    private ApiLogRakeBackService logRakeBackService;

    private Integer tempId = 0;

    /**
     * 每隔1秒扫描一下报名狗狗参赛池
     */
    @Scheduled(cron = "0/1 * * * * ?")
    private void getToken(){

        if(enrollList.size() <= 0 && matchList.size() <= 0) return;
        matchingDog();
        matchSecondsCurrent++;
    }

    /**
     * 倒计时
     */
    @Scheduled(cron = "0/1 * * * * ?")
    private void countDown(){

        if (currentSecond > 0) currentSecond--;
    }

    /**
     * 查询 匹配完成队列是否有正在等待队列 、 是否有队列在比赛中
     */
    @Scheduled(cron = "0/1 * * * * ?")
    private void queryQueue(){

        if(matcherQueue.size() <= 0) return;
        if (isHavePlay) return;
        gameStatus = 5;
        singleThreadExecutor();
    }

    /**
     * 每隔四十八分钟恢复一点体力
     * @throws ParseException
     */
    @Scheduled(cron = "0 */48 * * * ?")
    private void resumeBrawn() throws ParseException {
        userService.resumeBrawn();
    }

    /***
     * @Description: 每天凌晨一点签到自动重置
     * @Param: []
     * @return: void
     * @Author: Yuan
     * @Date: 2020/6/1
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateAccessToken() {

        userService.resettingSignIn();
        logAdService.deleteAllLog();
    }

    /**
     * 改变狗狗冷却状态
     * @throws ParseException
     */
    @Scheduled(cron = "0 */10 * * * ?")
    private void changeCoolStatus() throws ParseException {

        List<UserDogVo> userDogList = userDogService.queryListByCool(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String startTime = dateFormat.format(now);
        Date sd1 = dateFormat.parse(startTime); //当前时间

        for (int i = 0; i < userDogList.size(); i++){
            if (sd1.compareTo(userDogList.get(i).getCoolTime()) > 0){ // 当前时间 大于 冷却结束时间
                userDogService.updateCoolStatus(0, userDogList.get(i).getId());
            }
        }
    }

    /***
     * @Description: 每天凌晨两点 计算昨日返佣比例
     * @Param: []
     * @return: void
     * @Author: Yuan
     * @Date: 2020/6/1
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void countRebate() {
        rebateLog();
    }

    /**
     * 测试
     * @throws ParseException
     */
    @Scheduled(cron = "0 */8 * * * ?")
    public void test(){
//        rebateLog();
    }

    /***
     * @Description: 每隔一小时55分钟执行一次刷新
     * @Param: []
     * @return: void
     * @Author: Yuan
     * @Date: 2023/04/01
     */
//    @Scheduled(cron = "0 55 0/2 * * ?")
    public void updateJsApiTicket() {
    }

    /**
     * 每日计算昨天返佣 并告诉基地
     */
    public void rebateLog() {

        try {

            BigDecimal userAsg = null;
            List<LogASGVo> logASGVoList = null;
            List<UserVo> userList = userService.allUser();
            for (int i = 0; i < userList.size(); i++){
                userAsg = BigDecimal.ZERO;
                logASGVoList = asgService.queryObjectByType(userList.get(i).getUserId(), getYesterday());
                for (LogASGVo item : logASGVoList) {
                    userAsg = userAsg.add(item.getNum());
                }
                if (userAsg.compareTo(BigDecimal.ZERO) == 1){
                    LogRakeBackVo logRakeBackVo = new LogRakeBackVo();
                    logRakeBackVo.setCoinCode("asg");
                    logRakeBackVo.setNum(userAsg);
                    logRakeBackVo.setUserId(userList.get(i).getUserId());
                    logRakeBackVo.setAddress(userList.get(i).getAddress());
                    logRakeBackVo.setOrderSn(CommonUtil.generateOrderNumber());
                    logRakeBackVo.setCreateTime(new Date());
                    logRakeBackVo.setRemarks("狗狗每日返佣");
                    logRakeBackService.save(logRakeBackVo);
                }
            }

            // 向基地发送消息
            List<LogRakeBackVo> allLog = logRakeBackService.allLog(getToday(new Date()));
            for (LogRakeBackVo item : allLog){
                System.out.println(item);
                Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
                Map<String,Object> form = new HashMap<>();
                form.put("address", item.getAddress());
                form.put("nonce", nonce);
                form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                form.put("order_sn", item.getOrderSn());
                form.put("coin_code", item.getCoinCode());
                form.put("desc", item.getRemarks());
                form.put("is_rebate", 1);
                form.put("is_destroy", 0);
                String sign = MD5Util.encodeSign(form, key);
                form.put("sign", sign);
                form.put("create_time", item.getCreateTime());
                HttpResponse res = HttpRequest.post(addLog).form(form).header("Authorization", authorization).timeout(10000).execute();
                String body = res.body();
                System.out.println("返佣响应：" + body);
                JSONObject result = JSON.parseObject(body);
                if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 匹配狗狗
     * @return
     */
    public void matchingDog() {

        System.out.println("匹配狗狗中......");

        BigDecimal oneDogFightingMin = null;
        BigDecimal oneDogFightingMax = null;

        if(matchList.size() <= 0) {
            matchList.add(enrollList.get(0));
            enrollList.remove(0);
        }

        if (matchSecondsCurrent <= 5){
            oneDogFightingMin = matchList.get(0).getFightingNum().multiply(new BigDecimal(0.8));
            oneDogFightingMax = matchList.get(0).getFightingNum().multiply(new BigDecimal(1.2));
        } else {
            BigDecimal numMin = new BigDecimal(0.8).add(new BigDecimal(matchSecondsCurrent - 5).multiply(new BigDecimal(0.1)));
            BigDecimal numMax = new BigDecimal(0.8).add(new BigDecimal(matchSecondsCurrent - 5).multiply(new BigDecimal(0.1)));
            oneDogFightingMin = matchList.get(0).getFightingNum().multiply(numMin);
            oneDogFightingMax = matchList.get(0).getFightingNum().multiply(numMax);
        }

        for (int i = enrollList.size() - 1; i >= 0; i--){
            if (enrollList.get(i).getFightingNum().compareTo(matchList.get(0).getFightingNum()) == 0 || enrollList.get(i).getFightingNum().compareTo(oneDogFightingMin) == 1 || enrollList.get(i).getFightingNum().compareTo(oneDogFightingMax) == -1){
                matchList.add(enrollList.get(i));
                enrollList.remove(i);
                System.out.println("匹配到一只新的狗狗");
            }
        }

        // 匹配成功
        if (matchList.size() == 6){

            countRanking();

            // 组队完成 生成对战狗狗list 添加到队列
            synchronized (matcherQueue) {
                matcherQueue.offer(matchList);
                matcherQueue.notify();
            }

            // 组队完成 生成局号 添加到队列
            synchronized (gameNumQueue) {
                gameNumQueue.offer(CommonUtil.generateOrderNumber());
                gameNumQueue.notify();
            }

            System.out.println("匹配完毕,共 " + matchList.size() + "条 狗狗");
            matchSecondsCurrent = 0;
            matchList = new ArrayList<>();
            return;
        }

        // 匹配失败
        if (matchSecondsTotal <= matchSecondsCurrent && matchList.size() < 6){

            System.out.println("匹配失败 返还用户 体力 和 AGS");
            System.out.println(matchList);
            failList.addAll(matchList);
            matchSecondsCurrent = 0;

            // 返还报名费用到邮箱
            for (int i = 0; i < matchList.size(); i++){
                // 判断是否人机狗狗
                if (matchList.get(i).getId() > 0){

                    MailVo mailVo = new MailVo();
                    mailVo.setType(2);
                    mailVo.setMailTitle("匹配失败返还报名费");
                    mailVo.setMailContent("匹配失败返还报名费：" + 1000);
                    mailVo.setAwardType(1);
                    mailVo.setBrawnNum(5);
                    mailVo.setImgName("jinbi,jingli");
                    mailVo.setGameAward(1000 + "," + 5);
                    mailVo.setAwardNum(1000);
                    mailVo.setIsAttribute(0);
                    mailVo.setIsReceive(0);
                    mailVo.setUserId(matchList.get(i).getUserId());
                    mailVo.setCreateTime(new Date());
                    mailService.save(mailVo);

                    Map map = new HashMap();
                    map.put("userId", matchList.get(i).getUserId());
                    map.put("isHaveUnread", 1);
                    userService.update(map);

                    Map dogMap = new HashMap();
                    dogMap.put("dogId", matchList.get(i).getId());
                    dogMap.put("isCool", 0);
                    dogMap.put("isGame", 0);
                    userDogService.update(dogMap); // 更新狗狗 冷却/参战状态
                }
            }

            userDogService.updateBatchById(matchList);
            matchList = new ArrayList<>();
            List<UserDogVo> dogVoList = matcherQueue.peek();
            if (dogVoList == null) {
                worldStatus = 1;
            } else {
                worldStatus = 0;
            }
            gameStatus = 0;
            currentSecond = 0;
            isHavePlay = false;
            return;
        }

        // 匹配秒数大于匹配总秒数（暂定：60秒） 并且本场比赛狗狗数量小于6 匹配人机狗狗
//        if (matchSecondsCurrent >= matchSecondsTotal && matchList.size() < 6){
//
//            tempId = 0;
//            //创建随机数对象
//            Random r = new Random();
//            List<Integer> battle = null;
//
//            battle = new ArrayList<>();
//            //判断集合的长度是不是小于4
//            while (battle.size() < 6) {
//                //产生一个随机数，添加到集合
//                int number = r.nextInt((oneDogFightingMax.intValue() + 200)) % ((oneDogFightingMax.intValue() + 200) - 500 + 1) + 500;
//                battle.add(number);
//            }
//
//            int attributeNum = matchList.get(0).getSpeed() + matchList.get(0).getMood() + matchList.get(0).getEndurance() + matchList.get(0).getLuck();
//            for (int i = matchList.size(); i < 6; i++){
//                tempId --;
//                matchList.add(createDog(tempId, new BigDecimal(battle.get(i)), attributeNum, matchList.get(0).getDogGrade()));
//                System.out.println("进行人机匹配" + i);
//            }
//
//            countRanking();
//
//            // 组队完成 生成对战狗狗list 添加到队列
//            synchronized (matcherQueue) {
//                matcherQueue.offer(matchList);
//                matcherQueue.notify();
//            }
//
//            // 组队完成 生成局号 添加到队列
//            synchronized (gameNumQueue) {
//                gameNumQueue.offer(CommonUtil.generateOrderNumber());
//                gameNumQueue.notify();
//            }
//
//            System.out.println("人机匹配完毕,共 " + matchList.size() + "条 狗狗");
//            matchSecondsCurrent = 0;
//            matchList = new ArrayList<>();
//        }

    }

    /**
     * 创建随机狗狗
     * @param tempId 狗狗ID （人机是负数）
     * @param fightingNum 狗狗战力
     * @param attributeNum 速度、幸运、耐力、心情 总和
     * @param firstGrade 第一只狗狗的品质
     * @return
     */
    private UserDogVo createDog(Integer tempId, BigDecimal fightingNum, Integer attributeNum, Integer firstGrade){

        firstGrade = firstGrade + 2;
        Random r = new Random();
        int grade = r.nextInt(firstGrade) % (firstGrade - 1 + 1) + 1;

        ConfigDogVo configDogVo = configDogService.queryObjectByGrade(grade);

        UserDogVo userDogVo = new UserDogVo();
        userDogVo.setId(tempId);
        userDogVo.setUserId(tempId.longValue());
        userDogVo.setDogName(configDogVo.getDogName());
        userDogVo.setDogDesc(configDogVo.getDogDesc());
        userDogVo.setDogBreed(configDogVo.getDogBreed());
        userDogVo.setFightingNum(fightingNum);
        userDogVo.setImgName(configDogVo.getImgName());
        userDogVo.setAnimationName(configDogVo.getAnimationName());
        userDogVo.setDogGrade(configDogVo.getDogGrade());
        userDogVo.setMasterName(NameUtil.getRandomName());

        int minNum = 0;
        switch (attributeNum){
            case 6:
            case 8:
            case 10:
                minNum = 1;
                break;
            case 12:
            case 14:
            case 16:
                minNum = 2;
                break;
            case 18:
            case 20:
                minNum = 3;
                break;
            case 22:
                minNum = 4;
                break;
            case 24:
                minNum = 5;
                break;
            default:
                minNum = 1;
                break;
        }

        List<Integer> tempList = getWeight(4, attributeNum, minNum);

        userDogVo.setSpeed(tempList.get(0));
        userDogVo.setMood(tempList.get(1));
        userDogVo.setEndurance(tempList.get(2));
        userDogVo.setLuck(tempList.get(3));

        System.out.println("成功创建一只人机狗狗");
        return userDogVo;
    }

    /**
     * 根据本场比赛狗狗战斗力进行排名
     */
    private void countRanking(){

        // 按战力降序
        matchList.sort(Comparator.comparing(UserDogVo::getFightingNum).reversed());
        for (int i = 0; i < matchList.size(); i++){
            matchList.get(i).setRanking(i + 1);
        }
    }

    /**
     * 新建一条线程
     */
    public void singleThreadExecutor() {

        // 创建线程池
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        // 执行任务
        threadPool.execute(() -> {
            try {
                isHavePlay = true;
                gameStatus = 5;
                currentSecond = 5;
                if (worldStatus == 1 || worldStatus == 0) {
                    currentList = matcherQueue.poll();
                    currentGameNum = gameNumQueue.poll();
                    Collections.shuffle(currentList);
                    System.out.println("匹配出6只狗狗: " + currentList);
                }
                TimeUnit.SECONDS.sleep(5);
                currentBetTotal = 0;
                currentWinTotal = BigDecimal.ZERO;
                joinWinTotal = BigDecimal.ZERO;
                gameStatus = 6;
                worldStatus = 2;
                currentSecond = 120;
                TimeUnit.SECONDS.sleep(120);
                gameStatus = 7;
                worldStatus = 3;
                currentSecond = 20;
                BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));
                countDogRanking(sum);
                TimeUnit.SECONDS.sleep(20);
                gameStatus = 8;
                worldStatus = 4;
                currentSecond = 30;
                TimeUnit.SECONDS.sleep(30);
                gameStatus = 9;
                worldStatus = 5;
                currentSecond = 5;
                noteGameResult(); // 结算 记录比赛结果数据
                settleAccounts(); // 结算赞助奖励
                sendMail(); // 结算 发送邮件
                TimeUnit.SECONDS.sleep(5);
                userDogService.updateBatchById(currentList);
                currentList = new ArrayList<>();
                List<UserDogVo> dogVoList = matcherQueue.peek();
                if (dogVoList == null) {
                    worldStatus = 1;
                } else {
                    worldStatus = 0;
                }
                gameStatus = 0;
                currentSecond = 0;
                isHavePlay = false;
            } catch (InterruptedException e) {

            }
        });
    }

    /**
     * 记录比赛结果数据
     */
    private void noteGameResult(){

        try {
            for (int i = 0; i < currentList.size(); i++){

                LogGameResultVo logGameResultVo = new LogGameResultVo();
                logGameResultVo.setUserId(currentList.get(i).getUserId());
                logGameResultVo.setDogNumber(i + 1);
                logGameResultVo.setRanking(currentList.get(i).getRanking());
                logGameResultVo.setIsReal(currentList.get(i).getId() > 0 ? 1 : 0);
                logGameResultVo.setGameNum(currentGameNum);
                logGameResultVo.setCreateTime(new Date());

                logGameResultService.save(logGameResultVo);

                if (currentList.get(i).getId() > 0 && currentList.get(i).getRanking() == 1){

                    Map map = new HashMap();
                    map.put("userId", currentList.get(i).getUserId());
                    map.put("winNum", 1);
                    userService.countGameWinNum(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 比赛结束 系统返还 精力、AGS
     */
    public void sendMail(){

        try {
            // 查询比赛结果 并且按 名次排列赛道号顺序
            List<LogGameResultVo> gameResultList = logGameResultService.queryListByGameNum(currentGameNum);
            gameResultList.sort(Comparator.comparing(LogGameResultVo::getRanking));
            String dogNumStr = "";
            for (int i = 0; i < gameResultList.size(); i++){
                if (gameResultList.size() - 1 == i) {
                    dogNumStr += gameResultList.get(i).getDogNumber();
                    break;
                }
                dogNumStr += gameResultList.get(i).getDogNumber() + ">";
            }

//            List<List<LogAwardVo>> userLog = new ArrayList<>();
//            List<LogAwardVo> logList = null;
//            List<LogAwardVo> logAwardList = logAwardService.queryListByGameNum(currentGameNum);
//            for (int i = 0; i < currentList.size(); i++){
//                logList = new ArrayList<>();
//                for (int j = 0; j < logAwardList.size(); j++){
//                    if (logAwardList.get(j).getUserId().equals(currentList.get(i).getUserId())){
//                        logList.add(logAwardList.get(j));
//                    }
//                }
//                userLog.add(logList);
//            }


//            String showStr = "";
//            String mailStr01 = "";
//            String mailStr02 = "";
//            String mailStr03 = "";
//            String kong01 = "\n-------------战况摘要------------\n\n";
//            String kong02 = "\n-------------赞助记录------------\n\n";
//            String duStr = "";
//            String mingStr = "";
//            String baoStr = "";
//            Long userId = null;
//            BigDecimal sum = BigDecimal.ZERO;
//            if (jackpot.compareTo(BigDecimal.ZERO) == 1){
//                for (int i = 0; i < userLog.size(); i++){
//                    sum = BigDecimal.ZERO;
//                    for (int j = 0; j < userLog.get(i).size(); j++){
//                        if (userLog.get(i).get(j).getStakeType() == 1){ // 单压
//                            duStr += "独赢：" + userLog.get(i).get(j).getMoney() + " ASG = " + userLog.get(i).get(j).getOdds() + " x " + userLog.get(i).get(j).getPourNum() + "注 x （" + userLog.get(i).get(j).getDogNum() + "）\n";
//                        } else if (userLog.get(i).get(j).getStakeType() == 2){ // 名次
//                            mingStr += "名次" + userLog.get(i).get(j).getSelectType() + "：" + userLog.get(i).get(j).getMoney() + " ASG = " + userLog.get(i).get(j).getOdds() + " x " + userLog.get(i).get(j).getPourNum() + "注 x（" + userLog.get(i).get(j).getDogNum() + "）\n";
//                        } else if (userLog.get(i).get(j).getStakeType() == 3){ // 包围
//                            baoStr += "包围" + userLog.get(i).get(j).getSelectType() + "：" + userLog.get(i).get(j).getMoney() + " ASG = " + userLog.get(i).get(j).getOdds() + " x " + userLog.get(i).get(j).getPourNum() + "注 x（" + userLog.get(i).get(j).getDogNum() + "）\n";
//                        }
//                        sum = sum.add(userLog.get(i).get(j).getMoney());
//                        userId = userLog.get(i).get(j).getUserId();
//                    }
//
//                    if (sum.compareTo(BigDecimal.ZERO) == 1){
//                        mailStr01 = "恭喜您获得奖励： " + sum + " ASG\n";
//                        mailStr02 = "场次号:<color='#B73600'>No." + currentGameNum + "</color>\n";
//                        mailStr03 = "名次:<color='#B73600'>" + dogNumStr + "</color>\n";
//
//                        showStr = mailStr01 + kong01 + mailStr02 + mailStr03 + kong02 + duStr + mingStr + baoStr;
//
//                        MailVo mailVo = new MailVo();
//                        mailVo.setMailTitle("赞助成功！");
//                        mailVo.setMailContent(showStr);
//                        mailVo.setAwardNum(sum.intValue());
//                        mailVo.setIsAttribute(0);
//                        mailVo.setAwardType(1); // 代币
//                        mailVo.setType(2);
//                        mailVo.setImgName("jinbi");
//                        mailVo.setUserId(userId);
//                        mailVo.setCreateTime(new Date());
//                        mailService.save(mailVo);
//
//                        Map map = new HashMap();
//                        map.put("userId", userId);
//                        map.put("isHaveUnread", 1);
//                        userService.update(map);
//                    }
//                }
//            }

            List<LogAwardVo> logList = null;
            List<LogAwardVo> logAwardList = logAwardService.queryListByGameNum(currentGameNum);
            Map<Long, List<LogAwardVo>> userMap = new HashMap();
            for (LogAwardVo logAwardVo: logAwardList) {
                if (!userMap.containsKey(logAwardVo.getUserId())) {
                    logList = new ArrayList<>();
                } else {
                    logList = userMap.get(logAwardVo.getUserId());
                }
                logList.add(logAwardVo);
                userMap.put(logAwardVo.getUserId(), logList);
            }

            String showStr = "";
            String mailStr01 = "";
            String mailStr02 = "";
            String mailStr03 = "";
            String kong01 = "\n-------------战况摘要------------\n\n";
            String kong02 = "\n-------------赞助记录------------\n\n";
            String duStr = "";
            String mingStr = "";
            String baoStr = "";
            Long userId = null;
            BigDecimal sum = BigDecimal.ZERO;
            if (jackpot.compareTo(BigDecimal.ZERO) == 1){

                for (Map.Entry<Long, List<LogAwardVo>> entry : userMap.entrySet()) {
                    Long mapKey = entry.getKey();
                    List<LogAwardVo> mapValue = entry.getValue();
                    System.out.println("赞助用户：" + mapKey + " == " + mapValue);

                    duStr = "";
                    mingStr = "";
                    baoStr = "";
                    sum = BigDecimal.ZERO;
                    for (int j = 0; j < mapValue.size(); j++){
                        if (mapValue.get(j).getStakeType() == 1){ // 单压
                            duStr += "独赢：" + mapValue.get(j).getMoney() + " ASG = " + mapValue.get(j).getOdds() + " x " + mapValue.get(j).getPourNum() + "注 x （" + mapValue.get(j).getDogNum() + "）\n";
                        } else if (mapValue.get(j).getStakeType() == 2){ // 名次
                            mingStr += "名次" + mapValue.get(j).getSelectType() + "：" + mapValue.get(j).getMoney() + " ASG = " + mapValue.get(j).getOdds() + " x " + mapValue.get(j).getPourNum() + "注 x（" + mapValue.get(j).getDogNum() + "）\n";
                        } else if (mapValue.get(j).getStakeType() == 3){ // 包围
                            baoStr += "包围" + mapValue.get(j).getSelectType() + "：" + mapValue.get(j).getMoney() + " ASG = " + mapValue.get(j).getOdds() + " x " + mapValue.get(j).getPourNum() + "注 x（" + mapValue.get(j).getDogNum() + "）\n";
                        }
                        sum = sum.add(mapValue.get(j).getMoney());
                        userId = mapValue.get(j).getUserId();

                        mailStr01 = "恭喜您获得奖励： " + sum + " ASG\n";
                        mailStr02 = "场次号:<color='#B73600'>No." + currentGameNum + "</color>\n";
                        mailStr03 = "名次:<color='#B73600'>" + dogNumStr + "</color>\n";

                        showStr = mailStr01 + kong01 + mailStr02 + mailStr03 + kong02 + duStr + mingStr + baoStr;
                    }

                    MailVo mailVo = new MailVo();
                    mailVo.setMailTitle("赞助成功！");
                    mailVo.setMailContent(showStr);
                    mailVo.setAwardNum(sum.intValue());
                    mailVo.setIsAttribute(0);
                    mailVo.setAwardType(1); // 代币
                    mailVo.setType(2);
                    mailVo.setImgName("jinbi");
                    mailVo.setUserId(userId);
                    mailVo.setCreateTime(new Date());
                    if (sum.compareTo(BigDecimal.ZERO) == 1){
                        mailVo.setIsReceive(0);
                    } else {
                        mailVo.setIsReceive(1);
                    }
                    mailService.save(mailVo);

                    Map map = new HashMap();
                    map.put("userId", userId);
                    map.put("isHaveUnread", 1);
                    userService.update(map);
                }
            }

            for (int j = 0; j < currentList.size(); j++){

                DecimalFormat decimalFormat = new DecimalFormat("00");
                String numFormat = decimalFormat.format(currentList.get(j).getRanking());
                BigDecimal innerRatio = configService.queryByBigDecimalKey("inner_ranking_" + numFormat);
                BigDecimal outRatio = configService.queryByBigDecimalKey("out_ranking_" + numFormat);
                Integer brawnNum = configService.queryByIntKey("brawn_ranking_" + numFormat);
                BigDecimal sum2 = innerRatio.multiply(new BigDecimal(6000));
                BigDecimal sum1 = outRatio.multiply(new BigDecimal(currentBetTotal));
                BigDecimal sum3 = sum1.add(sum2);

                // 排除人机狗狗
                if (currentList.get(j).getId() > 0){

                    mailStr02 = "场次号:<color='#B73600'>No." + currentGameNum + "</color>\n";
                    mailStr03 = "名次:<color='#B73600'>" + dogNumStr + "</color>\n";

                    if (sum3.compareTo(BigDecimal.ZERO) == 1 || brawnNum > 0){

                        if (brawnNum > 0 && sum3.compareTo(BigDecimal.ZERO) == 1){
                            mailStr01 = "恭喜您获得奖励： " + sum3 + " ASG + " + brawnNum + " 体力值\n";
                        } else {
                            if (brawnNum > 0 && sum3.compareTo(BigDecimal.ZERO) <= 0) {
                                mailStr01 = "恭喜您获得奖励： " + brawnNum + " 体力值\n";
                            } else if (sum3.compareTo(BigDecimal.ZERO) == 1 && brawnNum <= 0) {
                                mailStr01 = "恭喜您获得奖励： " + sum3 + " ASG\n";
                            }
                        }

                        showStr = mailStr01 + kong01 + mailStr02 + mailStr03;

                        MailVo mailVo = new MailVo();
                        mailVo.setMailTitle("参赛结果！");
                        mailVo.setMailContent(showStr);
                        mailVo.setAwardNum(sum3.intValue());
                        mailVo.setIsAttribute(0);
                        mailVo.setAwardType(1); // 代币
                        mailVo.setType(2);
                        mailVo.setIsReceive(0);
                        if (brawnNum > 0 && sum3.compareTo(BigDecimal.ZERO) == 1){
                            mailVo.setBrawnNum(brawnNum);
                            mailVo.setImgName("jinbi,jingli");
                            mailVo.setGameAward(sum1.add(sum2) + "," + brawnNum);
                        } else {
                            if (brawnNum > 0 && sum3.compareTo(BigDecimal.ZERO) <= 0){
                                mailVo.setImgName("jingli");
                                mailVo.setBrawnNum(brawnNum);
                                mailVo.setGameAward(brawnNum.toString());
                            } else if (sum3.compareTo(BigDecimal.ZERO) == 1 && brawnNum <= 0) {
                                mailVo.setImgName("jinbi");
                                mailVo.setGameAward(sum1.add(sum2).toString());
                            }
                        }
                        mailVo.setUserId(currentList.get(j).getUserId());
                        mailVo.setCreateTime(new Date());
                        mailService.save(mailVo);
                    } else {
                        showStr = "很遗憾您的狗狗未能入围！" + kong01 + mailStr02 + mailStr03;
                        MailVo mailVo = new MailVo();
                        mailVo.setMailTitle("参赛结果！");
                        mailVo.setMailContent(showStr);
                        mailVo.setAwardNum(0);
                        mailVo.setIsAttribute(0);
                        mailVo.setAwardType(1); // 代币
                        mailVo.setType(2);
                        mailVo.setIsReceive(1);
                        mailVo.setBrawnNum(0);
                        mailVo.setImgName("");
                        mailVo.setGameAward("");
                        mailVo.setUserId(currentList.get(j).getUserId());
                        mailVo.setCreateTime(new Date());
                        mailService.save(mailVo);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结算赞助奖励
     */
    private void settleAccounts(){

        try {

            List<SupportRecordVo> supportRecordVos = supportRecordService.queryListByGameNum(currentGameNum);

            Map userMap = null;
            Boolean isHave = false;
            List<Map> mapList = new ArrayList<>();
            for (int i = 0; i < supportRecordVos.size(); i++){
                isHave = false;
                for (int j = 0; j < mapList.size(); j++){
                    if (supportRecordVos.get(i).getUserId().equals(Long.valueOf(mapList.get(j).get("userId").toString()))){
                        isHave = true;
                        break;
                    }
                }
                if (isHave == false){
                    userMap = new HashMap();
                    userMap.put("userId", supportRecordVos.get(i).getUserId());
                    mapList.add(userMap);
                }
            }

            // 查询比赛结果 并且按 名次排列赛道号顺序
            List<LogGameResultVo> gameResultList = logGameResultService.queryListByGameNum(currentGameNum);
            gameResultList.sort(Comparator.comparing(LogGameResultVo::getRanking));
            List<Integer> dogNumList = new ArrayList<>();
            for (int i = 0; i < gameResultList.size(); i++){
                dogNumList.add(gameResultList.get(i).getDogNumber());
            }

            Integer asg = 0;
            BigDecimal money = BigDecimal.ZERO;
            for (int j = 0; j < supportRecordVos.size(); j++){

                money = BigDecimal.ZERO;
                LogAwardVo logAwardVo = new LogAwardVo();
                logAwardVo.setUserId(supportRecordVos.get(j).getUserId());
                logAwardVo.setStakeType(supportRecordVos.get(j).getStakeType());
                logAwardVo.setSelectType(supportRecordVos.get(j).getSelectType());
                logAwardVo.setGameNum(supportRecordVos.get(j).getGameNum());
                logAwardVo.setDogNum(supportRecordVos.get(j).getDogNum());
                logAwardVo.setTrackNum(supportRecordVos.get(j).getTrackNum());
                logAwardVo.setPourNum(supportRecordVos.get(j).getPourNum());
                logAwardVo.setOdds(supportRecordVos.get(j).getOdds());
                logAwardVo.setCreateTime(new Date());
                logAwardVo.setAwardDesc("未押中");
                logAwardVo.setMoney(BigDecimal.ZERO);

                if (supportRecordVos.get(j).getStakeType() == 1){
                    // 押中 单压
                    if(supportRecordVos.get(j).getTrackNum().equals(dogNumList.get(0).toString())){
                        System.out.println("押中 单压");
                        asg = supportRecordVos.get(j).getPourNum() * 100;
                        money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                        logAwardVo.setAwardDesc("押中 "+ supportRecordVos.get(j).getDogNum() + " 单压");
                        logAwardVo.setMoney(money);
                    }
                } else if (supportRecordVos.get(j).getStakeType() == 2){

                    // 押注赛道List
                    List<String> trackNum = Arrays.asList(supportRecordVos.get(j).getTrackNum().split(","));

                    if(supportRecordVos.get(j).getSelectType() == 2){
                        // 本场比赛前两名
                        List<Integer> subList = dogNumList.subList(0, 2);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 名次前两名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中前两名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }

                    if(supportRecordVos.get(j).getSelectType() == 3){
                        // 本场比赛前三名
                        List<Integer> subList = dogNumList.subList(0, 3);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 名次前三名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中前三名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }

                    if(supportRecordVos.get(j).getSelectType() == 4){
                        // 本场比赛前四名
                        List<Integer> subList = dogNumList.subList(0, 4);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 名次前四名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中前四名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }

                } else if (supportRecordVos.get(j).getStakeType() == 3){

                    // 押注赛道List
                    List<String> trackNum = Arrays.asList(supportRecordVos.get(j).getTrackNum().split(","));

                    if(supportRecordVos.get(j).getSelectType() == 2){
                        // 本场比赛前两名
                        List<Integer> subList = dogNumList.subList(0, 2);
                        // 从小到大排序
                        Collections.sort(subList);
                        Collections.sort(trackNum);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 包围前两名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中包围前两名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }

                    if(supportRecordVos.get(j).getSelectType() == 3){
                        // 本场比赛前三名
                        List<Integer> subList = dogNumList.subList(0, 3);
                        // 从小到大排序
                        Collections.sort(subList);
                        Collections.sort(trackNum);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 包围前三名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中包围前三名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }

                    if(supportRecordVos.get(j).getSelectType() == 4){
                        // 本场比赛前四名
                        List<Integer> subList = dogNumList.subList(0, 4);
                        // 从小到大排序
                        Collections.sort(subList);
                        Collections.sort(trackNum);
                        if(trackNum.toString().equals(subList.toString())){
                            System.out.println("押中 包围前四名");
                            asg = supportRecordVos.get(j).getPourNum() * 100;
                            money = money.add(supportRecordVos.get(j).getOdds().multiply(new BigDecimal(asg)));
                            logAwardVo.setAwardDesc("押中包围前四名 "+ supportRecordVos.get(j).getDogNum());
                            logAwardVo.setMoney(money);
                        }
                    }
                }
                logAwardService.save(logAwardVo);
                currentWinTotal = currentWinTotal.add(money);
            }

            for (int j = 0; j < currentList.size(); j++){

                DecimalFormat decimalFormat = new DecimalFormat("00");
                String numFormat = decimalFormat.format(currentList.get(j).getRanking());
                BigDecimal outRatio = configService.queryByBigDecimalKey("out_ranking_" + numFormat);
                BigDecimal sum1 = outRatio.multiply(new BigDecimal(currentBetTotal));

                joinWinTotal = joinWinTotal.add(sum1);
            }

            // 本场比赛所有玩家 下注总金额 减去 本场比赛所有参赛玩家 赢得的场外奖金池总金额
            BigDecimal betTotal = new BigDecimal(currentBetTotal).subtract(joinWinTotal);
            // 本场玩家赢得总金额 小于 本场玩家押注总金额
            if (currentWinTotal.compareTo(betTotal) == -1){

                // 盈余出的金额 添加到基金池中
                BigDecimal surplus = betTotal.subtract(currentWinTotal);
                jackpot = jackpot.add(surplus);
                if (jackpot.compareTo(new BigDecimal(1000000)) == 1){
                    shareOutBonus = shareOutBonus.add(jackpot.subtract(new BigDecimal(1000000)));
                    jackpot = new BigDecimal(1000000);
                }
            } else if (currentWinTotal.compareTo(betTotal) == 1) { // 本场玩家赢得总金额 大于 本场玩家押注总金额

                // 亏损的金额 从基金池里扣除
                BigDecimal loss = currentWinTotal.subtract(betTotal);
                if (jackpot.compareTo(loss) == 1){
                    jackpot = jackpot.subtract(loss);
                }
            }

            Map jackpotMap = new HashMap();
            jackpotMap.put("key", "jackpot");
            jackpotMap.put("value", jackpot);
            configService.updateValueByKey(jackpotMap);

            Map bonusMap = new HashMap();
            bonusMap.put("key", "share_out_bonus");
            bonusMap.put("value", shareOutBonus);
            configService.updateValueByKey(bonusMap);

            System.out.println("赞助池剩余资金：" + jackpot);
            System.out.println("平台分红所得资金：" + shareOutBonus);
            System.out.println("本场下注总金额：" + currentBetTotal);
            System.out.println("本场比赛玩家押注赢得总金额：" + currentWinTotal);
            System.out.println("本场比参赛玩家赢得场外奖池总金额：" + joinWinTotal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取狗狗 速度、心情、耐力、幸运
     * @param num
     * @param max
     * @param min
     * @return
     */
    public static List<Integer> getWeight(int num, int max, int min) {

        List<Integer> result = new ArrayList<Integer>();
        if (num == 1) {
            result.add(max);
            return result;
        } else {
            int num1 = getRandom(min, (max / num + 1));
            result.add(num1);
            int total = max;
            for (int i = 1; i < num; i++) {
                total = total - num1;
                while (total < min) {
                    int maxc = Collections.max(result);
                    result.set(result.indexOf(maxc), min);
                    int s = result.stream().map(e -> e).reduce(Integer::sum).get();
                    total = max - s;
                }
                if (i != num - 1) {
                    num1 = getRandom(min, total);
                    result.add(num1);
                } else {
                    result.add(total);
                }
            }
        }
        return result;
    }

    /**
     * 获取一个随机数
     * @param MIN
     * @param MAX
     * @return
     */
    public static int getRandom(int MIN, int MAX) {
        Random random = new Random();
        return random.nextInt(MAX - MIN + 1) + MIN;
    }

    /**
     * 计算 每条赛道狗狗的胜率
     * @param sumFight
     */
    private void countDogRanking(BigDecimal sumFight){

        for (int i = 0; i < currentList.size(); i++){
            BigDecimal winNum = currentList.get(i).getFightingNum().divide(sumFight, 10, BigDecimal.ROUND_DOWN); // 胜率
            winNum = winNum.setScale(2, BigDecimal.ROUND_DOWN);
            currentList.get(i).setWeight(winNum.multiply(new BigDecimal(100)));
        }

        for (int i = 0; i < currentList.size(); i++){
            System.out.print( "赛道" + (i + 1) + "，的胜率" + currentList.get(i).getWeight() + "\n");
        }

        // 创建带有权重的对象包装
        List<WeightRandom.WeightObj<String>> list = new ArrayList<WeightRandom.WeightObj<String>>();
        // 构造参数、设置比例
        list.add(new WeightRandom.WeightObj<>("赛道1", currentList.get(0).getWeight().doubleValue()));    // 赛道1
        list.add(new WeightRandom.WeightObj<>("赛道2", currentList.get(1).getWeight().doubleValue()));    // 赛道2
        list.add(new WeightRandom.WeightObj<>("赛道3", currentList.get(2).getWeight().doubleValue()));    // 赛道3
        list.add(new WeightRandom.WeightObj<>("赛道4", currentList.get(3).getWeight().doubleValue()));    // 赛道4
        list.add(new WeightRandom.WeightObj<>("赛道5", currentList.get(4).getWeight().doubleValue()));    // 赛道5
        list.add(new WeightRandom.WeightObj<>("赛道6", currentList.get(5).getWeight().doubleValue()));    // 赛道6

        // 创建带有权重的随机生成器
        WeightRandom wr = RandomUtil.weightRandom(list);
        String randomStr = "";

        Integer ranking = 1;
        List<String> trackList = new ArrayList<>();

        while (true){
            randomStr = wr.next().toString();
            if("赛道1".equals(randomStr) && !trackList.contains("赛道1")){
                currentList.get(0).setRanking(ranking);
                trackList.add("赛道1");
                ranking++;
            } else if("赛道2".equals(randomStr) && !trackList.contains("赛道2")){
                currentList.get(1).setRanking(ranking);
                trackList.add("赛道2");
                ranking++;
            } else if("赛道3".equals(randomStr) && !trackList.contains("赛道3")){
                currentList.get(2).setRanking(ranking);
                trackList.add("赛道3");
                ranking++;
            } else if("赛道4".equals(randomStr) && !trackList.contains("赛道4")){
                currentList.get(3).setRanking(ranking);
                trackList.add("赛道4");
                ranking++;
            } else if("赛道5".equals(randomStr) && !trackList.contains("赛道5")){
                currentList.get(4).setRanking(ranking);
                trackList.add("赛道5");
                ranking++;
            } else if("赛道6".equals(randomStr) && !trackList.contains("赛道6")){
                currentList.get(5).setRanking(ranking);
                trackList.add("赛道6");
                ranking++;
            }
            if (trackList.size() >= 6) break;
        }

        System.out.println("随机到: " + trackList);
    }

    public Object matchingDogBEIFEN() {

        BigDecimal oneDogFightingMin = null;
        BigDecimal oneDogFightingMax = null;

        if(matchList.size() <= 0) {
            matchList.add(enrollList.get(0));
            enrollList.remove(0);
        }

        if (matchSecondsCurrent <= 5){
            oneDogFightingMin = matchList.get(0).getFightingNum().multiply(new BigDecimal(0.8));
            oneDogFightingMax = matchList.get(0).getFightingNum().multiply(new BigDecimal(1.2));
        } else {
            BigDecimal numMin = new BigDecimal(0.8).add(new BigDecimal(matchSecondsCurrent - 5).multiply(new BigDecimal(0.1)));
            BigDecimal numMax = new BigDecimal(0.8).add(new BigDecimal(matchSecondsCurrent - 5).multiply(new BigDecimal(0.1)));
            oneDogFightingMin = matchList.get(0).getFightingNum().multiply(numMin);
            oneDogFightingMax = matchList.get(0).getFightingNum().multiply(numMax);
        }

        for (int i = enrollList.size() - 1; i >= 0; i--){
            if (enrollList.get(i).getFightingNum().compareTo(matchList.get(0).getFightingNum()) == 0 || enrollList.get(i).getFightingNum().compareTo(oneDogFightingMin) == 1 || enrollList.get(i).getFightingNum().compareTo(oneDogFightingMax) == -1){
                matchList.add(enrollList.get(i));
                enrollList.remove(i);
                System.out.println("匹配到一只新的狗狗");
            }
        }

        if (matchList.size() >= 5){
            List<UserDogVo> dogVoList = matchList;

            // 组队完成 生成对战狗狗list 添加到队列
            synchronized (matcherQueue) {
                matcherQueue.offer(dogVoList);
                matcherQueue.notify();
            }

            // 组队完成 生成局号 添加到队列
            synchronized (gameNumQueue) {
                gameNumQueue.offer(CommonUtil.generateOrderNumber());
                gameNumQueue.notify();
            }

            matchSecondsCurrent = 0;
            matchList = new ArrayList<>();

            System.out.println("把狗狗 " + dogVoList + "加入到队列 中!");
        }

        // 匹配失败
//        if (matchSecondsTotal <= matchSecondsCurrent){
//
//            System.out.println(matchList);
//            System.out.println("匹配失败 返还用户 体力 和 AGS");
//            failList.addAll(matchList);
//            matchSecondsCurrent = 0;
//            matchList = new ArrayList<>();
//        }

        if (matchSecondsCurrent >= 20 && matchList.size() < 6){

            tempId = 0;
            //创建随机数对象
            Random r = new Random();
            List<Integer> battle = null;

            battle = new ArrayList<>();
            //判断集合的长度是不是小于4
            while (battle.size() < 6) {
                //产生一个随机数，添加到集合
                int number = r.nextInt(1000) % (1000 - 500 + 1) + 500;
                battle.add(number);
            }
            for (int i = matchList.size(); i < 6; i++){
                tempId --;
                matchList.add(createDog(tempId, new BigDecimal(battle.get(i)), 6, 1));
                System.out.println("进行人机匹配" + i);
            }

            countRanking();

            // 组队完成 生成对战狗狗list 添加到队列
            synchronized (matcherQueue) {
                matcherQueue.offer(matchList);
                matcherQueue.notify();
            }

            // 组队完成 生成局号 添加到队列
            synchronized (gameNumQueue) {
                gameNumQueue.offer(CommonUtil.generateOrderNumber());
                gameNumQueue.notify();
            }

            System.out.println("人机匹配完毕");
            System.out.println(matcherQueue);
            matchSecondsCurrent = 0;
            matchList = new ArrayList<>();
//            gameStatus = 2;
//            singleThreadExecutor();
        }

        return null;
    }

    private void countRankingBEIFEN(){

        // 按战力升序
//        List<UserDogVo> sortedByFightingAscList = matchList.stream().sorted(Comparator.comparing(h -> ((BigDecimal) h.getFightingNum()))).collect(Collectors.toList());


        //创建随机数对象
        Random r = new Random();
        List<Integer> battle = null;

        // 按战力降序
        matchList.sort(Comparator.comparing(UserDogVo::getFightingNum).reversed());
        for (int i = 0; i < matchList.size(); i++){

            battle = new ArrayList<>();
            //判断集合的长度是不是小于4
            while (battle.size() < 4) {
                //产生一个随机数，添加到集合
                int number = r.nextInt(8) % (8 - 3 + 1) + 3;
                battle.add(number);
            }

            matchList.get(i).setSpeed(battle.get(0));
            matchList.get(i).setMood(battle.get(1));
            matchList.get(i).setEndurance(battle.get(2));
            matchList.get(i).setLuck(battle.get(3));

            BigDecimal mood = new BigDecimal(matchList.get(i).getMood());
            // 冲刺冷却秒数
            BigDecimal coolSec = new BigDecimal(10).subtract(mood.multiply(new BigDecimal(0.01)).multiply(new BigDecimal(10)));
            // 正常速度
            Integer speed = matchList.get(i).getSpeed();
            // 冲刺概率
            Integer luck = matchList.get(i).getLuck();
            // 冲刺速度
            BigDecimal speedUp = new BigDecimal(matchList.get(i).getSpeed()).multiply(new BigDecimal(2));
            // 每次冲刺持续时间 (耐力点数 * 0.1)
            BigDecimal spanSec = new BigDecimal(matchList.get(i).getEndurance()).multiply(new BigDecimal(0.1));
            Integer speedUpCount = 0;
            Integer coolSecInt = 0;
            // 未冲刺总距离
            BigDecimal noDashDistance = BigDecimal.ZERO;
            // 未冲刺总秒数
            BigDecimal secNum = BigDecimal.ZERO;
            // 冲刺总距离
            BigDecimal dashDistance = BigDecimal.ZERO;
            // 冲刺总秒数
            BigDecimal speedUpSec = BigDecimal.ZERO;
            for(int j = 0; j < 90; j+=speed){
                // 获得冲刺概率
                Map<String, Integer> map = new HashMap<>();
                map.put("0", 100 - luck);
                map.put("1", luck);
                // 判断是否获得了冲刺
                if (coolSecInt == 0 && weightRandom(map).equals("1")){
                    speedUpCount++;
                    speedUpSec = speedUpSec.add(spanSec);
                    coolSecInt = coolSec.intValue();
                }
                coolSecInt--;
            }

            dashDistance = speedUpSec.multiply(speedUp);
            noDashDistance = new BigDecimal(90).subtract(dashDistance);
            secNum = noDashDistance.divide(new BigDecimal(speed), BigDecimal.ROUND_CEILING);

//            matchList.get(i).setSpurtNum(speedUpCount);
//            matchList.get(i).setRanking(i + 1);
//            matchList.get(i).setFinishSec(secNum.add(speedUpSec));

            if (i == 0) {
                matchList.get(i).setSpeed(5);
            } else {
                matchList.get(i).setSpeed(8 - i);
            }
            System.out.println(r.nextInt(2));
            matchList.get(i).setSpurtNum(r.nextInt(2));
            matchList.get(i).setRanking(i + 1);
            matchList.get(i).setFinishSec(new BigDecimal(12).add(new BigDecimal(4.5).multiply(new BigDecimal(i))));

        }

//        for (int i = 0; i < sortedByFightingAscList.size(); i++){
//            BigDecimal num1 = sortedByFightingAscList.get(i).getFightingNum().multiply(new BigDecimal(0.25));
//            BigDecimal num2 = sortedByFightingAscList.get(i).getFightingNum().divide(new BigDecimal(4));
//        }
    }

    public static String weightRandom(Map<String, Integer> map) {

        //获取map里的key值集合
        Set<String> keySet = map.keySet();
        List<String> weights = new ArrayList<>();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            //循环获取map里key值
            String weightStr = it.next();
            //获取key对应的value值，即每个金额对应的次数
            int weight = map.get(weightStr);
            for (int i = 0; i < weight; i++) {
                //等于将90个'10元',9个'100元',1个'1000元'分别添加到weights集合中
                weights.add(weightStr);
            }
        }
        // 随机获取集合里（100个金额字符串集）的字符串的下标
        int idx = new Random().nextInt(weights.size());
        // 返回对应的概率
        return weights.get(idx);
    }
}
