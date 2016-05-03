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

    private String[] filters;

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
    protected String refreshPath()
    {
        return null;
    }

    public boolean isFileList()
    {
        return true;
    }

    @Override
    protected void invokeLoadNext() {
        String path =nextPath();
        if(path!=null)
            loadListing(path,false);
        else {
            dataLoadError(null);
        }
    }

    @Override
    protected void invokeLoadRefresh() {
        String path =refreshPath();
        if(path!=null)
            loadListing(path,false);
        else {
            dataLoadError(null);
        }
    }

    protected void loadListing(String path, boolean isFetchingRefresh) {
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
                    if (getFilters()!=null && getFilters().length!=0)
                    {
                        if(file[i].getName().indexOf(".")!=-1)
                        {
                            String ext = file[i].getName().substring(file[i].getName().lastIndexOf(".")+1,file[i].getName().length());
                            for (String s : getFilters()) {
                                if(s.equals(ext))
                                {
                                    data.add(file[i]);
                                    break;
                                }
                            }
                        }
                    }
                    else
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
        } else {
            dataLoadError("NOT_FOUND");
        }
    }


    public String[] getFilters() {
        return filters;
    }


    public void setFilters(String[] filters) {
        this.filters = filters;
    }
}
