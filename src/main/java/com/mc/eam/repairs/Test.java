package com.mc.eam.repairs;

import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

}
