package com.duan.table_manager;

import com.duan.common.ComPrint;
import com.duan.db.DBControl;
import com.duan.db.DBMalwareHelper;
import com.duan.db.DBUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 管理 samples 表
 */
public class SamplesManager implements DBControl<SamplesManager.Samples> {

    /**
     * 刷新数据库中 samples 表中指定软件类型
     *
     * @param whichtype 类型在{@link DBMalwareHelper}中定义
     * @param cleanOut  刷新表前是否先清空
     */
    public void checkType(String whichtype, boolean cleanOut) {

        if (cleanOut)
            cleanOut(whichtype);

        File file = new File("D:\\Decompile\\testApks\\" + whichtype);
        File[] pgs = file.listFiles();
        int len = pgs.length;
        if (len == 0) {
            ComPrint.normal(whichtype + " 文件夹内没有数据！");
            return;
        }
        Samples[] samples = new Samples[len];
        for (int i = 0; i < len; i++) {
            samples[i].setType(whichtype);
            samples[i].setPackageName(pgs[i].getName());

            // 取的时候若显示 \ 后的内容会被转义 ，直接用 OK
            //注意 \\ 和 \ 的转换
            //pgs[i].getAbsolutePath() 里有 \ 的会被保存为 \\ ，所以 用 \\\\ 截取
            String[] pt = pgs[i].getAbsolutePath().split("\\\\");
            StringBuilder path = new StringBuilder();
            for (String s : pt)
                // 在后面追加 \\\\ 实际打印出来就是 \\ ，保存时是 \
                path.append(s + "\\\\");
            // 保存时最后有个多余的 \ ，保存是 \\，去掉
            samples[i].setPath(new String(path.toString().toCharArray(), 0, path.length() - 2));

        }

        boolean su = insertToDB(samples);
        ComPrint.normal(DBMalwareHelper.TABLE_SAMPLES + " 表  " + whichtype + " 类数据刷新" + (su ? "成功" : "失败"));
    }

    //清空 samples 表中指定类型
    private static void cleanOut(String whichtype) {
        final Connection conn = DBMalwareHelper.getConnection();
        Statement statement = null;

        String sql = "DELETE FROM " + DBMalwareHelper.TABLE_SAMPLES + " WHERE " + Samples.TYPE + " LIKE '" + whichtype + "'";
        ComPrint.info(DBMalwareHelper.TABLE_SAMPLES + " 表中 " + whichtype + "类数据已清空");

        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeSetStatConn(conn, null, statement);
        }

    }

    @Override
    public boolean insertToDB(Samples... samples) {
        final Connection conn = DBMalwareHelper.getConnection();
        Statement statement = null;
        for (int i = 0; i < samples.length; i++) {
            boolean isNew = true;
            String pn = samples[i].getPackageName();
            String path = samples[i].getPath();
            String type = samples[i].getType();

            String sql = "INSERT " + DBMalwareHelper.TABLE_SAMPLES + " VALUES(NULL,'" + pn + "','" + type + "','" + path + "');";

            try {
                statement = conn.createStatement();
                statement.executeUpdate(sql);

            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {//重复插入 跳过
                    ComPrint.error(pn + " 已存在于数据库中");
                    isNew = false;
                } else {
                    e.printStackTrace();
                    return false;
                }
            }
            if (isNew)
                ComPrint.info("新增 " + type + "\t" + pn);
        }
        DBUtil.closeSetStatConn(conn, null, statement);
        return true;
    }

    /**
     * 获得指定 id 的apk信息
     *
     * @param id id
     * @return 信息
     */
    public Samples getSampleInfo(int id) {
        Samples samples = new Samples();
        Connection conn = DBMalwareHelper.getConnection();
        String sql = "SELECT * FROM " + DBMalwareHelper.TABLE_SAMPLES + " WHERE " + Samples.ID + " = " + id;
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = conn.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()) {
                samples.setId(id);
                samples.setPackageName(set.getString(Samples.PACKAGE));
                samples.setType(set.getString(Samples.TYPE));
                samples.setPath(set.getString(Samples.PATH));
            }

            return samples;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.closeSetStatConn(conn, set, statement);
        }
    }

    /**
     * 获得指定 类型的所有 的apk信息
     *
     * @param whichType 类型
     * @return 信息
     */
    public Samples[] getSampleInfo(String whichType) {
        ArrayList<Samples> list = new ArrayList<>();
        Connection conn = DBMalwareHelper.getConnection();
        String sql = "SELECT * FROM " + DBMalwareHelper.TABLE_SAMPLES + " WHERE " + Samples.TYPE + " LIKE " + "'" + whichType + "'";
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = conn.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()) {
                Samples samples = new Samples();
                samples.setId(set.getInt(Samples.ID));
                samples.setPackageName(set.getString(Samples.PACKAGE));
                samples.setType(set.getString(Samples.TYPE));
                samples.setPath(set.getString(Samples.PATH));
                list.add(samples);
            }
            int i = 0;
            Samples[] samples = new Samples[list.size()];
            for (Samples sa : list)
                samples[i++] = sa;
            return samples;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.closeSetStatConn(conn, set, statement);
        }
    }

    /**
     * 获得指定 id 的apk信息
     *
     * @param ids ids
     * @return 信息
     */
    public Samples[] getSampleInfo(int[] ids) {
        Samples samples[] = new Samples[ids.length];
        Connection conn = DBMalwareHelper.getConnection();
        StringBuilder builder = new StringBuilder();
        Arrays.stream(ids).filter(i -> i != ids[0]).forEach(i -> builder.append(" OR " + Samples.ID + " = " + i));
        String sql = "SELECT * FROM " + DBMalwareHelper.TABLE_SAMPLES + " WHERE " + Samples.ID + " = " + ids[0] + builder.toString();
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = conn.createStatement();
            set = statement.executeQuery(sql);
            int cc = 0;
            while (set.next()) {
                samples[cc].setId(set.getInt(Samples.ID));
                samples[cc].setPackageName(set.getString(Samples.PACKAGE));
                samples[cc].setType(set.getString(Samples.TYPE));
                samples[cc].setPath(set.getString(Samples.PATH));
                cc++;
            }

            return samples;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.closeSetStatConn(conn, set, statement);
        }
    }


    public static class Samples {
        static final String ID = "sample_id";
        static final String PACKAGE = "sample_package_name";
        static final String TYPE = "sample_type";
        static final String PATH = "sample_path";

        private int id;
        private String packageName;
        private String type;
        private String path;

        public void setId(int id) {
            this.id = id;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getType() {
            return type;
        }

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            return "Samples{" +
                    "id=" + id +
                    ", packageName='" + packageName + '\'' +
                    ", type='" + type + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }

}
