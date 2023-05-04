package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.UserDogVo;
import com.platform.entity.UserVo;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;


@Api(tags = "匹配对手")
@RestController
@RequestMapping("/api/matcher")
public class ApiMatcherController extends ApiBaseAction {

    /***
     * @Description: 查询匹配
     * @Param: [loginUser]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "查询匹配")
    @GetMapping("mate")
    private Object matchingDog(@LoginUser UserVo loginUser) {

        for (int i = 0; i < matchList.size(); i++){
            if (loginUser.getUserId().equals(matchList.get(i).getUserId())){
                return returnStatus(loginUser.getUserId(),1);
            }
        }

        for (int i = 0; i < enrollList.size(); i++){
            if (loginUser.getUserId().equals(enrollList.get(i).getUserId())) {
                return returnStatus(loginUser.getUserId(),1);
            }
        }

        for (int i = 0; i < failList.size(); i++){
            if (loginUser.getUserId().equals(failList.get(i).getUserId())) {
                failList.remove(i);
                return returnStatus(loginUser.getUserId(),3);
            }
        }

        List<UserDogVo> dogVoList = matcherQueue.peek();
        if (dogVoList != null) {
            for (int i = 0; i < dogVoList.size(); i++){
                if (loginUser.getUserId().equals(dogVoList.get(i).getUserId())) {
                    return returnStatus(loginUser.getUserId(),4);
                }
            }
        }

        for (int i = 0; i < currentList.size(); i++){
            if (loginUser.getUserId().equals(currentList.get(i).getUserId())) {
                return returnStatus(loginUser.getUserId(), gameStatus);
            }
        }

        Boolean isHaveMe = false;
        for (List<UserDogVo> o : matcherQueue) {
            for (int j = 0; j < o.size(); j++){
                if (loginUser.getUserId().equals(o.get(j).getUserId())){
                    return returnStatus(loginUser.getUserId(),4);
                }
            }
        }

        if(isHaveMe == false) return returnStatus(loginUser.getUserId(),0);

        return returnStatus(loginUser.getUserId(), gameStatus);
    }

    /**
     * 返回用户当前状态
     * @param userId
     * @param status
     * @return
     */
    private Object returnStatus(Long userId, Integer status){

        System.out.println(userId + " 的当前状态 " + status);

        Map myMap = new HashMap();
        myMap.put("type", status);
        myMap.put("orderNum", currentGameNum);
        myMap.put("etcNum", matcherQueue.size());

        Map worldMap = new HashMap();
        worldMap.put("type", worldStatus);
        worldMap.put("orderNum", currentGameNum);

        Map returnMap = new HashMap();
        returnMap.put("myMap", myMap);
        returnMap.put("worldMap", worldMap);

        if (status == 0){
            System.out.println("============= 未匹配 ================");
            return toResponsObject(0, "未匹配", returnMap);
        } else if (status == 1){
            System.out.println("============= 匹配中 60秒 ================");
            return toResponsObject(0, "匹配中(20秒)...", returnMap);
        } else if (status == 2){
            System.out.println("============= 匹配成功 ================");
            return toResponsObject(0, "匹配成功...", returnMap);
        } else if (status == 3){
            System.out.println("============= 匹配失败 ================");
            return toResponsObject(0, "匹配失败...", returnMap);
        } else if (status == 4){
            System.out.println("============= 等待中 ================");
            return toResponsObject(0, "等待中...", returnMap);
        } else if (status == 5){
            System.out.println("============= 入场中 5秒 ================");
            return toResponsObject(0, "入场中(5秒)...", returnMap);
        } else if (status == 6){
            System.out.println("============= 押注中 120秒 ================");
            return toResponsObject(0, "押注中(120秒)...", returnMap);
        } else if (status == 7){
            System.out.println("============= 调整士气中 20秒 ================");
            return toResponsObject(0, "调整士气中(20秒)...", returnMap);
        } else if (status == 8){
            System.out.println("============= 比赛中 30秒 ================");
            return toResponsObject(0, "比赛中(30秒)...", returnMap);
        } else if (status == 9){
            System.out.println("============= 公布比赛结果 5秒 ================");
            return toResponsObject(0, "公布比赛结果(5秒)", returnMap);
        }

        return toResponsFail("网络异常,请重试一下");
    }
}
