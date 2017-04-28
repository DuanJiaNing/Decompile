package com.duan.analysis;

import com.duan.common.ComString;
import com.duan.table_manager.PermissionManager;
import com.duan.table_manager.SamplesManager;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * Created by DuanJiaNing on 2017/4/21.
 * *
 */
public class PermissionAnalysis extends Analysis{

    private final PermissionManager mPermissionManager;

    private final HashSet<PermissionManager.Permission> hashSet;

    public PermissionAnalysis() {
        this.mPermissionManager = new PermissionManager();
        hashSet = new HashSet<>();
    }

    @Override
    protected File getFile(SamplesManager.Samples samples) {
        return new File(ComString.DECOMPILE_PATH + samples.getType() + "\\" + samples.getPackageName() + "\\" + ComString.ANDROIDMANIFEST);
    }

    @Override
    protected String getRegex(SamplesManager.Samples samples) {
        return ComString.REGEX_PERMISSION;
    }

    @Override
    protected void find(Matcher m, SamplesManager.Samples bean) {
        PermissionManager.Permission per = new PermissionManager.Permission();
        per.setType(bean.getType());
        per.setName(m.group(1));
        //重复的会被过滤
        hashSet.add(per);
    }

    @Override
    public Collection<?> getContainerSet() {
        return hashSet;
    }

    public boolean insertToDB(PermissionManager.Permission... ts) {
        return mPermissionManager.insert(ts);
    }

}
