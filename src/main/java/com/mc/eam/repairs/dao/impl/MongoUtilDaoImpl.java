package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ResponseCode;
import com.mc.eam.repairs.dao.MongoUtilDao;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ymj
 * @Date： 2021/2/19 13:18
 * @description: 通用Dao
 */
@Component
public class MongoUtilDaoImpl implements MongoUtilDao {

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

        List<Document> arrayList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        Iterator<Document> iterator = documents.iterator();
        String[] keyArray = keyName.split("\\.");
        // 只有一个 _id
        while (iterator.hasNext()) {
            Document document = iterator.next();
            for (int i = 0; i < keyArray.length - 1; i++) {
                Object keyObj =  document.get(keyArray[i]);
                if (keyObj.getClass().getName() == "java.util.ArrayList") {
                    arrayList = (List<Document>) keyObj;
                }
            }
            if (iterator.hasNext()) {
                System.out.println("错误： id 重复");
                break;
            }
        }

        for (Document d: arrayList) {
            String finalValue = keyArray[keyArray.length-1];
            if (d.containsKey(finalValue)) {
                valueList.add(d.getString(finalValue));
            }
        }
        return valueList;
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
            String key;
            if (jsonKey.length() == 0) {
                key = jsonKey + "." + entry.getKey();
            } else {
                key = entry.getKey();
            }
            System.out.println(jsonKey);
            // 获取value
            String value = entry.getValue();
            update.set(key, value);
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, collectionName);
        return updateResult.toString();
    }


    // todo 删除 某 document (永久删除)

    /**
     *
     * @param collectionName
     * @return 响应码
     */
    @Override
    public int deleteCollection(String collectionName) {
        if (mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.dropCollection(collectionName);
            return ResponseCode.SUCCESS.getCode();
        } else {
            return ResponseCode.NULL.getCode();
        }
    }


    //
    @Override
    public boolean deleteDocument(String collectionName,Map filters) {
        Query query = new Query();
        if(filters.containsKey("_id")) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(filters.get("_id").toString())));
        }
        mongoTemplate.remove(query,collectionName);
        return true;
    }

    /**
     * 在 集合中 查询 符合 条件 的 document 对应的 _id
     * @param collectionName 集合名称
     * @param filter 查询条件 Map
     * @return
     */
    @Override
    public List findUniqueIndex (String collectionName, @Nullable Map filter) {
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        List idList = new ArrayList<String>();
        Document filterDocument = new Document();
        if (filter != null)
            filterDocument = Document.parse(JSONObject.toJSONString(filter));

        FindIterable<Document> documents = collection.find()
                .filter(filterDocument)
                .projection(
                        new Document("_id", 1)
                );
        Iterator<Document> iterator = documents.iterator();
        // 只有一个 _id

        while (iterator.hasNext()) {
            idList.add(iterator.next().get("_id").toString());
        }
        return  idList;
    }



}
