package com.duan.decompile;

import com.duan.common.ComPrint;
import com.duan.common.ComString;
import com.duan.common.FileUtil;
import com.duan.table_manager.SamplesManager;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DuanJiaNing on 2017/4/19.
 * 调用 decompileApk** 方法进行反编译
 */
public class Decompiler {

    private final int THREADSUM = 10;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREADSUM);

    private SamplesManager mSamplesManager;

    //反编译结束是否提取 androidMinefist 和 Smail 文件
    private boolean mExtract = true;
    //提取文件结束是否删除编译结果文件
    private boolean mDeleteTemp = true;

    public Decompiler(boolean mExtract, boolean mDeleteTemp) {
        this.mExtract = mExtract;
        this.mDeleteTemp = mDeleteTemp;
        this.mSamplesManager = new SamplesManager();
    }

    public void decompileALL(String whichType) {
        SamplesManager.Samples samples[] = mSamplesManager.getSampleInfo(whichType);
        for (SamplesManager.Samples s : samples) {
            ComPrint.normal(s.getId() + " " + s.getPackageName() + " 开始反编译...");
            executeTasks(s);
        }

    }

    public void decompileApk(int[] apkIds) {
        for (int id : apkIds) {
            SamplesManager.Samples s = mSamplesManager.getSampleInfo(id);
            ComPrint.normal(s.getId() + " " + s.getPackageName() + " 正在反编译...");
            executeTasks(s);
        }
    }

    public void decompileApk(int apkId) {
        SamplesManager.Samples sample = mSamplesManager.getSampleInfo(apkId);
        ComPrint.normal(apkId + " " + sample.getPackageName() + " 正在反编译...");
        executeTasks(sample);
    }

    /*
     * 通过线程池控制开启Windows进程进行反编译
     * @param id 数据库中保存的 apk 的编号
     */
    private void executeTasks(SamplesManager.Samples samples) {
        //断定 pn 和 pp 都会获得正确的值
        int id = samples.getId();
        String pn = samples.getPackageName();
        String pp = samples.getPath();
        String tempP = ComString.DECOMPILE_TEMP_PATH + pn;
        String cmd = ComString.APKTOOL_PATH + ComString.APKTOOL_OPTION_F + pp + ComString.APKTOOL_OPTION_O + tempP;
        String type = samples.getType();
        File file = new File(tempP);
        if (file.exists() && file.isDirectory()) {
            decompileFinish(tempP, pn, id, type);
        } else {
            Runnable runnable = () -> {
                try {

                    Process process = Runtime.getRuntime().exec(cmd);
                    handlError(process.getErrorStream(), pn, id);
                    handlMessage(process.getInputStream());
                    process.waitFor();
                    decompileFinish(tempP, pn, id, type);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            fixedThreadPool.execute(runnable);
        }
    }

    //处理错误流
    private void handlError(InputStream errorStream, String pn, int id) {
        InputStreamReader reader = new InputStreamReader(errorStream);
        new Thread(() -> {
            int co = 0;
            char[] msg = new char[1024];
            int len = 0;
            try {
                while ((len = (reader.read(msg, 0, msg.length))) > 0) {
                    if (co++ < 3) {
//                        ComPrint.error(id + " " + pn + " 反编译出错: " + new String(msg, 0, len));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //消费输出流，不使进程阻塞
    private void handlMessage(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        new Thread(() -> {
            try {
                while ((reader.readLine()) != null) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*
     * 提取 AndroidManifest.xml 文件和 Smail 文件到 decompile\包名 目录下
     * @param tempPath 编译生成文件临时保存路径
     */
    private void extract(String tempPath, String pn, int id, String type) {
        try {

            FileUtil.move(tempPath + "\\smali\\com", ComString.DECOMPILE_PATH + type + "\\" + pn + "\\com", true);
            FileUtil.move(tempPath + "\\" + ComString.ANDROIDMANIFEST, ComString.DECOMPILE_PATH + type + "\\" + pn + "\\" + ComString.ANDROIDMANIFEST, false);

        } catch (Exception e) {
            e.printStackTrace();
            ComPrint.error(id + " " + pn + " 文件提取失败！");
        }
    }

    /*
     * 文件反编译完成，提取文件，任务完成
     * @param tempPath 编译生成文件临时保存路径
     * @param pn 包名
     * @param id 数据库中保存的 apk 的编号
     */
    private void decompileFinish(String tempPath, String pn, int id, String type) {
        synchronized (pn) {
            String apk = id + " " + pn + " ";
            ComPrint.info(apk + "反编译完成");
            if (mExtract) {
                ComPrint.normal(apk + "开始提取文件...");
                extract(tempPath, pn, id, type);
                ComPrint.info(apk + "文件提取完成");
            }
            if (mDeleteTemp) {
                ComPrint.normal(apk + "开始清除临时文件...");
                deleteTemp(new File(tempPath));
                ComPrint.info(apk + "临时文件已清除");
            }
        }
    }

    //删除编译和提取过程产生的临时文件
    private void deleteTemp(File tempF) {
        if (tempF.exists()) {
            if (tempF.isFile()) {
                tempF.delete();
            } else if (tempF.isDirectory()) {
                File[] files = tempF.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.deleteTemp(files[i]);
                }
                tempF.delete();
            }
        } else {
            ComPrint.error("所删除的文件不存在");
        }
    }

}
