package util;

import com.alibaba.fastjson.JSON;
import scala.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 15-11-19.
 */
public class JsonUtil {
	public static Map<String,Map<String,Double>> jsonToMap(String json){
		Map<String,Object> first = (Map<String,Object>)JSON.parseObject(json);
		String userId = first.get("userId").toString();
		Map<String,ArrayList<Integer>> t = (Map<String,ArrayList<Integer>>)first.get("data");
		List<Integer> fondIds = t.get("fondIds");
		Map<String,Map<String,Double>> resu = new HashMap<String,Map<String,Double>>();
		Map<String,Double> mk= new HashMap<String,Double>();
		for(int id : fondIds){
			mk.put(String.valueOf(id), 1.0 / fondIds.size());
		}
		resu.put(userId,mk);
		return resu;
	}

	/**
	 * 获得data 中的key数组
	 * @param json
	 * @param key
	 * @return
	 */
	public static Map<String,String> jsonToMap2(String json,String key){
		Map<String,Object> first = (Map<String,Object>)JSON.parseObject(json);
		String userId = first.get("userId").toString();
		Map<String,ArrayList<Integer>> t = (Map<String,ArrayList<Integer>>)first.get("data");
		List<Integer> fondIds = t.get(key);
		Map<String,String> resu = new HashMap<String,String>();

		String tx = "";
		for(int id : fondIds){
			tx=tx+","+id+":"+1.0 / fondIds.size();
		}
		resu.put(userId,tx.replaceFirst(",",""));
		return resu;
	}

	/**
	 * 组装dislike数据
 	 */
	public static Map<String,String> jsonToMapForDisLike(String json,String key){
		Map<String,Object> first = (Map<String,Object>)JSON.parseObject(json);
		String userId = first.get("userId").toString();
		String logType = first.get("logType").toString();

		Map<String,String> resu = new HashMap<String,String>();
		String tx="";
		if("DISLIKE".equals(logType)){
			Map<String,ArrayList<Integer>> t = (Map<String,ArrayList<Integer>>)first.get("data");
			List<Integer> catIds = t.get(key);

			if(catIds != null && catIds.size() > 0){
				for(int id : catIds){
					tx=tx+","+id+":"+1.0;
				}
			}
		}
		resu.put(userId,tx.replaceFirst(",",""));
		return resu;
	}
}
