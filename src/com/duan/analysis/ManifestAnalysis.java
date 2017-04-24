package com.duan.analysis;

import com.duan.common.ComString;
import com.duan.db.DBControl;
import com.duan.table_manager.PermissionManager;
import com.duan.table_manager.SamplesManager;

import java.io.File;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * Created by DuanJiaNing on 2017/4/21.
 * *
 */
public class ManifestAnalysis extends Analysis implements DBControl<PermissionManager.Permission> {

    private PermissionManager mPermissionManager;

    private HashSet<PermissionManager.Permission> hashSet;

    public ManifestAnalysis() {
        this.mPermissionManager = new PermissionManager();
        hashSet = new HashSet<>();
    }

    @Override
    File getFile(SamplesManager.Samples samples) {
        return new File(ComString.DECOMPILE_PATH + samples.getType() + "\\" + samples.getPackageName() + "\\" + ComString.ANDROIDMANIFEST);
    }

    @Override
    String getRegex(SamplesManager.Samples samples) {
        return ComString.REGEX_PERMISSION;
    }

    @Override
    void find(Matcher m, SamplesManager.Samples bean) {
        PermissionManager.Permission per = new PermissionManager.Permission();
        per.setType(bean.getType());
        per.setName(m.group(1));
        hashSet.add(per);
    }

    @Override
    public boolean insertToDB(PermissionManager.Permission... ts) {
        return mPermissionManager.insertToDB(ts);
    }

}
