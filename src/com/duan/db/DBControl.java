package com.duan.db;

/**
 * Created by DuanJiaNing on 2017/4/24.
 * @param <B> 数据库表对应实体类
 */
public interface DBControl<B> {

    /**
     * 插入数据到数据库中
     * @param ts 数据项
     * @return 插入成功返回 true
     */
    boolean insertToDB(B... ts);
}
