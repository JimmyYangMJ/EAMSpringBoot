package com.mc.eam.repairs;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
