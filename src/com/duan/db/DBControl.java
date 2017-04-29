package com.duan.db;

import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by DuanJiaNing on 2017/4/24.
 *
 * @param <B> 数据库表对应实体类
 */
@FunctionalInterface
public interface DBControl<B> {

    /**
     * 插入数据到数据库中
     *
     * @param ts 数据项
     * @return 插入成功返回 true
     */
    boolean insert(B... ts);


    /**
     * 获得表中指定类型软件的数据总数
     * 有且只有在指定表为 tb_samples 时获取到的数量为指定软件类样本的总数
     * @param whichType 类型
     * @return 数量
     */
    static int countByType(String whichType, String whichTable) {
        Connection conn = DBMalwareHelper.getConnection();
        ResultSet set = null;
        Statement sta = null;
        final String sql = "SELECT COUNT(*) FROM " + whichTable + " WHERE mtype LIKE '" + whichType + "'";
        int count = 0;
        try {
            sta = conn.createStatement();
            set = sta.executeQuery(sql);

            while (set.next())
                count += set.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeSetStatConn(conn, set, sta);
        }
        return count;
    }
}
