package com.mc.eam.repairs.service;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/29 17:27
 * @description: 资产
 */
public interface AssetBiz {

    /**
     * 保存文件
     * @param file
     * @return 是否成功
     */
    boolean saveFile(MultipartFile file, String filePath);

    /**
     * excel 文件数据 保存 至 Mongo DB 中
     * @param mFile 上传的 excel 文件
     * @param assetSetName 资产表名称
     * @param requestMap 请求体 参数
     * @return 是否插入成功
     * @throws Exception
     */
    boolean excelToMongo(MultipartFile mFile, String assetSetName, Map requestMap) throws Exception;


    ServerResponse<JSONObject> queryAssetPerPages(String collectionName, Integer pageSize, Integer page) throws JSONException;
}
