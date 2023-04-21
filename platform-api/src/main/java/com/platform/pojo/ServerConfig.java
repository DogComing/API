package com.platform.pojo;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import com.platform.entity.LogAwardVo;
import com.platform.entity.LogGameResultVo;
import com.platform.entity.SupportRecordVo;
import com.platform.service.ApiConfigService;
import com.platform.service.ApiLogAwardService;
import com.platform.service.ApiLogGameResultService;
import com.platform.service.ApiSupportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static com.platform.config.ConstantConfig.*;

/**
 * @program: platform
 * @description: 程序自启组件类
 * @author: Yuan
 * @create: 2020-09-16 15:06
 **/
@Component
public class ServerConfig {

    @Autowired
    private ApiConfigService configService;

    @Autowired
    private ApiSupportRecordService supportRecordService;
    @Autowired
    private ApiLogAwardService logAwardService;
    @Autowired
    private ApiLogGameResultService logGameResultService;

    @PostConstruct
    public void automatic() {

        String str = appId + ":" + appSecret;
        authorization = "Bearer " + Base64.getEncoder().encodeToString(str.getBytes());

        jackpot = configService.queryByBigDecimalKey("jackpot");
        shareOutBonus = configService.queryByBigDecimalKey("share_out_bonus");

        gameBeginTime = configService.queryByStringKey("begin_time");
        gameEndTime = configService.queryByStringKey("end_time");

        System.out.println("场外奖金池：" + jackpot);
        System.out.println("平台分红：" + shareOutBonus);
        System.out.println("每日比赛开始时间：" + gameBeginTime);
        System.out.println("每日比赛结束时间：" + gameEndTime);
    }

    private Integer settleAccounts(){

        try {

            currentGameNum = "20230417163226000694834";
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

            for (int i = 0; i < supportRecordVos.size(); i++){
                System.out.println(supportRecordVos.get(i).getUserName() + "用户：" +  supportRecordVos.get(i).getTrackNum());
            }


            Integer asg = 0;
            BigDecimal money = BigDecimal.ZERO;
            for (int j = 0; j < supportRecordVos.size(); j++){

                System.out.println("狗狗号列表=====" + dogNumList);
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
                        System.out.println("======================");
                        System.out.println(dogNumList.get(0));
                        System.out.println(supportRecordVos.get(j).getUserName());
                        System.out.println(supportRecordVos.get(j).getDogNum());
                        System.out.println(supportRecordVos.get(j).getTrackNum());
                        System.out.println("======================");
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
                        List<Integer> tempList = (List<Integer>) SerializationUtils.clone((Serializable) dogNumList);
                        // 本场比赛前两名
                        List<Integer> subList = tempList.subList(0, 2);
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
                        List<Integer> tempList = (List<Integer>) SerializationUtils.clone((Serializable) dogNumList);
                        // 本场比赛前三名
                        List<Integer> subList = tempList.subList(0, 3);
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
                        List<Integer> tempList = (List<Integer>) SerializationUtils.clone((Serializable) dogNumList);
                        // 本场比赛前四名
                        List<Integer> subList = tempList.subList(0, 4);
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
//                logAwardService.save(logAwardVo);
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

        return 0;
    }
}
