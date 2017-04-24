package com.duan.analysis;

import com.duan.common.ComString;
import com.duan.db.DBControl;
import com.duan.table_manager.FunctionsManager;
import com.duan.table_manager.SamplesManager;

import java.io.File;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * Created by DuanJiaNing on 2017/4/24.
 * *
 */
public class SmailAnalysis extends Analysis implements DBControl<FunctionsManager.Function>{

    private FunctionsManager mFunctionsManager;

    private HashSet<FunctionsManager.Function> hashSet;

    public SmailAnalysis() {
        this.mFunctionsManager = new FunctionsManager();
        hashSet = new HashSet<>();
    }

    @Override
    File getFile(SamplesManager.Samples sample) {
        return new File(ComString.DECOMPILE_PATH);
    }

    @Override
    String getRegex(SamplesManager.Samples sample) {
        return null;
    }

    @Override
    void find(Matcher m, SamplesManager.Samples sample) {

    }

    @Override
    public boolean insertToDB(FunctionsManager.Function... ts) {
        return false;
    }
}
