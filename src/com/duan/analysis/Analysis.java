package com.duan.analysis;

import com.duan.common.ComPrint;
import com.duan.table_manager.SamplesManager;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DuanJiaNing on 2017/4/24.
 * *
 */
public abstract class Analysis {

    private SamplesManager mSamplesManager;

    public Analysis() {
        this.mSamplesManager = new SamplesManager();
    }

    private SamplesManager.Samples[] getInfo(String whichType) {
        return mSamplesManager.getSampleInfo(whichType);
    }

    private SamplesManager.Samples[] getInfo(int... ids) {
        return mSamplesManager.getSampleInfo(ids);
    }

    abstract File getFile(SamplesManager.Samples sample);

    abstract String getRegex(SamplesManager.Samples sample);

    abstract void find(Matcher m, SamplesManager.Samples sample);

    public void analysis(String whictype) {
        SamplesManager.Samples samples[] = getInfo(whictype);
        Arrays.stream(samples).forEach(bean -> ergodicFile(getFile(bean),bean));
    }

    public void analysis(int... id) {
        SamplesManager.Samples samples[] = getInfo(id);
        Arrays.stream(samples).forEach(bean -> ergodicFile(getFile(bean),bean));
    }

    private void ergodicFile(File tempF,SamplesManager.Samples bean) {
        if (tempF.exists()) {
            if (tempF.isFile()) {
                matcher(tempF,bean);
            } else if (tempF.isDirectory()) {
                File[] files = tempF.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.ergodicFile(files[i],bean);
                }
                tempF.delete();
            }
        } else {
            ComPrint.error("文件不存在");
        }
    }

    private void matcher(File file,SamplesManager.Samples bean) {
        BufferedReader reader = null;
        try {

            FileInputStream is = new FileInputStream(file);
            InputStreamReader ir = new InputStreamReader(is);
            reader = new BufferedReader(ir);

            String line = null;
            Pattern p = Pattern.compile(getRegex(bean));
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    find(m, bean);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
