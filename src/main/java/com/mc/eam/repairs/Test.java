package com.mc.eam.repairs;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ymj
 * @Date： 2021/1/29 13:30
 * @description:
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("collectionName".substring(4));
    }

    @org.junit.Test
    public  void  mapToDocument() {
        HashMap hashMap = new HashMap();
//        hashMap.put("as.sw","12");
        HashMap hashMap1 = new HashMap();
        hashMap1.put("ass", new ObjectId("6037080ac56b845f2181dc39"));
        hashMap.put("as", hashMap1);
        Document document = new Document(hashMap);
        System.out.println(document.toJson());
    }

    /** 多线程 */
    public void threadTest() throws InterruptedException {
        //使用原子变量只是为了将线程中的计算结果导出，与它的其余能力无关
        AtomicInteger thread1Result = new AtomicInteger(0);
        AtomicInteger thread2Result = new AtomicInteger(0);

        Thread thread1 = new Thread(() -> {
            int count = 3;
            thread1Result.set(count);
        });
        Thread thread2 = new Thread(() -> {
            int count = 4;
            thread2Result.set(count);
        });
        thread1.start();
        thread2.start();

        thread1.join(); // 主线程 等待，等该 thread1 结束
        thread2.join();

        System.out.println(thread1Result.get() + thread2Result.get());
    }

    /** 线程池 */
    public void ExecutorService() throws ExecutionException, InterruptedException {
        // 实际 应用中 可采用静态变量
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(() -> {
            int count = 1;
            return count;
        });
        tasks.add(() -> {
            int count = 3;
            return count;
        });

        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        int result = 0;
        for (int i = 0; i < futures.size(); i++) {
            result += futures.get(i).get();
        }
        executorService.shutdown();

        System.out.println(result);
    }

    @org.junit.Test
    public void saveObject() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put("ass", new ObjectId("6037080ac56b845f2181dc39"));
        ObjectOutputStream out = null;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("C:/output.txt");
            out = new ObjectOutputStream(fout);
            out.writeObject(hashMap);
        } finally {
            fout.close();
            out.close();
        }
    }

    // 读取对象，反序列化
    @org.junit.Test
    public void readObject() throws Exception {
        ObjectInputStream in = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("C:/output.txt");
            in = new ObjectInputStream(fin);
            Object object = in.readObject();
            System.out.println(object);

        } finally {
            fin.close();
            in.close();
        }
    }

    @org.junit.Test
    public void mongoTest()  {

        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection collection = database.getCollection("asset_statistics");

        String[] strings = {"q","754"};
//        Arrays.asList("Bakery", "Coffee", "Pastries")
//        Document filter = new Document("type", "step.plugins.functions.types.GeneralScriptFunction");
//        Document replacement = new Document("$set", new Document("type", "step.plugins.java.GeneralScriptFunction"));

        System.out.println(collection.updateMany(
                new Document("_id", new ObjectId("60418d337e6703294ab27b31")),
                new Document("$addToSet", new Document("dataDictionary", new Document("$each", Arrays.asList(strings))))
        ));
    }



}
