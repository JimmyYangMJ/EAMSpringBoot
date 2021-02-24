package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.MongoAssetDao;
import com.mc.eam.repairs.util.ConvertUtil;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.util.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author ymj
 * @Date： 2021/1/26 11:18
 * @description: Mongo CRUD
 */
@Component
public class MongoAssetDaoImpl implements MongoAssetDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public static String collectionName = null;

    /**
     * 插入 动态 Object （新增）
     * @param objectBean 要存储的 Javabean
     * @param objKey 需要包装为嵌套 Object 的 中的 key，当key 为 null 及非嵌套
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public <T> boolean insert(Object objectBean, String objKey) throws IllegalAccessException {

        // create codec registry for POJOs
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build() )
        );

        MongoCollection<T> collection = (MongoCollection<T>) mongoTemplate.getDb()
                .withCodecRegistry(pojoCodecRegistry)
                .getCollection(collectionName, objectBean.getClass());
        if(objKey == null) {
            collection.insertOne((T) objectBean);
        }else {
            Document d = ConvertUtil.convertDoc2(objectBean);
            // 一级嵌套
            Document document = new Document(objKey, d);
//        collection.updateOne();
            collection.insertOne((T) document);

        }
        return true;
    }

    /**
     *  指定资产 插入 data 数据 (key 相同时 更新  value)
     * @param map data 数据参数
     * @param assetId  资产 id, 如果为 Null 则  插入所有
     * @param collectionName  集合名称（数据表）
     * @param jsonKey kv,其中 v 为 json ，要插入的 数据参数
     * @return
     */
    @Override
    public String updateAssetData(Map map, String assetId, String collectionName, String jsonKey) {
        Query query = new Query();
        if(assetId != null) {
            query.addCriteria(Criteria.where("_id").is(assetId));
        }
        Update update = new Update();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            // 获取key
            String key = jsonKey + "." + entry.getKey();
            // 获取value
            String value = entry.getValue();
            update.set(key, value);
        }
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, collectionName);
        return updateResult.toString();
    }

    /**
     *  查看当前数据库 某类型 数据表/ collection
     *  不包括统计表： 例如 asset_statistics
     * @param type 类型 assert：资产
     * @return
     */
    @Override
    public Set selectContainCollection(String type) {
        Set<String> CollectionList = mongoTemplate.getCollectionNames();
        Set<String> result = new HashSet<>();
        for (String name : CollectionList) {
            if (name.startsWith(type)) {
                result.add(name.split("_")[1]);
            }
        }
        result.remove("statistics");
        return result;
    }

    @Override
    public List<String> selectAssetSetNameList() {
        MongoCollection<Document> collection  = mongoTemplate.getCollection("asset_statistics");
        FindIterable<Document> documents = collection.find()
                .projection(
                        new Document("assetSetName", 1).append("_id", 0)
                );
        List<String> arrayList = new ArrayList<>();
        Iterator<Document> iterator = documents.iterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();
            arrayList.add(document.getString("assetSetName"));
        }
        return arrayList;
    }

    /**
     * 更新资产统计表 asset_statistics
     * @param parameter 更新参数
     * @param assetName 资产名称
     * @param id 资产id
     * @return
     */
    @Override
    public String updateAssetStatistics(Document parameter , String assetName, String id) {
        MongoCollection mongoCollection = mongoTemplate.getCollection("asset_statistics");
        Document documentFilter = new Document();
        documentFilter.put("name", assetName);
        FindIterable<Document> documents = mongoCollection.find(documentFilter);
        /**
         * assetName 存在 ——更新
         *          不存在 ——插入
         */
        if (documents.iterator().hasNext()) {
             System.out.println(mongoCollection.updateOne(documentFilter, parameter).toString());
            return "更新";
        } else {
            mongoCollection.insertOne(parameter);
            return "新增";
        }
    }

    /**
     * 查询collection document数量
     * @param parameterMap 查询条件参数
     * @param collectionName 数据表名称
     * @return document count
     */
    @Override
    public long selectDocumentCount(Map parameterMap, String collectionName) {
        if (parameterMap == null || parameterMap.size() == 0) {
            return mongoTemplate.getCollection(collectionName).countDocuments();
        }
        Document queryDocument = new Document();
        queryDocument.putAll(parameterMap);
        return mongoTemplate.getCollection(collectionName).countDocuments(queryDocument);

    }

    /**
     * 根据表名查询所有
     * @param collectionName
     * @return
     */
    @Override
    public String queryAll(String collectionName) {

        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents = collection.find();
        StringBuffer stringBuffer = new StringBuffer("[");
        Iterator<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            stringBuffer.append(document.toJson());
            if(iterator.hasNext()){
                stringBuffer.append(",");
            }

        }
        stringBuffer.append("]");
        return  stringBuffer.toString();
    }


    /**
     *  分页查询
     * @param collectionName
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public JSONArray queryPerPages(String collectionName, Integer pageSize, Integer page, Map queryMap) {
        Document queryDocument = new Document();
        if (queryMap != null){
            queryDocument.putAll(queryMap);
        }
        MongoCollection<Document> collection  = mongoTemplate.getCollection(collectionName);
        FindIterable<Document> documents = collection.find(queryDocument).skip((page-1)*pageSize).limit(pageSize);
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
        return  jsonArray;
    }


}
