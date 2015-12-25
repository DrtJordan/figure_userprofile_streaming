package com.caishi.model;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.operation.UpdateOperation;
import com.mongodb.util.JSON;
import org.bson.BsonArray;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Filter;

/**
 * Created by root on 15-10-16.
 */
public class MongoTest {
    public static void main(String[] args) throws UnknownHostException, MongoException {
        MongoClient mg = new MongoClient( "10.1.1.134" , 27017 );

        //查询所有的Database
        for (String name : mg.getDatabaseNames()) {
            System.out.println("dbName: " + name);
        }

        MongoDatabase db = mg.getDatabase("user_profiles");

        //查询所有的聚集集合
        for (String name : db.listCollectionNames()) {
            System.out.println("collectionName: " + name);
        }

        MongoCollection ul = db.getCollection("usercatLike");
        if(ul == null){
            db.createCollection("usercatLike");
            ul = db.getCollection("usercatLike");
        }
        System.out.println(ul.find().limit(10));
        try {
            List<Document> catList = new ArrayList<Document>();
            catList.add(new Document().append("catId",1).append("weight",0.1));
            catList.add(new Document().append("catId",3).append("weight",0.3));

            Document doc = new Document();
            doc.append("createTime","12").append("catLikes",catList).append("_id","tx123");
            ul.updateMany(Filters.eq("_id","tx123"), new Document("$set",doc), new UpdateOptions().upsert(true));
//
//            Map<Integer,Double> cs = new HashMap<Integer,Double>();
//            Document one = (Document) ul.find().first();
//            CatModel cm = com.alibaba.fastjson.JSON.parseObject(one.toJson(),CatModel.class);
//            for(Cat c : cm.getCatLikes()){
//                cs.put(c.getCatId(),c.getWeight());
//            }
//
//            System.out.println(cm.getCatLikes().get(0).toString());
        }catch (Exception  e){
            e.printStackTrace();
            System.out.println("---------");
        }
//        for(Object o :startup_log.find(Filters.eq("_id","10-1-1-134-1445082564741"))){
//            System.out.println(((Document) o).toJson());
//        }
//        for(Object o : startup_log.find().limit(10)){
//            System.out.println(((Document) o).toJson());
//        }
        //查询所有的数据
//        for (final Object index : startup_log.listIndexes()) {
//            System.out.println(((Document) index).toJson());
//        }

//        MongoDatabase test = mg.getDatabase("mydb");

//        test.createCollection("user");
//        MongoCollection user = test.getCollection("test3");
//        BsonArray ba = new BsonArray();
////        ba.add("1");
//        Document d = new Document("name", "name2").append("pos", 111);
//        user.updateMany(Filters.eq("name","name2"), new Document("$set",d), new UpdateOptions().upsert(true));
//        user.insertOne(d);

//        user.createIndex(new Document("id","text"));
//        Iterator it = user.find().into(new ArrayList()).iterator();
//        System.out.println(user.count());
//        while (it.hasNext()){
//            System.out.println(it.next());
//        }
//        for (final Object index : user.listIndexes()) {
//            System.out.println(((Document) index).toJson());
//        }
//        System.out.println(cur.count());
//        System.out.println(cur.getCursorId());
//        System.out.println(JSON.serialize(cur));
    }
    public void test(){
        MongoClient mg = new MongoClient( "10.1.1.134" , 27017 );
    }
}
