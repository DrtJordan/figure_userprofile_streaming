package com.caishi.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.*;

/**
 * Created by root on 15-10-27.
 */
public class Util {
    // 更新用户冷启动时分类权重算法
    public static void updateDoc(String newData, Document doc,int topN){
        CatModel o = JSON.parseObject(doc.toJson(),CatModel.class);
        Map<String,Double> cs = new HashMap<String,Double>();
        double count = 1.0;
        if(o.getCatLikes() != null) {
            for (Cat c : o.getCatLikes()) {
                cs.put(String.valueOf(c.getCatId()), c.getWeight());
            }
            count +=1.0;
        }

        // 新旧数据拟合后归一化
        for(String kv : newData.split(",")){
            String[] tm = kv.split(":");
            if(cs.containsKey(tm[0])){
                cs.put(tm[0],(Double.valueOf(cs.get(tm[0]))+Double.valueOf(tm[1])));
            }else{
                cs.put(tm[0],Double.valueOf(tm[1]));
            }
        }
        List<Document> r = new ArrayList<Document>();
        for(String key : cs.keySet()){
            Document tmp= new Document();
            tmp.append("catId",Integer.valueOf(key)).append("weight",cs.get(key)/count);
            r.add(tmp);
        }
        Collections.sort(r, new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                int flag = 0;
                double result = o1.getDouble("weight")-o2.getDouble("weight");
                if(result > 0 )
                    flag = 1;
                else if(result < 0)
                    flag = -1;
                return -flag;
            }
        });
        if(r.size() > topN){
            r = r.subList(0,topN);
        }
        doc.append("catLikes",r);
    }

    /**
     * 更新用户的dislike属性算法
      */
    public static void updateDisLikeDoc(String newData, Document doc,double dislikeBase,double dislikeExponent){
        CatModel o = JSON.parseObject(doc.toJson(),CatModel.class);
        Map<String,Double> cs = new HashMap<String,Double>();

        if(o.getCatLikes() != null) {
            for (Cat c : o.getCatLikes()) {
                cs.put(String.valueOf(c.getCatId()), c.getWeight());
            }
        }

        // 计算
        for(String kv : newData.split(",")){
            if(StringUtils.isEmpty(kv))
                continue;
            String[] tm = kv.split(":");
            if(cs.containsKey(tm[0])){
                cs.put(tm[0],cs.get(tm[0]) * dislikeExponent);
            }else{
                cs.put(tm[0],dislikeBase*dislikeExponent);
            }
        }

        // 计算
        List<Document> r = new ArrayList<Document>();
        for(String key : cs.keySet()){
            Document tmp= new Document();
            tmp.append("catId",Integer.valueOf(key)).append("weight",cs.get(key));
            r.add(tmp);
        }
        Collections.sort(r, new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                int flag = 0;
                double result = o1.getDouble("weight")-o2.getDouble("weight");
                if(result > 0 )
                    flag = 1;
                else if(result < 0)
                    flag = -1;
                return -flag;
            }
        });
        List<Document> tmp = r;
        if(r.size()>10){
            tmp = r.subList(0,10);
        }
        double count = 0.0;
        for(Document v : tmp)
            count+=v.getDouble("weight");
        for(Document v : tmp){
            v.append("weight",v.getDouble("weight")/count);
        }
        doc.append("catLikes",tmp);
    }
}
