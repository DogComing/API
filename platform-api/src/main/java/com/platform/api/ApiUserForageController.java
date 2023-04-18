package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserForageVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserForageService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 用户饲养道具接口
 * @author: Yuan
 * @create: 2020-09-04 11:50
 **/
@Api(tags = "用户饲养道具接口")
@RestController
@RequestMapping("/api/forage")
public class ApiUserForageController extends ApiBaseAction {

    @Autowired
    private ApiUserForageService userForageService;

    /**
     * 饲料列表【每次请求10条】
     */
    @IgnoreAuth
    @GetMapping(value = "all")
    @ApiOperation(value = "饲料列表")
    public Object group(@LoginUser UserVo loginUser, @RequestParam(value = "forageType", defaultValue = "1") Integer forageType, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {

        try {
            //查询列表数据
            Map params = new HashMap();
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "forage_id");
            params.put("order", "desc");
            params.put("forageType", forageType);
            params.put("userId", loginUser.getUserId());

            Query query = new Query(params);
            int total = userForageService.queryTotal(query);
            List<UserForageVo> storeList = userForageService.queryList(query);
            ApiPageUtils storeData = new ApiPageUtils(storeList, total, query.getLimit(), query.getPage());

            return toResponsSuccess(storeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 饲料详情
     */
    @IgnoreAuth
    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = " 饲料详情")
    public Object detail(@PathVariable("id") Integer id) {

        try {
            UserForageVo userForageVo = userForageService.queryObject(id);
            return toResponsSuccess(userForageVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }
}
