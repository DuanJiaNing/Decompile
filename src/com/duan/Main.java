package com.duan;

import com.duan.analysis.ManifestAnalysis;
import com.duan.db.DBMalwareHelper;
import com.duan.decompile.Decompiler;
import com.duan.table_manager.SamplesManager;

/**
 * Created by DuanJiaNing on 2017/4/19.
 * *
 */
public class Main {

    public static void main(String[] args) {

//        initSamplesTable();

        new Decompiler(false, false).decompileApk(8);

//        new ManifestAnalysis(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW).analysis();

    }


    //初始化 sample 表
    private static void initSamplesTable() {
        SamplesManager manager = new SamplesManager();
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_SEX_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_OTHER_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_TEST_SW, false);

    }
}
