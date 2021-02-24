package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ymj
 * @Date： 2021/2/19 13:18
 * @description: 通用Dao
 */
@Component
public class MongoUtilImpl implements MongoUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据表名 查询所有 document
     * @param collectionName
     * @return
     */
    @Override
    public JSONArray queryDocuments(String collectionName) {
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);

        FindIterable<Document> documents = collection.find();
        JSONArray jsonArray = new JSONArray();
        Iterator<Document> iterator = documents.iterator();
        /**
         *  document.toString()   document: _id=601a3fcba3d6b1d076c30495
         *  document.toJson()     "_id" : { "$oid" : "601a3fcba3d6b1d076c30495"
         *  jsonArray.add(document) ··· ···
         *
         */
        while (iterator.hasNext()) {
            Document document = iterator.next();
            jsonArray.add(JSONObject.parseObject(document.toJson()));
        }
        return jsonArray;
    }

    @Override
    public List<String> findNameList(String collectionName, String keyName, String id) {
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents = collection.find()
                .filter(new Document("_id", new ObjectId(id)))
                .projection(
                        new Document(keyName, 1).append("_id", 0)
                );

        List<String> arrayList = new ArrayList<>();
        Iterator<Document> iterator = documents.iterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();
            for (Object d : (ArrayList) document.get(keyName) ) {
                System.out.println(d);
            }
            System.out.println(document.toJson());
            System.out.println(document.getString(keyName));
//            arrayList.add(d);
        }
        return arrayList;
    }
    @Override
    public List<String> findNameList(String collectionName, String keyName) {
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents = collection.find()
                .projection(
                        new Document(keyName, 1).append("_id", 0)
                );
        List<String> arrayList = new ArrayList<>();
        Iterator<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            arrayList.add(document.getString(keyName));
        }
        return arrayList;
    }



    /**
     *
     * @param keyName  返回的 key 对应 kv json
     * @param queryKey
     * @param queryValue
     * @param collectionName
     * @return
     */
    @Override
    public JSONObject findValue(String keyName, String queryKey, String queryValue,String collectionName) {

        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents;
        if (queryKey.equals("_id")) {
             documents = collection.find()
                    .filter(new Document("_id", new ObjectId(queryValue)))
                    .projection(
                            new Document(keyName, 1).append("_id", 0)
                    );
        } else {
            documents = collection.find()
                    .filter(new Document(queryKey, queryValue))
                    .projection(
                            new Document(keyName, 1).append("_id", 0)
                    );
        }
        Iterator<Document> iterator = documents.iterator();

        Document document = null;
        while (iterator.hasNext()) {
            document = iterator.next();
            System.out.println("*****");

        }
        return  JSONObject.parseObject(document.toJson());
    }


    /**
     * 更新 某 document 的 值
     * @param map 更新的 data  kv 对
     * @param id  document id
     * @param collectionName 集合名称
     * @param jsonKey  更新 的 father value
     * @return
     */
    @Override
    public String updateDocumentValue(Map map, String id, String collectionName, String jsonKey) {
        Query query = new Query();
        if(id != null) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        }
        Update update = new Update();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            // 获取key
            String key = jsonKey + "." + entry.getKey();
            System.out.println(jsonKey);
            // 获取value
            String value = entry.getValue();
            update.set(key, value);
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, collectionName);
        return updateResult.toString();
    }
}
