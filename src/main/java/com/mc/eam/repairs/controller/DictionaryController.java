package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.service.FlowBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.web.bind.annotation.*;

/**
 * @author ymj
 * @Date：
 * @description: 数据字典
 */
@RestController
@RequestMapping("dictionary")
public class DictionaryController {



    /**
     * 数据字典 根据中文 查询 英文
     * @param key
     * @return todo
     */
    @GetMapping(value = "ddValue.do")
    public String dataDictionaryValue(@RequestParam("key") String key ) {
        return null;
    }

    /**
     * 数据字典 根据【英文】 查询 【中文】
     * @param key
     * @return todo
     */
    @GetMapping(value = "ddKey.do")
    public String dataDictionaryKey(@RequestParam("value") String key ) {
        return null;
    }

    /**
     * 新增数据字典
     * @param key  Chinese
     * @param value English
     * @param type 数据类型 eg: String(default) ,Integer
     * @return todo
     */
    @PutMapping (value = "ddKV.do")
    public String dataDictionaryKV(String key, String value, String type) {
        return null;
    }

    /**
     * 数据字典 查询
     * @return todo
     */
    @GetMapping(value = "dd")
    public String dataDictionary() {
        return null;
    }

    /**
     *  数据字典 分页查询
     * @param pageSize 每页大小
     * @param page 页码
     * @return todo
     */
    @GetMapping(value = "ddPage")
    public String dataDictionary(Integer pageSize, Integer page) {
        return null;
    }

}
