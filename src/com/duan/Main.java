package com.duan;

import com.duan.analysis.ManifestAnalysis;
import com.duan.analysis.SmailAnalysis;
import com.duan.common.ComPrint;
import com.duan.common.ComString;
import com.duan.db.DBMalwareHelper;
import com.duan.decompile.Decompiler;
import com.duan.table_manager.PermissionManager;
import com.duan.table_manager.SamplesManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DuanJiaNing on 2017/4/19.
 * *
 */
public class Main {

    public static void main(String[] args) {

//        initSamplesTable();

//        new Decompiler(true, true).decompileApk(new int[]{8,7});

//        analysisManifest(7,8);

        analysisSmail(8);


    }

    private static void test() {
        Pattern p = Pattern.compile(ComString.REGEX_FUNCTIONS);
        Matcher m = p.matcher("   invoke-static {v6, v7}, Landroid/widget/View;->getXXX(Ljava/lang/String;I)Ljava/lang/Integer;");
        while (m.find()) {
            ComPrint.error(m.groupCount() + " "+m.group(4) + " " + m.group(1) + "." + m.group(2) + "(" + m.group(3) + ")");
        }
    }

    //从 smail 中提取出方法调用信息
    private static void analysisSmail(int... ids) {
        SmailAnalysis analysis = new SmailAnalysis();
        analysis.analysis(ids);
    }

    //从 AndroidManifest 文件中提取权限
    private static void analysisManifest(int... ids) {
        ManifestAnalysis analysis = new ManifestAnalysis();
        analysis.analysis(ids);
        analysis.insertToDB(analysis.getHashSet().toArray(new PermissionManager.Permission[0]));
    }

    //初始化 sample 表
    private static void initSamplesTable() {
        SamplesManager manager = new SamplesManager();
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_SEX_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_OTHER_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_TEST_SW, true);

    }
}
