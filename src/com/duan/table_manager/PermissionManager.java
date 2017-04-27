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
 * Created by DuanJiaNing on 2017/4/21.
 */
public class PermissionManager implements DBControl<PermissionManager.Permission> {

    @Override
    public boolean insert(Permission... permissions) {
        final Connection conn = DBMalwareHelper.getConnection();
        final Statement[] statement = {null};
        Arrays.stream(permissions).forEach(permission -> {
            boolean isNew = true;
            String type = permission.getType();
            String name = permission.getName();
            String sql = "INSERT " + DBMalwareHelper.TABLE_PERMISSION + " VALUES (NULL,'" + type + "','" + name + "');";
            try {
                statement[0] = conn.createStatement();
                statement[0].executeUpdate(sql);

            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    isNew = false;
                    ComPrint.error(name + " 已存在于数据库中");
                } else {
                    e.printStackTrace();
                }
            }
            if (isNew)
                ComPrint.info("新增 " + type + "\t" + name);

        });
        DBUtil.closeSetStatConn(conn, null, statement[0]);
        return true;

    }


    public static class Permission {
        final static String ID = "id";
        final static String TYPE = "mtype";
        final static String NAME = "name";

        private int id;
        private String name;
        private String type;

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Permission))
                return false;
            Permission per = (Permission) obj;
            return per.getName().equals(name) && per.getType().equals(type);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
