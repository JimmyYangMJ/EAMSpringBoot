package com.mc.eam.repairs.util;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ymj
 * @Date： 2021/1/26 12:15
 * @description: 生成OBJ
 */
public class GenerateObjectUtil {

    public static Object getValue(Object obj, String property) {
        BeanMap beanMap = BeanMap.create(obj);
        return beanMap.get(property);
    }

    public static void setValue(Object obj, String property, Object value) {
        BeanMap beanMap = BeanMap.create(obj);
        beanMap.put(property, value);
    }

    public static Object generateObject(Map properties) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = properties.keySet();
        for(Iterator i = keySet.iterator(); i.hasNext();) {
            String key = (String)i.next();
            generator.addProperty(key, (Class)properties.get(key));
        }
        return generator.create();
    }
}
