package com.duan;

import com.duan.common.ComPrint;
import com.duan.db.DBControl;
import com.duan.db.DBMalwareHelper;

/**
 * Created by DuanJiaNing on 2017/4/19.
 * *
 */
public class Main {

    public static void main(String[] args) {


    }

    /**
     * ************已完成**************
     *
     * 数据库预处理 ，该方法将依次完成以下任务
     * 1.将磁盘中的恶意软件样本集数据添加（刷新）到 tb_samples 表中
     * 2.将恶意软件样本集进行反编译，提取 AndroidManifest 文件和 smail 文件到指定文件夹下
     * 3.提取每一个恶意软件反编译得到的 AndroidManifest 文件中申请的权限
     * 4.提取每一个恶意软件反编译得到的 smail 文件中调用到的 Android SDK 方法信息
     */
    private static void init() {

        DatabasePrepare prepare = new DatabasePrepare();

        // 1.
//        prepare.initSamplesTable();

        // 2.
//        prepare.decompil();

        // 3.
//        prepare.analysisManifest();

        // 4.
//        prepare.analysisSmail();

    }


}
