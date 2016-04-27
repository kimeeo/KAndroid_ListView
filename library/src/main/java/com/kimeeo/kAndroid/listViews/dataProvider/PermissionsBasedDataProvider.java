package com.kimeeo.kAndroid.listViews.dataProvider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.kimeeo.kAndroid.core.permissions.PermissionsHelper;

import java.util.ArrayList;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class PermissionsBasedDataProvider<T> extends DataProvider<T>
{
    @Override
    public boolean next()
    {
        if(hasPermission())
            super.next();
        else
        {
            PermissionListener permissionListener = new PermissionListener()
            {
                @Override
                public void onPermissionGranted() {
                    PermissionsBasedDataProvider.super.next();
                }
                @Override
                public void onPermissionDenied(ArrayList<String> arrayList) {
                    dataLoadError("NO_PERMISSION");
                }
            };
            invokePermission(permissionListener);
        }
        return false;
    }
    @Override
    public boolean refresh()
    {
        if(hasPermission())
            super.refresh();
        else
        {
            PermissionListener permissionListener = new PermissionListener()
            {
                @Override
                public void onPermissionGranted() {
                    PermissionsBasedDataProvider.super.refresh();
                }
                @Override
                public void onPermissionDenied(ArrayList<String> arrayList) {
                    dataLoadError("NO_PERMISSION");
                }
            };
            invokePermission(permissionListener);
        }
        return false;
    }





    private PermissionsHelper permissionsHelper;
    Context context;
    public PermissionsBasedDataProvider(Context context) {
        permissionsHelper=createPermissionsHelper(context);
        this.context=context;
    }


    public boolean requiredPermission(String[] permissions) {
        return permissions != null && permissions.length != 0;
    }
    public void invokePermission(PermissionListener permissionListener) {
        invokePermission(requirePermissions(), getFriendlyPermissionsMeaning(), permissionListener);
    }

    public void invokePermission(String[] permissions, String[] friendlyPermissionsMeaning, PermissionListener permissionListener) {
        if (requiredPermission(permissions)) {
            if (hasPermission(permissions))
                permissionListener.onPermissionGranted();
            else {
                permissionsHelper.setOnPermission(permissionListener);
                permissionsHelper.check(permissions, friendlyPermissionsMeaning);
            }
        }
    }
    protected PermissionsHelper createPermissionsHelper(Context context) {
        PermissionsHelper permissionsHelper = new PermissionsHelper(context);
        permissionsHelper.setShowRationaleConfirm(getShowRationaleConfirm());
        permissionsHelper.setRationaleConfirmText(getRationaleConfirmText(context));
        permissionsHelper.setRationaleMessage(getRationaleMessage(context));
        permissionsHelper.setShowDeniedMessage(getShowDeniedMessage());
        permissionsHelper.setDeniedCloseButtonText(getDeniedCloseButtonText(context));
        permissionsHelper.setDeniedMessage(getDeniedMessage(context));
        return permissionsHelper;
    }
    protected String getDeniedCloseButtonText(Context context) {
        return context.getString(com.kimeeo.kAndroid.core.R.string._permission_denied_close_button_text);
    }

    protected String getRationaleConfirmText(Context context) {
        return context.getString(com.kimeeo.kAndroid.core.R.string._permission_rationale_confirm_text);
    }

    protected String getRationaleMessage(Context context) {
        return context.getString(com.kimeeo.kAndroid.core.R.string._permission_rationale_message);
    }

    protected String getDeniedMessage(Context context) {
        return context.getString(com.kimeeo.kAndroid.core.R.string._permission_denied_message);
    }

    protected boolean getShowDeniedMessage() {
        return false;
    }

    protected boolean getShowRationaleConfirm() {
        return false;
    }

    public String[] requirePermissions() {
        return null;
    }
    public String[] getFriendlyPermissionsMeaning() {
        return null;
    }
    public boolean hasPermission(String[] permissions) {
        boolean has = true;
        if (permissions != null && permissions.length != 0) {
            for (int i = 0; i < permissions.length; i++) {
                int result = ContextCompat.checkSelfPermission(context, permissions[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    has = false;
                    break;
                }
            }
        }
        return has;
    }
    public boolean hasPermission() {
        return hasPermission(requirePermissions());
    }
}
