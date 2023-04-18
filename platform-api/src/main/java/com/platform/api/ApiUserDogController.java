package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.UserDogVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserDogService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户狗狗信息
 *
 * @author xingGuangTeam
 * @email 249893127@qq.com
 * @date 2019-03-23 15:31
 */
@Api(tags = "用户狗狗信息")
@RestController
@RequestMapping("/api/user/dog")
public class ApiUserDogController extends ApiBaseAction {

    @Autowired
    private ApiUserDogService userDogService;

    /***
     * @Description: 获取用户所有狗狗
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取用户所有狗狗")
    @PostMapping("all")
    public Object list(@LoginUser UserVo loginUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            Map params = new HashMap();
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "is_use");
            params.put("order", "desc");
            params.put("userId", loginUser.getUserId());

            //查询列表数据
            Query query = new Query(params);
            int total = userDogService.queryTotal(query);
            List<UserDogVo> dogVoList = userDogService.queryList(query);
            ApiPageUtils pageUtil = new ApiPageUtils(dogVoList, total, query.getLimit(), query.getPage());

            return toResponsSuccess(pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 获取用户某个狗狗信息
     * @Param: [orderId]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取用户某个狗狗信息")
    @GetMapping(value = "detail/{id}")
    public Object detail(@LoginUser UserVo loginUser, @PathVariable("dogId") Integer dogId) {

        try {
            Map map = new HashMap();
            map.put("id", dogId);
            map.put("userId", loginUser.getUserId());
            UserDogVo dogInfo = userDogService.queryUserDogInfo(map); // 查询狗狗信息
            if (null == dogInfo) return toResponsObject(400, "未查询到狗狗信息", "");
            return toResponsSuccess(dogInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 设置宠物为使用状态
     * @Param: [orderId]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "设置宠物为使用状态")
    @PutMapping(value = "use/{dogId}")
    public Object useDog(@LoginUser UserVo loginUser, @PathVariable("dogId") Integer dogId) {

        try {
            userDogService.updateBatchIsUse(loginUser.getUserId());
            Map map = new HashMap();
            map.put("dogId", dogId);
            map.put("isUse", 1);
            userDogService.update(map);
            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("操作失败");
    }

    /***
     * @Description: 获取用户所有战斗狗狗
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取用户所有战斗狗狗")
    @GetMapping("fight")
    public Object fightDogList(@LoginUser UserVo loginUser) {
        try {
            List<UserDogVo> dogVoList = userDogService.queryAllListByUser(loginUser.getUserId());
            return toResponsSuccess(dogVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }
}
