package com.mc.eam.repairs;

import com.alibaba.fastjson.JSONObject;
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

        String kk = "{ \"_id\" : { \"$oid\" : \"60370841c56b845f2181dc58\" }, \"date\" : { \"$date\" : 1614238455199 }, \"s1\" : 1, \"s2\" : {\n" +
                "\n" +
                "\"test\": \"asdasd\"} }\n" +
                "\n";
        System.out.println(Document.parse(kk).get("_id"));
//        String a = "601cf3004703714b00d00875";
//        ObjectId objectId = new ObjectId(a);
//        String k = "{\"forecast\":[{\"id\": 4},{\"id\": 5},{\"id\": 6}]}";
//
//        Document document = Document.parse(k);
//        ArrayList<Document> arrayList = (ArrayList<Document>) document.get("forecast");
//        System.out.println(arrayList.toString());
//        System.out.println(arrayList.size());
//        System.out.println(arrayList.get(1));

//        Map<String, Object> map = null;
////        map.put("a", "a");
////        map.put("b.c", "123");
////        JSONObject json = new JSONObject(map);
//        System.out.println(Document.parse(JSONObject.toJSONString(map)));
//        System.out.println(JSONObject.toJSONString(map));

//        System.out.println(json.toJSONString());

//
//        for (Document d: arrayLists) {
//            String finalValue = keyArray[keyArray.length-1];
//            if (d.containsKey(finalValue)) {
//                valueList.add(d.getString(finalValue));
//            }
//         }
//        System.out.println(valueList.toString());

//        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//
//        System.out.println(ObjectId.isValid(a));
//        System.out.println(objectId.toString());
//        System.out.println(localDateTime);
//
//        a = ObjectId.createFromLegacyFormat(localDateTime.getSecond(),
//                objectId.getMachineIdentifier(),
//                3).toString();
//        System.out.println(a);
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

}
