package com.platform.util;

import cn.hutool.json.JSONObject;
import com.platform.entity.*;

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

    public static Map createDogNftJson(NftVo nftVo, UserDogVo dogVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", dogVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", dogVo.getDogId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", dogVo.getDogName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", dogVo.getDogDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", dogVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "animationName");
        root.put("value", dogVo.getAnimationName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", dogVo.getDogGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "breed");
        root.put("value", dogVo.getDogBreed());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightingNum");
        root.put("value", dogVo.getFightingNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "speed");
        root.put("value", dogVo.getSpeed());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "mood");
        root.put("value", dogVo.getMood());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "endurance");
        root.put("value", dogVo.getEndurance());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "luck");
        root.put("value", dogVo.getLuck());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "maxGrowUpNum");
        root.put("value", dogVo.getMaxGrowUpNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "currentGrowUpNum");
        root.put("value", dogVo.getCurrentGrowUpNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "inbornNum");
        root.put("value", dogVo.getInbornNum());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createFeedNftJson(NftVo nftVo, UserForageVo forageVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", forageVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", forageVo.getForageId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", forageVo.getForageName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", forageVo.getForageDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", forageVo.getGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", forageVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "type");
        root.put("value", forageVo.getForageType());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "typeTxt");
        root.put("value", forageVo.getForageTypeTxt());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "isIgnoreTalent");
        root.put("value", forageVo.getIsIgnoreTalent());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightingNum");
        root.put("value", forageVo.getFightingNum());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createPropNftJson(NftVo nftVo, UserPropVo propVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", propVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", propVo.getPropId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", propVo.getPropNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", propVo.getPropDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", propVo.getGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", propVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "attributeType");
        root.put("value", propVo.getAttributeType());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "attributeTxt");
        root.put("value", propVo.getAttributeTypeTxt());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "useType");
        root.put("value", propVo.getUseType());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createWildNftJson(NftVo nftVo, UserWildVo wildVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", wildVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", wildVo.getWildId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", wildVo.getWildName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", wildVo.getWildDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", wildVo.getGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", wildVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightingNum");
        root.put("value", wildVo.getFightingNum());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "isFight");
        root.put("value", wildVo.getIsFight());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "isIgnoreTalent");
        root.put("value", wildVo.getIsIgnoreTalent());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityMax");
        root.put("value", wildVo.getDurabilityMax());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityResidue");
        root.put("value", wildVo.getDurabilityResidue());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "wildType");
        root.put("value", wildVo.getWildType());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createCatchNftJson(NftVo nftVo, UserCatchEquipVo catchEquipVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", catchEquipVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", catchEquipVo.getEquipId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", catchEquipVo.getEquipName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", catchEquipVo.getEquipDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", catchEquipVo.getGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", catchEquipVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "catchType");
        root.put("value", catchEquipVo.getCatchType());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "deedType");
        root.put("value", catchEquipVo.getDeedType());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "extraOne");
        root.put("value", catchEquipVo.getExtraOne());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "extraTwo");
        root.put("value", catchEquipVo.getExtraTwo());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityMax");
        root.put("value", catchEquipVo.getDurabilityMax());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityResidue");
        root.put("value", catchEquipVo.getDurabilityResidue());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createFightNftJson(NftVo nftVo, UserFightEquipVo fightEquipVo){

        String path = "/www/wwwroot/dog_race/static/json/";
        String urlPath = "/json/";

        // 数据准备:
        JSONObject resData = new JSONObject();

        resData.put("id", nftVo.getNftId());
        resData.put("name", nftVo.getName());
        resData.put("description", nftVo.getDescription());
        resData.put("image", nftVo.getImage());

        ArrayList<Map> list = new ArrayList<>();
        JSONObject root = new JSONObject();
        root.put("trait_type", "id");
        root.put("value", fightEquipVo.getId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "configId");
        root.put("value", fightEquipVo.getFightId());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "name");
        root.put("value", fightEquipVo.getFightName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "desc");
        root.put("value", fightEquipVo.getFightDesc());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "grade");
        root.put("value", fightEquipVo.getGrade());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "imgName");
        root.put("value", fightEquipVo.getImgName());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightingAddition");
        root.put("value", fightEquipVo.getFightingAddition());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "fightType");
        root.put("value", fightEquipVo.getFightType());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityMax");
        root.put("value", fightEquipVo.getDurabilityMax());
        list.add(root);
        root = new JSONObject();
        root.put("trait_type", "durabilityResidue");
        root.put("value", fightEquipVo.getDurabilityResidue());
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
        if (file.exists()) file.delete();

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
    }

    public static Map createDogNftJson11(UserDogVo userDogVo, NftVo nftVo){

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
