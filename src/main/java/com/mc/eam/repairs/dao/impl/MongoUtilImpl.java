package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
     * @param keyName 返回的 key 对应 kv json
     * @param
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

}
