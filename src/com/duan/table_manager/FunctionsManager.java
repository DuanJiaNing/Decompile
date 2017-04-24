package com.duan.table_manager;

import com.duan.common.ComPrint;
import com.duan.db.DBControl;
import com.duan.db.DBMalwareHelper;
import com.duan.db.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Created by DuanJiaNing on 2017/4/24.
 */
public class FunctionsManager implements DBControl<FunctionsManager.Function> {

    @Override
    public boolean insertToDB(Function... functions) {
        final Connection conn = DBMalwareHelper.getConnection();
        final Statement[] statement = {null};
        Arrays.stream(functions).forEach(fun -> {
            boolean isNew = true;
            String clasS = fun.getClasS();
            String signature = fun.getSignature();
            int count = fun.getCount();
            String type = fun.getType();
            String sql = "INSERT " + DBMalwareHelper.TABLE_FUNCTIONS + " VALUES (NULL,'" + clasS + "','" + signature + "'," + count + ",'" + type + "');";
            try {
                statement[0] = conn.createStatement();
                statement[0].executeUpdate(sql);

            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    isNew = false;
                    ComPrint.error(type + " " + signature + " 已存在于数据库中");
                } else {
                    e.printStackTrace();
                }
            }
            if (isNew)
                ComPrint.info("新增 " + type + "\t" + signature);

        });
        DBUtil.closeSetStatConn(conn, null, statement[0]);
        return true;
    }

    public static class Function {

        final static String ID = "function_id";
        final static String CLASS = "function_class";
        final static String SIGNATURE = "function_signature";
        final static String COUNT = "function_count";
        final static String TYPE = "function_type";

        private int id;
        private String clasS;
        private String signature;
        private int count;
        private String type;

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Function))
                return false;
            Function fun = (Function) obj;
            return fun.getType().equals(type) && fun.getSignature().equals(signature);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setClasS(String clasS) {
            this.clasS = clasS;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getClasS() {
            return clasS;
        }

        public String getSignature() {
            return signature;
        }

        public int getCount() {
            return count;
        }

        public String getType() {
            return type;
        }
    }

}
