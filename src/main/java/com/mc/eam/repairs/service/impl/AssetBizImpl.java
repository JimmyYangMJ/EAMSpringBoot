package com.mc.eam.repairs.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;
import com.mc.eam.repairs.service.AssetBiz;
import com.mc.eam.repairs.util.FileUtil;
import com.mc.eam.repairs.util.excel.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/25 17:37
 * @description: 文件处理
 */
@Service
public class AssetBizImpl implements AssetBiz {

    @Autowired
    private ExcelListener excelListener;

    @Autowired
    private MongoAssetDaoImpl mongoCRUDDao;

    /**
     * 保存文件
     * @param file
     * @return 是否成功
     */
    @Override
    public boolean saveFile(MultipartFile file, String filePath) {
        String fileName = file.getOriginalFilename(); // 获取文件名
        fileName = "原始文件.xlsx";
        filePath = "C:\\Users\\dell\\Desktop\\MC资产管理系统EAM\\test2\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            System.out.println("success");
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return false;
    }


    /**
     * excel 文件数据 保存 至 Mongo DB 中
     * @param mFile 上传的 excel 文件
     * @param assetSetName 资产表名称
     * @param requestMap 请求体 参数
     * @return 是否插入成功
     * @throws Exception
     */
    @Override
    public boolean excelToMongo(MultipartFile mFile, String assetSetName, Map requestMap) throws Exception {
        // 更新 数据表名/document
        mongoCRUDDao.collectionName = "asset_" + assetSetName;

        File file = FileUtil.multipartFileToFile(mFile);
        EasyExcel.read(file, excelListener).sheet("Sheet1").doRead();

        /** 更新资产统计表 asset_statistics
         *    name : 647台阿里索电维修
             department：部门
             charge：负责人
             count：数量
             default_flow： 默认流程
         */
        Document document = new Document();
        document.put("count", mongoCRUDDao.selectDocumentCount(null, "asset_" + assetSetName));
        Iterator<Map.Entry<String, String[]>> iter = requestMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String[]> entry = iter.next();
            document.put(entry.getKey(), entry.getValue()[0]);
        }
        mongoCRUDDao.updateAssetStatistics(document, assetSetName, null);
        return true;
    }

    /**
     * 分页查询
     * @param collectionName
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public ServerResponse<JSONObject> queryAssetPerPages(String collectionName, Integer pageSize, Integer page)  {
        JSONArray dataList = mongoCRUDDao.queryPerPages(collectionName, pageSize, page);
        long dataSum = mongoCRUDDao.selectDocumentCount(null, collectionName);
        if(dataSum == 0 ){
            return ServerResponse.createByErrorMessage("没有相应记录");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sum", dataSum);
        jsonObject.put("dataList", dataList);
        System.out.println(jsonObject.toJSONString());
        System.out.println(jsonObject.toString());
        return  ServerResponse.createBySuccess("查询成功", jsonObject);
    }


}
