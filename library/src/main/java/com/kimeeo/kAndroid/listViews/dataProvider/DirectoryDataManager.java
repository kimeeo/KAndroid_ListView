package com.kimeeo.kAndroid.listViews.dataProvider;

import android.Manifest;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 28/04/16.
 */
abstract public class DirectoryDataManager extends PermissionsBasedDataProvider{

    public DirectoryDataManager(Context context)
    {
        super(context);
    }
    @Override
    public String[] requirePermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }
    @Override
    public String[] getFriendlyPermissionsMeaning() {
        return new String[]{"Storage"};
    }

    abstract protected String nextPath();
    abstract protected String refreshPath();

    public boolean isFileList()
    {
        return true;
    }

    @Override
    protected void invokeLoadNext() {
        loadListing(nextPath(),false);
    }

    @Override
    protected void invokeLoadRefresh() {
        loadListing(refreshPath(),true);
    }

    protected void loadListing(String path,boolean isFetchingRefresh) {
        File directory = new File(path);
        File file[] = null;
        if (directory != null && directory.exists() && directory.isDirectory())
            file = directory.listFiles();

        if (file != null && file.length != 0) {
            if (isFetchingRefresh)
                removeAll(this);

            if (isFileList()) {
                List<File> data = new ArrayList<>();
                for (int i = 0; i < file.length; i++) {
                    data.add(file[i]);
                }
                addData(data);
            } else {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < file.length; i++) {
                    data.add(file[i].getAbsolutePath());
                }
                addData(data);
            }
        }
        else
        {
            dataLoadError("NOT_FOUND");
        }
    }


}
