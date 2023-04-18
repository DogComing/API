package com.platform.util;

import cn.hutool.json.JSONObject;
import com.platform.entity.NftVo;
import com.platform.entity.UserDogVo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.nftJsonBaseUrl;

/**
 * 创建NFT工具类
 */
public class CreateNftUtil {

    public static Map createDogNftJson(UserDogVo userDogVo, NftVo nftVo){

        // 所有NFTJson存储路径
//        String path = "D:\\nginx\\Images\\dog_2.0\\json\\";
        String path = "/www/wwwroot/dog_race/static/json/";
        // 文件目录
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "dogId");
        root.put("value", userDogVo.getDogId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "dogName");
        root.put("value", userDogVo.getDogName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "dogDesc");
        root.put("value", userDogVo.getDogDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", userDogVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "animationName");
        root.put("value", userDogVo.getAnimationName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "dogGrade");
        root.put("value", userDogVo.getDogGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "dogBreed");
        root.put("value", userDogVo.getDogBreed());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightingNum");
        root.put("value", userDogVo.getFightingNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "speed");
        root.put("value", userDogVo.getSpeed());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "mood");
        root.put("value", userDogVo.getMood());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "endurance");
        root.put("value", userDogVo.getEndurance());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "luck");
        root.put("value", userDogVo.getLuck());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "maxGrowUpNum");
        root.put("value", userDogVo.getMaxGrowUpNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "currentGrowUpNum");
        root.put("value", userDogVo.getCurrentGrowUpNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "inbornNum");
        root.put("value", userDogVo.getInbornNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "createTime");
        root.put("value", nftVo.getCreateTime());
        list.add(root);
        resData.put("attributes", list);
        // 格式化json数据
        String jsonString = resData.toStringPretty();
        System.out.println("格式化json数据：" + jsonString);

        // 输出
        File file = new File(path + nftVo.getNftId() + ".json");
//        File file = new File("/Users/huzhiyuan/Desktop/" + nftVo.getNftId() + ".json");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        Map map = new HashMap();
        map.put("jsonUrl", nftJsonBaseUrl + urlPath + nftVo.getNftId() + ".json");
        map.put("attributes", list.toString());

        return map;
//        return nftJsonBaseUrl + urlPath + nftVo.getNftId() + ".json";
    }

}
