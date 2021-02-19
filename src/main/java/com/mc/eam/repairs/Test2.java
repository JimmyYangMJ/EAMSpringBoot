package com.mc.eam.repairs;


import com.alibaba.excel.EasyExcel;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import com.mc.eam.repairs.util.excel.*;

import java.io.IOException;
import java.util.Iterator;

@Component
@SpringBootTest
@RunWith(SpringRunner.class) // 使用测试class 所需的 注入，创建Spring的应用上下文
public class Test2 {

    @Autowired
    private MongoAssetDaoImpl mongoCRUD;

    @Autowired
    private ExcelListener excelListener;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() throws IOException {
        MongoCollection<Document> collection  = mongoTemplate.getCollection("test_data");
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
        System.out.println(stringBuffer.toString());

//        Query query = new Query();
//        query.addCriteria(Criteria.where("name").is("647台阿里索电维修2") );
//
//        Update update = new Update();
//        update.set("key", "value");

//        UpdateResult updateResult = mongoTemplate.save(query, update, "asset_statistics");
//        System.out.println(updateResult);

//        Query query2 = new Query();
//        query.addCriteria(Criteria.where("name").is("647台阿里索电维修")
//                .orOperator(Criteria.where("_id").is("123")));
//
//        Update update2 = new Update();
//        update.set("key", "value");
//
//        UpdateResult updateResult2 = mongoTemplate.(query2, update2, "asset_statistics");
//        System.out.println(updateResult);

    }

    @Test
    public void update() {

        MongoDatabase mongoDatabase  = mongoTemplate.getDb();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("60124738a092642696faa379"));
        Update update = new Update();
        update.set("data.key1", "value");
        update.set("data.key2", "value");
        mongoTemplate.updateFirst(query,update,"647台阿里索电维修_assert");

    }
    @Test
    public  <T> void  test3() {
        System.out.println(mongoCRUD.getClass());
        System.out.println(mongoCRUD);
        String fileD = "C:\\Users\\dell\\Desktop\\MC资产管理系统EAM\\test2\\原始文件.xlsx";
        EasyExcel.read(fileD, excelListener).sheet("Sheet1").doRead();

//        Map properties = new HashMap();
//        properties.put("idNum", Class.forName("java.lang.Integer"));
//        properties.put("name", Class.forName("java.lang.String"));
//        properties.put("address", Class.forName("java.lang.String"));
//        Object stu = GenerateObjectUtil.generateObject(properties);
//        GenerateObjectUtil.setValue(stu, "idNum", 123);
//        GenerateObjectUtil.setValue(stu, "name", "454");
//        GenerateObjectUtil.setValue(stu, "address", "789");
////

//        mongoCRUD.insert(stu);
    }

}
/*
class com.mc2.eam.repairs.dao.MongoCRUD
com.mc2.eam.repairs.dao.MongoCRUD@5f36c8e3
* */