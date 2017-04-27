package com.duan.analysis;

/**
 * Created by DuanJiaNing on 2017/4/27.
 * 解析操作入口方法规则定义
 */
public interface Analysisor {

    /**
     * 根据给定 id 解析对应文件
     * @param id 样本在数据库中的id
     */
    void analysisById(int id);

    /**
     * 根据给定 id 解析对应文件
     * @param ids 样本在数据库中的id
     */
    void analysisByIds(int...ids);

    /**
     * 对给定软件类型下的所有软件进行解析
     * @param whichType 类型定义见{@link com.duan.db.DBMalwareHelper}
     */
    void analysisByType(String whichType);
}
