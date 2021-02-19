package com.mc.eam.repairs.util;

import org.bson.Document;
import java.lang.reflect.Field;
/**
 * @author ymj
 * @Date： 2021/1/28 10:53
 * @description: 转换工具类
 */
public class ConvertUtil {

    /**
     * 对象转换为Document (普通 Bean Obj 转为 Document)
     * @param o  对象
     * @return Document
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Document convertDoc(Object o)
            throws IllegalArgumentException, IllegalAccessException {
        Document document = new Document();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            document.append(field.getName(), field.get(o));
        }
        return document;
    }

    /**
     * 对象转换为Document (Spring Bean Obj 转为 Document)
     * @param o  对象
     * @return Document
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Document convertDoc2(Object o)
            throws IllegalArgumentException, IllegalAccessException {
        Document document = new Document();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            document.append(field.getName().substring(12), field.get(o));
        }
        return document;
    }

}
