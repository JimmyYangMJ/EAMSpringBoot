package com.mc.eam.repairs;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;
import com.mongodb.Block;
import com.mongodb.ClientSessionOptions;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import com.mc.eam.repairs.util.excel.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


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

//        MongoDbFactory mongoDbFactory = mongoTemplate.getMongoDbFactory();
//        ClientSession clientSession =  mongoDbFactory.getSession();
//        clientSession.startTransaction();
//
//        MongoCollection<Document> collection  = mongoTemplate.getCollection("testCollection");
//        Document document = new Document("name", "Café Con Leche");
//        collection.insertOne(document);
//
//        clientSession.abortTransaction();

//        FindIterable<Document> documents = collection.find();
//        StringBuffer stringBuffer = new StringBuffer("[");
//        Iterator<Document> iterator = documents.iterator();
//        while (iterator.hasNext()) {
//            Document document = iterator.next();
//            stringBuffer.append(document.toJson());
//            if(iterator.hasNext()){
//                stringBuffer.append(",");
//            }
//
//        }
//        stringBuffer.append("]");
//        System.out.println(stringBuffer.toString());

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
    public void file() {
//        // Create a gridFSBucket with a custom bucket name "files"
//        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb(),"files2");
////        GridFSBucket gridFSFilesBucket = GridFSBuckets.create(, "files");
//        try {
//            InputStream streamToUploadFrom = new FileInputStream(new File("数据字典.xls"));
//            // Create some custom options
//            GridFSUploadOptions options = new GridFSUploadOptions()
//                    .chunkSizeBytes(358400)
//                    .metadata(new Document("type", "presentation"));
//
//            ObjectId fileId = gridFSBucket.uploadFromStream("数据字典5 Mongo", streamToUploadFrom, options);
//            System.out.println(fileId);
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//            // handle exception
//        }

        // 查询
        GridFSBucket gridFSBuckets = GridFSBuckets.create(mongoTemplate.getDb(),"files2");
        gridFSBuckets.find().forEach(
                new Block<GridFSFile>() {
                    public void apply(final GridFSFile gridFSFile) {
                        System.out.println(gridFSFile.getFilename());
                        System.out.println(gridFSFile.getId());
                        System.out.println(gridFSFile.getObjectId());
                    }
                });
        ObjectId fileId = new ObjectId("6037445b6f22e52f706c57b9");

        try {
            FileOutputStream streamToDownloadTo = new FileOutputStream("Mongo测试用");
            gridFSBuckets.downloadToStream(fileId, streamToDownloadTo);
            streamToDownloadTo.close();
            System.out.println(streamToDownloadTo.toString());

        } catch (IOException e) {
            // handle exception
        }
    }

    @Test
    public void update() {

        MongoDatabase mongoDatabase  = mongoTemplate.getDb();

        LocalDateTime rightNow   =   LocalDateTime.now  ();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("6037080ac56b845f2181dc39"));
        Update update = new Update();

        update.set("datas.int", 5);
        mongoTemplate.updateFirst(query, update,"testCollection");

    }
    @Test
    public  void  test3() {
        MongoCollection collection  = mongoTemplate.getCollection("testCollection");
        Document filterDocument = new Document();
        filterDocument.put("_id",new ObjectId("60370841c56b845f2181dc58"));
        LocalDateTime rightNow  =   LocalDateTime.now  ();
        filterDocument.put("date", Date.from( rightNow.atZone(ZoneId.systemDefault()).toInstant()));
        filterDocument.put("s1", 1);
        filterDocument.put("s2", 1.2);
        System.out.println(filterDocument.toJson());
//        System.out.println(collection.deleteOne(filterDocument).getDeletedCount());
    }

    @Test
    public  void  select() {
        MongoCollection collection  = mongoTemplate.getCollection("testCollection");
        Document filterDocument = new Document();
        filterDocument.put("_id",new ObjectId("6037080ac56b845f2181dc39"));
        LocalDateTime rightNow   =   LocalDateTime.now  ();
        filterDocument.put("datas.key1.value", 5);
//        filterDocument.put("datas",new Document("data", Date.from( rightNow.atZone(ZoneId.systemDefault()).toInstant())));
        System.out.println(filterDocument.toJson());
        FindIterable<Document> documents= collection.find()
                    .filter(filterDocument)
                    .projection(
                            new Document().append("_id", 1)
                    );
        Iterator<Document> iterator = documents.iterator();

        while (iterator.hasNext()) {
            System.out.println("*:" + iterator.next().toJson());

        }
//        System.out.println(collection.deleteOne(filterDocument).getDeletedCount());
    }
    @Test
    public  void  mapToDocument() {

        HashMap hashMap = new HashMap();
        hashMap.put("_id",new ObjectId("60375d70c56b845f218244ce"));
        Document document = new Document(hashMap);
        MongoCollection collection  = mongoTemplate.getCollection("testCollection");
        System.out.println(collection.deleteMany(document).getDeletedCount());
    }


}
/*
class com.mc2.eam.repairs.dao.MongoCRUD
com.mc2.eam.repairs.dao.MongoCRUD@5f36c8e3
* */