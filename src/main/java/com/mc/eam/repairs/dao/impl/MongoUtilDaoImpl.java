package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ResponseCode;
import com.mc.eam.repairs.dao.MongoUtilDao;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
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
@Component("mongoUtilDaoImpl")
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
     * @param keyName  返回的 对应 key
     * @param filters 查询条件
     * @param collectionName 集合名称
     * @return
     */
    @Override
    public JSONObject findValue(String keyName, Map filters ,String collectionName) {

        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents;
        if (filters.containsKey("_id")) {
            documents = collection.find()
                    .filter(new Document("_id", new ObjectId(filters.get("_id").toString())))
                    .projection(
                            new Document(keyName, 1).append("_id", 0)
                    );
        } else {
            documents = collection.find()
                    .filter(new Document(filters))
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
     * @return the number of documents modified
     */
    @Override
    public long updateDocumentValue(Map map, String id, String collectionName) {
        MongoCollection collection  = mongoTemplate.getCollection(collectionName);
        return collection.updateOne(
                new Document(map),
                new Document("_id", new ObjectId(id))
        ).getModifiedCount();
    }

    /**
     * 某 document 更新 value
     * @param arrayKey key
     * @param list 追加数据
     * @param filters
     * @param collectionName
     * @return
     */
    @Override
    public long updateDocumentValue(String arrayKey, List list, Map filters, String collectionName) {
        MongoCollection collection  = mongoTemplate.getCollection(collectionName);
        return collection.updateMany(
                new Document(filters),
                new Document("$push", new Document(arrayKey, new Document("$each", list)))
        ).getModifiedCount();
    }

    /**
     * 删除 collection
     * @param collectionName
     * @return 响应码 ResponseCode
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


    /**
     * 删除 某 collection 某符合条件的 所有 document
     * @param collectionName 集合名称
     * @param filters 查询条件 <key, value> 需要符合document规范
     * @return 删除 数据的条数
     */
    @Override
    public long deleteDocument(String collectionName,Map filters) {

        MongoCollection collection  = mongoTemplate.getCollection(collectionName);
        return collection.deleteMany(new Document(filters)).getDeletedCount();
    }

    /**
     * 查询 在 collection 中  符合 条件 的 document 对应的 _id
     * @param collectionName 集合名称
     * @param filter 查询条件 Map (不包含 _id)
     * @return  _id 集合
     */
    @Override
    public List findUniqueIndex (String collectionName, @Nullable Map filter) {
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        List idList = new ArrayList<String>();
        Document filterDocument = new Document(filter);
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
