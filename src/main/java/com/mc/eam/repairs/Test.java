package com.mc.eam.repairs;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author ymj
 * @Date： 2021/1/29 13:30
 * @description:
 */
public class Test {
    public static void main(String[] args) {
        String a = "601cf3004703714b00d00875";
        ObjectId objectId = new ObjectId(a);
        String k = "{\"forecast\":[{\"id\": 4},{\"id\": 5},{\"id\": 6}]}";

        Document document = Document.parse(k);
        ArrayList<Document> arrayList = (ArrayList<Document>) document.get("forecast");
        System.out.println(arrayList.toString());
        System.out.println(arrayList.size());
        System.out.println(arrayList.get(1));

        String k2 = "{\"forecast\":[{\"id\": 4},{\"id\": 5},{\"id\": 6}]}";
        System.out.println("**********");
        Document documents = Document.parse(k2);
        Object arrayLists =  documents.get("forecast");
        System.out.println(arrayLists.toString());
        System.out.println(arrayLists.getClass().getName());
        System.out.println(ArrayList.class.getName());
        System.out.println("**********");


        Date date = objectId.getDate();

        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        System.out.println(ObjectId.isValid(a));
        System.out.println(objectId.toString());
        System.out.println(localDateTime);

        a = ObjectId.createFromLegacyFormat(localDateTime.getSecond(),
                objectId.getMachineIdentifier(),
                3).toString();
        System.out.println(a);
    }

}
