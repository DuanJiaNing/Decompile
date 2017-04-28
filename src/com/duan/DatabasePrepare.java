package com.duan;

import com.duan.analysis.PermissionAnalysis;
import com.duan.analysis.FunctionsAnalysis;
import com.duan.db.DBControl;
import com.duan.db.DBMalwareHelper;
import com.duan.decompile.Decompiler;
import com.duan.table_manager.FunctionsManager;
import com.duan.table_manager.PermissionManager;
import com.duan.table_manager.SamplesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by DuanJiaNing on 2017/4/27.
 * 数据库预处理
 */
//TODO 待测试
public final class DatabasePrepare {


    // 1.将磁盘中的恶意软件样本集数据添加（刷新）到 tb_samples 表中
    void initSamplesTable() {
        SamplesManager manager = new SamplesManager();
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_SEX_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW, true);
        manager.checkType(DBMalwareHelper.MALWARE_TYPE_OTHER_SW, true);

    }

    // 2.将恶意软件样本集进行反编译，提取 AndroidManifest 文件和 smail 文件到指定文件夹下
    void decompil() {
        new Decompiler(true, true).decompileALL(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW);
        new Decompiler(true, true).decompileALL(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW);
        new Decompiler(true, true).decompileALL(DBMalwareHelper.MALWARE_TYPE_SEX_SW);
        new Decompiler(true, true).decompileALL(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW);
        new Decompiler(true, true).decompileALL(DBMalwareHelper.MALWARE_TYPE_OTHER_SW);
    }

    // 3.提取每一个恶意软件反编译得到的 AndroidManifest 文件中申请的权限
    void analysisManifest() {
        HashSet<PermissionManager.Permission> permissions = new HashSet<>();
        PermissionAnalysis analysis = new PermissionAnalysis();
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW);
        permissions.addAll((Collection<? extends PermissionManager.Permission>) analysis.getContainerSet());

        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW);
        permissions.addAll((Collection<? extends PermissionManager.Permission>) analysis.getContainerSet());

        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_SEX_SW);
        permissions.addAll((Collection<? extends PermissionManager.Permission>) analysis.getContainerSet());

        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW);
        permissions.addAll((Collection<? extends PermissionManager.Permission>) analysis.getContainerSet());

        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_OTHER_SW);
        permissions.addAll((Collection<? extends PermissionManager.Permission>) analysis.getContainerSet());

        analysis.insertToDB(permissions.toArray(new PermissionManager.Permission[0]));

    }

    // 4.提取每一个恶意软件反编译得到的 smail 文件中调用到的 Android SDK 方法信息
    void analysisSmail() {
        FunctionsAnalysis analysis = new FunctionsAnalysis();
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_PHISHING_SW);
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_RANSOMWARE_SW);
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_SEX_SW);
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_TROJAN_VIRUS_SW);
        analysis.analysisByType(DBMalwareHelper.MALWARE_TYPE_OTHER_SW);

    }

    /**
     * 每次解析一个软件类型下的所有 smail 文件，list 中每一个元素（Function）的 count 字段
     * 表示该方法在该软件类型样本集中总的出现的次数（每一个 apk 中出现次数之和）
     * 因而取其平均值
     */
    //TODO 待测试
    private void avgTimesOfFunction(ArrayList<FunctionsManager.Function> res, String type, String table) {
        res.forEach(fun -> fun.setCount(fun.getCount() / DBControl.countType(type, table)));
    }

}
