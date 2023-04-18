package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.*;
import com.platform.service.ApiConfigService;
import com.platform.service.ApiSupportRecordService;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.CollectorsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.platform.config.ConstantConfig.*;

/**
 * @program: platform
 * @description: 狗狗比赛接口
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "狗狗比赛接口")
@RestController
@RequestMapping("/api/game")
public class ApiGameController extends ApiBaseAction {

    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiSupportRecordService supportRecordService;
    @Autowired
    private ApiConfigService configService;

    /***
     * @Description: 所有参赛狗狗
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "所有参赛狗狗")
    @GetMapping("all/dog")
    public Object takePart(@LoginUser UserVo loginUser) {

        try {
            if(currentList != null && currentList.size() > 0){
//                BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));
//                countRanking(sum);
                return toResponsSuccess(currentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("未查询到参赛狗狗");
    }

    /***
     * @Description: 单压
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "单压")
    @PostMapping("stake/one")
    public Object oneStake(@LoginUser UserVo loginUser, @RequestBody StakeOneVo stakeOneVo) {

        try {

            if (worldStatus == 3) return toResponsObject(400,"士气调整中，不可下注", null);

            BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));

            // 减少用户身上对应狗币/代币数值
            if (loginUser.getDogCoin() < stakeOneVo.getStakeNumber() * 100) return toResponsObject(1,"亲～您的ASG不足", null);
            userService.useDogCoinNum(loginUser.getUserId(), stakeOneVo.getStakeNumber() * 100);

//        jackpot += stakeOneVo.getStakeNumber() * 100;
            currentBetTotal += stakeOneVo.getStakeNumber() * 100;

            SupportRecordVo supportRecordVo = new SupportRecordVo();
            supportRecordVo.setGameNum(currentGameNum);
            supportRecordVo.setUserName(loginUser.getUserName());
            supportRecordVo.setUserId(loginUser.getUserId());
            supportRecordVo.setDogNum(stakeOneVo.getDogNumber().toString() + "号");
            supportRecordVo.setTrackNum(stakeOneVo.getDogNumber().toString());
            supportRecordVo.setDogId(stakeOneVo.getDogId().toString());
            supportRecordVo.setStakeType(1);
            supportRecordVo.setSelectType(0);
            supportRecordVo.setPourNum(stakeOneVo.getStakeNumber());
            supportRecordVo.setCreateTime(new Date());
            supportRecordVo.setOdds(countOneStake(sum).get(Integer.parseInt(stakeOneVo.getDogNumber().toString()) - 1));
            supportRecordService.save(supportRecordVo);

            // 给对应狗狗增加士气
            for (int i = 0; i < currentList.size(); i++){
                if(currentList.get(i).getId().equals(stakeOneVo.getDogId()) && currentList.get(i).getMorale() < 30){
                    currentList.get(i).setMorale(currentList.get(i).getMorale() + stakeOneVo.getStakeNumber());
                    break;
                }
            }

            return toResponsSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsObject(400,"操作失败，请重试", null);
    }

    /***
     * @Description: 按名次押注
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "按名次押注")
    @PostMapping("stake/more")
    public Object moreStake(@LoginUser UserVo loginUser, @RequestBody StakeMoreVo stakeMoreVo) {

        try {

            if (worldStatus == 3) return toResponsObject(400,"士气调整中，不可下注", null);

            Map oneMap = new HashMap();
            oneMap = stakeMoreVo.getOneMap();
            Map twoMap = new HashMap();
            twoMap = stakeMoreVo.getTwoMap();
            Map threeMap = new HashMap();
            threeMap = stakeMoreVo.getThreeMap();

            BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));

            Integer stakeNumber = 0;
            if (oneMap != null) stakeNumber = Integer.parseInt(oneMap.get("stakeNumber").toString()) * 100;
            if (twoMap != null) stakeNumber += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 100;
            if (threeMap != null) stakeNumber += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 100;

            // 减少用户身上对应狗币/代币数值
            if (loginUser.getDogCoin() < stakeNumber) return toResponsObject(1,"亲～您的ASG不足", null);
            userService.useDogCoinNum(loginUser.getUserId(), stakeNumber);

            if (oneMap != null && Integer.parseInt(oneMap.get("stakeNumber").toString()) > 0){
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(oneMap.get("one").toString() + "号 " + oneMap.get("two").toString() + "号");
                supportRecordVo.setTrackNum(oneMap.get("one").toString() + "," + oneMap.get("two").toString());
                dogNumList.add(Integer.parseInt(oneMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(oneMap.get("two").toString()));
                supportRecordVo.setStakeType(2);
                supportRecordVo.setSelectType(2);
                supportRecordVo.setPourNum(Integer.parseInt(oneMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(countOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(oneMap.get("stakeNumber").toString()) * 2 * 100;
                currentBetTotal += Integer.parseInt(oneMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(oneMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(oneMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(oneMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(oneMap.get("stakeNumber").toString()));
                    }
                }
            }

            if (twoMap != null && Integer.parseInt(twoMap.get("stakeNumber").toString()) > 0){
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(twoMap.get("one").toString() + "号 " + twoMap.get("two").toString() + "号 " + twoMap.get("three").toString() + "号");
                supportRecordVo.setTrackNum(twoMap.get("one").toString() + "," + twoMap.get("two").toString() + "," + twoMap.get("three").toString());
                dogNumList.add(Integer.parseInt(twoMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(twoMap.get("two").toString()));
                dogNumList.add(Integer.parseInt(twoMap.get("three").toString()));
                supportRecordVo.setStakeType(2);
                supportRecordVo.setSelectType(3);
                supportRecordVo.setPourNum(Integer.parseInt(twoMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(countOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 3 * 100;
                currentBetTotal += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(twoMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(twoMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(twoMap.get("threeDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                }
            }

            if (threeMap != null && Integer.parseInt(threeMap.get("stakeNumber").toString()) > 0) {
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(threeMap.get("one").toString() + "号 " + threeMap.get("two").toString() + "号 " + threeMap.get("three").toString() + "号 " + threeMap.get("four").toString() + "号");
                supportRecordVo.setTrackNum(threeMap.get("one").toString() + "," + threeMap.get("two").toString() + "," + threeMap.get("three").toString() + "," + threeMap.get("four").toString());
                dogNumList.add(Integer.parseInt(threeMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("two").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("three").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("four").toString()));
                supportRecordVo.setStakeType(2);
                supportRecordVo.setSelectType(4);
                supportRecordVo.setPourNum(Integer.parseInt(threeMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(countOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 4 * 100;
                currentBetTotal += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(threeMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("threeDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("fourDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                }
            }

            return toResponsSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsObject(400,"操作失败，请重试", null);
    }

    /***
     * @Description: 包围
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "包围")
    @PostMapping("stake/surround")
    public Object surroundStake(@LoginUser UserVo loginUser, @RequestBody StakeMoreVo stakeMoreVo) {

        try {

            if (worldStatus == 3) return toResponsObject(400,"士气调整中，不可下注", null);

            Map oneMap = new HashMap();
            oneMap = stakeMoreVo.getOneMap();
            Map twoMap = new HashMap();
            twoMap = stakeMoreVo.getTwoMap();
            Map threeMap = new HashMap();
            threeMap = stakeMoreVo.getThreeMap();

            BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));

            Integer stakeNumber = 0;
            if (oneMap != null) stakeNumber = Integer.parseInt(oneMap.get("stakeNumber").toString()) * 100;
            if (twoMap != null) stakeNumber += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 100;
            if (threeMap != null) stakeNumber += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 100;

            // 减少用户身上对应狗币/代币数值
            if (loginUser.getDogCoin() < stakeNumber) return toResponsObject(1,"亲～您的ASG不足", null);
            userService.useDogCoinNum(loginUser.getUserId(), stakeNumber);

            if (oneMap != null && Integer.parseInt(oneMap.get("stakeNumber").toString()) > 0){
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(oneMap.get("one").toString() + "号 " + oneMap.get("two").toString() + "号");
                supportRecordVo.setTrackNum(oneMap.get("one").toString() + "," + oneMap.get("two").toString());
                dogNumList.add(Integer.parseInt(oneMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(oneMap.get("two").toString()));
                supportRecordVo.setStakeType(3);
                supportRecordVo.setSelectType(2);
                supportRecordVo.setPourNum(Integer.parseInt(oneMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(returnOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(oneMap.get("stakeNumber").toString()) * 100 * 2;
                currentBetTotal += Integer.parseInt(oneMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(oneMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(oneMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(oneMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(oneMap.get("stakeNumber").toString()));
                    }
                }
            }

            if (twoMap != null && Integer.parseInt(twoMap.get("stakeNumber").toString()) > 0){
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(twoMap.get("one").toString() + "号 " + twoMap.get("two").toString() + "号 " + twoMap.get("three").toString() + "号");
                supportRecordVo.setTrackNum(twoMap.get("one").toString() + "," + twoMap.get("two").toString() + "," + twoMap.get("three").toString());
                dogNumList.add(Integer.parseInt(twoMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(twoMap.get("two").toString()));
                dogNumList.add(Integer.parseInt(twoMap.get("three").toString()));
                supportRecordVo.setStakeType(3);
                supportRecordVo.setSelectType(3);
                supportRecordVo.setPourNum(Integer.parseInt(twoMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(returnOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 100 * 3;
                currentBetTotal += Integer.parseInt(twoMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(twoMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(twoMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(twoMap.get("threeDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(twoMap.get("stakeNumber").toString()));
                    }
                }
            }

            if (threeMap != null && Integer.parseInt(threeMap.get("stakeNumber").toString()) > 0) {
                List<Integer> dogNumList = new ArrayList<>();
                SupportRecordVo supportRecordVo = new SupportRecordVo();
                supportRecordVo.setGameNum(currentGameNum);
                supportRecordVo.setUserName(loginUser.getUserName());
                supportRecordVo.setUserId(loginUser.getUserId());
                supportRecordVo.setDogNum(threeMap.get("one").toString() + "号 " + threeMap.get("two").toString() + "号 " + threeMap.get("three").toString() + "号 " + threeMap.get("four").toString() + "号");
                supportRecordVo.setTrackNum(threeMap.get("one").toString() + "," + threeMap.get("two").toString() + "," + threeMap.get("three").toString() + "," + threeMap.get("four").toString());
                dogNumList.add(Integer.parseInt(threeMap.get("one").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("two").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("three").toString()));
                dogNumList.add(Integer.parseInt(threeMap.get("four").toString()));
                supportRecordVo.setStakeType(3);
                supportRecordVo.setSelectType(4);
                supportRecordVo.setPourNum(Integer.parseInt(threeMap.get("stakeNumber").toString()));
                supportRecordVo.setCreateTime(new Date());
                supportRecordVo.setOdds(returnOdds(sum, dogNumList));
                supportRecordService.save(supportRecordVo);

//            jackpot += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 100;
                currentBetTotal += Integer.parseInt(threeMap.get("stakeNumber").toString()) * 100;

                // 给对应狗狗增加士气
                for (int i = 0; i < currentList.size(); i++){
                    if(currentList.get(i).getId().equals(threeMap.get("oneDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("twoDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("threeDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                    if(currentList.get(i).getId().equals(threeMap.get("fourDogId")) && currentList.get(i).getMorale() < 30){
                        currentList.get(i).setMorale(currentList.get(i).getMorale() + Integer.parseInt(threeMap.get("stakeNumber").toString()));
                    }
                }
            }

            return toResponsSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsObject(400,"操作失败，请重试", null);
    }

    /***
     * @Description: 当前游戏状态
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "当前游戏状态")
    @GetMapping("status")
    public Object gameStatus(@LoginUser UserVo loginUser) {

        try {
            Map map = new HashMap();
            map.put("type", worldStatus);
            map.put("second", currentSecond);
            return toResponsSuccess(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 获取奖金池
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取奖金池")
    @GetMapping("jackpot")
    public Object getJackpot(@LoginUser UserVo loginUser) {

        try {
            Map map = new HashMap();
            map.put("jackpot", jackpot);
            return toResponsSuccess(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 获取游戏结果
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取游戏结果")
    @GetMapping("result")
    public Object getGameResult(@LoginUser UserVo loginUser){

        try {
            Map map = null;
            List<Map> resultList = new ArrayList<>();
            for (int i = 0; i < currentList.size(); i++){
                DecimalFormat decimalFormat = new DecimalFormat("00");
                String numFormat = decimalFormat.format(currentList.get(i).getRanking());
                BigDecimal innerRatio = configService.queryByBigDecimalKey("inner_ranking_" + numFormat);
                BigDecimal outRatio = configService.queryByBigDecimalKey("out_ranking_" + numFormat);
                Integer brawnNum = configService.queryByIntKey("brawn_ranking_" + numFormat);
                BigDecimal sum1 = outRatio.multiply(new BigDecimal(currentBetTotal));
                BigDecimal sum2 = innerRatio.multiply(new BigDecimal(6000));

                map = new HashMap();
                map.put("ranking", currentList.get(i).getRanking());
                map.put("masterName", currentList.get(i).getMasterName());
                map.put("dogName", currentList.get(i).getDogName());
                map.put("brawn", brawnNum);
                map.put("dogCoin", sum1.add(sum2).intValue());
                resultList.add(map);
            }
            return toResponsSuccess(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 获取赞助记录
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取赞助记录")
    @GetMapping("record")
    public Object getSupportRecord(@LoginUser UserVo loginUser){

        try {
            List<SupportRecordVo> recordVoList = supportRecordService.recordByGameNum(loginUser.getUserId(), currentGameNum);
            return toResponsSuccess(recordVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    @ApiOperation(value = "获取胜率")
    @PostMapping("win/ratio")
    public Object getWinRatio(@LoginUser UserVo loginUser, @RequestBody StakeMoreVo stakeMoreVo){

        try {
            Map resultMap = new HashMap();
            if (stakeMoreVo.getType() == 1){
                BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));
                resultMap.put("type", 1);
                resultMap.put("result", countOneStake(sum));
                return toResponsSuccess(resultMap);
            }

            if (stakeMoreVo.getType() == 2 || stakeMoreVo.getType() == 3){

                Map oneMap = new HashMap();
                oneMap = stakeMoreVo.getOneMap();
                Map twoMap = new HashMap();
                twoMap = stakeMoreVo.getTwoMap();
                Map threeMap = new HashMap();
                threeMap = stakeMoreVo.getThreeMap();
                List<Integer> dogNumList = null;
                List<BigDecimal> winRatioList = new ArrayList<>();
                BigDecimal sum = currentList.stream().collect(CollectorsUtils.summingBigDecimal(UserDogVo::getFightingNum));

                if (oneMap != null && !oneMap.get("one").toString().equals("0") && !oneMap.get("two").toString().equals("0")){
                    dogNumList = new ArrayList<>();
                    dogNumList.add(Integer.parseInt(oneMap.get("one").toString()));
                    dogNumList.add(Integer.parseInt(oneMap.get("two").toString()));
                    if (stakeMoreVo.getType() == 2){
                        winRatioList.add(countOdds(sum, dogNumList));
                    } else {
                        winRatioList.add(returnOdds(sum, dogNumList)); // 添加到结果数组
                    }
                } else {
                    winRatioList.add(BigDecimal.ZERO);
                }

                if (twoMap != null && !twoMap.get("one").toString().equals("0") && !twoMap.get("two").toString().equals("0") && !twoMap.get("three").toString().equals("0")){
                    dogNumList = new ArrayList<>();
                    dogNumList.add(Integer.parseInt(twoMap.get("one").toString()));
                    dogNumList.add(Integer.parseInt(twoMap.get("two").toString()));
                    dogNumList.add(Integer.parseInt(twoMap.get("three").toString()));
                    if (stakeMoreVo.getType() == 2){
                        winRatioList.add(countOdds(sum, dogNumList));
                    } else {
                        winRatioList.add(returnOdds(sum, dogNumList)); // 添加到结果数组
                    }
                } else {
                    winRatioList.add(BigDecimal.ZERO);
                }

                if (threeMap != null && !threeMap.get("one").toString().equals("0") && !threeMap.get("two").toString().equals("0") && !threeMap.get("three").toString().equals("0") && !threeMap.get("four").toString().equals("0")){
                    dogNumList = new ArrayList<>();
                    dogNumList.add(Integer.parseInt(threeMap.get("one").toString()));
                    dogNumList.add(Integer.parseInt(threeMap.get("two").toString()));
                    dogNumList.add(Integer.parseInt(threeMap.get("three").toString()));
                    dogNumList.add(Integer.parseInt(threeMap.get("four").toString()));
                    if (stakeMoreVo.getType() == 2){
                        winRatioList.add(countOdds(sum, dogNumList));
                    } else {
                        winRatioList.add(returnOdds(sum, dogNumList)); // 添加到结果数组
                    }
                } else {
                    winRatioList.add(BigDecimal.ZERO);
                }

                resultMap.put("type", stakeMoreVo.getType());
                resultMap.put("result", winRatioList);
                return toResponsSuccess(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("请按规则选择赞助！");
    }

    /**
     * 计算单压每个狗狗的赔率
     * @param sumFight
     * @return
     */
    private List<BigDecimal> countOneStake(BigDecimal sumFight){

        List<BigDecimal> winRatioList = new ArrayList<>();
        for (int i = 0; i < currentList.size(); i++){
            BigDecimal winNum = currentList.get(i).getFightingNum().divide(sumFight, 10, BigDecimal.ROUND_DOWN); // 胜率
            BigDecimal result = BigDecimal.ONE.divide(winNum, 10, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(0.8)); // 赔率
            winRatioList.add(result.setScale(1, RoundingMode.HALF_UP)); // 保留两位小数,并添加到结果list中
        }
        return winRatioList;
    }

    private BigDecimal returnOdds(BigDecimal sumFight, List<Integer> dogNumList){

        BigDecimal winNum = BigDecimal.ZERO;
        List<List> composeList = getArrange(dogNumList, dogNumList.size(), null, null);
        for (int i = 0; i < composeList.size(); i++){
            winNum = winNum.add(countMoreStake(sumFight, composeList.get(i)));
        }
        BigDecimal result = BigDecimal.ONE.divide(winNum, 10, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(0.8)); // 赔率
        return result.setScale(1, RoundingMode.HALF_UP); // 保留两位小数
    }

    /**
     * 计算名次赔率
     * @param sumFight
     * @param dogNumList
     * @return
     */
    private BigDecimal countOdds(BigDecimal sumFight, List<Integer> dogNumList){

        BigDecimal winNum = countMoreStake(sumFight, dogNumList);
        BigDecimal result = BigDecimal.ONE.divide(winNum, 10, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(0.8)); // 赔率
        return result.setScale(1, RoundingMode.HALF_UP); // 保留两位小数;
    }

    /**
     * 名次胜率
     * @param sumFight
     * @param dogNumList
     * @return
     */
    private BigDecimal countMoreStake(BigDecimal sumFight, List<Integer> dogNumList){

        List<UserDogVo> dogList = currentList;

        // 前二胜率（根据选中狗狗会变） 胜率=(Z1/S)*(Z2/(S-Z1))
        if (dogNumList.size() <= 2){
            Integer firstDogIndex = dogNumList.get(0) - 1; // 押注第一条赛道 对应的 本局狗狗列表的索引角标
            Integer secondDogIndex = dogNumList.get(1) - 1; // 押注第二条赛道 对应的 本局狗狗列表的索引角标
            System.out.println(dogNumList);
            System.out.println("押注的第一条狗狗信息：" + dogList.get(firstDogIndex));
            System.out.println("押注的第二条狗狗信息：" + dogList.get(secondDogIndex));
            BigDecimal num01 = dogList.get(firstDogIndex).getFightingNum().divide(sumFight, 10, BigDecimal.ROUND_DOWN);

            BigDecimal num02 = sumFight.subtract(dogList.get(secondDogIndex).getFightingNum());
            BigDecimal num03 = dogList.get(firstDogIndex).getFightingNum().divide(num02, 10, BigDecimal.ROUND_DOWN);

            return num01.multiply(num03); // 胜率
        }

        // 前三胜率（根据选中狗狗会变） 胜率=(Z1/S)*(Z2/(S-Z1))*(Z3/(S-Z1-Z2))
        if (dogNumList.size() <= 3){
            Integer firstDogIndex = dogNumList.get(0) - 1; // 押注第一条赛道 对应的 本局狗狗列表的索引角标
            Integer secondDogIndex = dogNumList.get(1) - 1; // 押注第二条赛道 对应的 本局狗狗列表的索引角标
            Integer thirdDogIndex = dogNumList.get(2) - 1; // 押注第三条赛道 对应的 本局狗狗列表的索引角标
            System.out.println(dogNumList);
            System.out.println("押注的第一条狗狗信息：" + dogList.get(firstDogIndex));
            System.out.println("押注的第二条狗狗信息：" + dogList.get(secondDogIndex));
            System.out.println("押注的第三条狗狗信息：" + dogList.get(thirdDogIndex));

            BigDecimal num01 = dogList.get(firstDogIndex).getFightingNum().divide(sumFight, 2, BigDecimal.ROUND_DOWN);

            BigDecimal num02 = sumFight.subtract(dogList.get(secondDogIndex).getFightingNum());
            BigDecimal num03 = dogList.get(firstDogIndex).getFightingNum().divide(num02, 2, BigDecimal.ROUND_DOWN);

//            BigDecimal num04 = sumFight.subtract(dogList.get(0).getFightingNum()).subtract(dogList.get(1).getFightingNum());
            BigDecimal num04 = sumFight.subtract(dogList.get(firstDogIndex).getFightingNum()).subtract(dogList.get(secondDogIndex).getFightingNum());
            BigDecimal num05 = dogList.get(thirdDogIndex).getFightingNum().divide(num04, 2, BigDecimal.ROUND_DOWN);

            return num01.multiply(num03).multiply(num05); // 胜率
        }

        // 前四胜率（根据选中狗狗会变） 胜率=(Z1/S)*(Z2/(S-Z1))*(Z3/(S-Z1-Z2))*(Z4/(S-Z1-Z2-Z3))
        if (dogNumList.size() <= 4){
            Integer firstDogIndex = dogNumList.get(0) - 1; // 押注第一条赛道 对应的 本局狗狗列表的索引角标
            Integer secondDogIndex = dogNumList.get(1) - 1; // 押注第二条赛道 对应的 本局狗狗列表的索引角标
            Integer thirdDogIndex = dogNumList.get(2) - 1; // 押注第三条赛道 对应的 本局狗狗列表的索引角标
            Integer fourthDogIndex = dogNumList.get(3) - 1; // 押注第四条赛道 对应的 本局狗狗列表的索引角标
            System.out.println(dogNumList);
            System.out.println("押注的第一条狗狗信息：" + dogList.get(firstDogIndex));
            System.out.println("押注的第二条狗狗信息：" + dogList.get(secondDogIndex));
            System.out.println("押注的第三条狗狗信息：" + dogList.get(thirdDogIndex));
            System.out.println("押注的第四条狗狗信息：" + dogList.get(fourthDogIndex));

            BigDecimal num01 = dogList.get(firstDogIndex).getFightingNum().divide(sumFight, 2, BigDecimal.ROUND_DOWN);

            BigDecimal num02 = sumFight.subtract(dogList.get(secondDogIndex).getFightingNum());
            BigDecimal num03 = dogList.get(firstDogIndex).getFightingNum().divide(num02, 2, BigDecimal.ROUND_DOWN);

//            BigDecimal num04 = sumFight.subtract(dogList.get(0).getFightingNum()).subtract(dogList.get(1).getFightingNum());
            BigDecimal num04 = sumFight.subtract(dogList.get(firstDogIndex).getFightingNum()).subtract(dogList.get(secondDogIndex).getFightingNum());
            BigDecimal num05 = dogList.get(thirdDogIndex).getFightingNum().divide(num04, 2, BigDecimal.ROUND_DOWN);

//            BigDecimal num06 = sumFight.subtract(dogList.get(0).getFightingNum()).subtract(dogList.get(1).getFightingNum()).subtract(dogList.get(2).getFightingNum());
            BigDecimal num06 = sumFight.subtract(dogList.get(firstDogIndex).getFightingNum()).subtract(dogList.get(secondDogIndex).getFightingNum()).subtract(dogList.get(thirdDogIndex).getFightingNum());
            BigDecimal num07 = dogList.get(fourthDogIndex).getFightingNum().divide(num06, 2, BigDecimal.ROUND_DOWN);

            return num01.multiply(num03).multiply(num05).multiply(num07); // 胜率
        }

        return BigDecimal.ZERO;
    }

    /**
     * 循环递归获取给定数组元素(无重复)的全排列
     *
     * @param oriList 原始数组
     * @param oriLen 原始数组size
     * @param arrayCombResult 数组排列结果集，可传null或空Set
     * @param preList 记录排列参数，可传null或空List
     * @return 排列结果
     */
    public List<List> getArrange(List oriList, int oriLen, List<List> arrayCombResult, List preList){

        if (oriList == null){
            return arrayCombResult;
        }

        if (arrayCombResult == null){
            arrayCombResult = new ArrayList<>();
        }

        if (preList == null){
            preList = new ArrayList();
        }

        for (int i = 0; i < oriList.size(); i++){
            while(preList.size() > 0 && oriList.size() + preList.size() > oriLen){
                preList.remove(preList.size() - 1);
            }
            List arrList = new ArrayList(oriList);
            preList.add(arrList.get(i));
            arrList.remove(i);
            if (arrList.isEmpty()){
                arrayCombResult.add(preList);
            }else {
                getArrange(arrList, oriLen, arrayCombResult, preList);
            }
        }
        return arrayCombResult;
    }

}
