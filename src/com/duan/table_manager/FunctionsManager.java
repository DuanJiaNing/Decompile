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
    public boolean insert(Function... functions) {
        final Connection conn = DBMalwareHelper.getConnection();
        final Statement[] statement = {null};
        Arrays.stream(functions).forEach(fun -> {
            boolean isNew = true;
            String clasS = fun.getClasS();
            String signature = fun.getSignature();
            float count = fun.getRatio();
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
                ComPrint.info("新增 " + type + "\t类:" + clasS + "\t签名:" + signature + "\t比例:" + count);

        });
        DBUtil.closeSetStatConn(conn, null, statement[0]);
        return true;
    }

    public static class Function {

        final static String ID = "id";
        final static String CLASS = "class";
        final static String SIGNATURE = "signature";
        final static String RATIO = "ratio";
        final static String TYPE = "mtype";

        private String clasS;
        private String signature;
        //创建对象了就一定有一次，插入到数据库前作为计数使用，插入数据库时转换为百分比
        private float ratio = 1.0f;
        private String type;

        /**
         * 格式化方法签名
         *
         * @param fName      方法名
         * @param fParameter 方法参数
         * @param fReturn    字符串格式为：所属类名 返回值类型 方法名 (参数1类型 参数2类型 ...)
         */
        public void setSignature(String fName, String fParameter[], String fReturn) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(fReturn).append(" ").append(fName).append("(");
            Arrays.stream(fParameter).forEach(str -> buffer.append(str).append(" "));
            if (fParameter.length != 0)
                buffer.append("\b");
            buffer.append(")");
            this.signature = buffer.toString();
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Function))
                return false;
            Function fun = (Function) obj;
            return fun.getClasS().equals(clasS) && fun.getSignature().equals(signature) && fun.getType().equals(type);
        }

        @Override
        public String toString() {
            return "Function{" +
                    "clasS='" + clasS + '\'' +
                    ", signature='" + signature + '\'' +
                    ", count=" + ratio +
                    ", type='" + type + '\'' +
                    '}';
        }

        public void setClasS(String clasS) {
            this.clasS = clasS;
        }

        public void setRatio(float count) {
            this.ratio = count;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClasS() {
            return clasS;
        }

        public String getSignature() {
            return signature;
        }

        public float getRatio() {
            return ratio;
        }

        public String getType() {
            return type;
        }
    }

}
