package com.duan.analysis;

import com.duan.common.ComPrint;
import com.duan.common.ComString;
import com.duan.db.DBControl;
import com.duan.table_manager.FunctionsManager;
import com.duan.table_manager.SamplesManager;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * Created by DuanJiaNing on 2017/4/24.
 * *
 */
//TODO 完善该类
public class SmailAnalysis extends Analysis implements DBControl<FunctionsManager.Function> {

    private FunctionsManager mFunctionsManager;

    private HashSet<FunctionsManager.Function> hashSet;

    public HashSet<FunctionsManager.Function> getHashSet() {
        return hashSet;
    }

    public SmailAnalysis() {
        this.mFunctionsManager = new FunctionsManager();
        hashSet = new HashSet<>();
    }

    @Override
    File getFile(SamplesManager.Samples sample) {
        return new File(ComString.DECOMPILE_PATH + sample.getType() + "\\" + sample.getPackageName() + "\\com");
    }

    @Override
    String getRegex(SamplesManager.Samples sample) {
        return ComString.REGEX_FUNCTIONS;
    }

    @Override
    void find(Matcher m, SamplesManager.Samples sample) {
        if (m.groupCount() != 4)
            return;
        String fClass = m.group(1);
        String fName = m.group(2);
        String fPar = m.group(3);
        String fReaturn = m.group(4);
        String fpars[] = null, temp[];

        //返回值类型
        if (fReaturn.charAt(0) == 'L')
            fReaturn = fReaturn.substring(1, fReaturn.length() - 1);

        //参数类型
        if (fPar.contains(";")) {
            String[] strs = fPar.split(";");
            temp = new String[strs.length];
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].charAt(0) == 'L')
                    strs[i] = strs[i].substring(1);
                temp[i] = strs[i];
            }
            fpars = temp;
        } else if (!fPar.contains("/")) {
            char[] chars = fPar.toCharArray();
            int j = 0;
            temp = new String[chars.length];
            for (int i = 0; i < chars.length; i++) {
                if (
                        chars[i] == 'V' ||
                                chars[i] == 'Z' ||
                                chars[i] == 'B' ||
                                chars[i] == 'S' ||
                                chars[i] == 'C' ||
                                chars[i] == 'I' ||
                                chars[i] == 'J' ||
                                chars[i] == 'F' ||
                                chars[i] == 'D'
                        ) {
                    temp[j++] = chars[i] + "";
                }
            }

            if (j < chars.length) {
                fpars = new String[j];
                for (int i = 0; i < j; i++) {
                    fpars[i] = temp[i];
                }
            } else
                fpars = temp;
        }
        String[] fPre = new String[fpars.length];
        for (int i = 0; i < fpars.length; i++) {
            fPre[i] = fpars[i].replace("/",".");
        }
        String value = FunctionsManager.Function.getSignature(
                fClass.replace("/", "."),
                fName.replace("/", "."),
                fPre,
                fReaturn.replace("/", "."));
        ComPrint.error(value);
    }

    @Override
    public boolean insertToDB(FunctionsManager.Function... ts) {
        return true;
    }

}
