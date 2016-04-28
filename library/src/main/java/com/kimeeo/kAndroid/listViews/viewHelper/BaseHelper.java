package com.kimeeo.kAndroid.listViews.viewHelper;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.kimeeo.kAndroid.listViews.DefaultErrorHandler;
import com.kimeeo.kAndroid.listViews.IEmptyViewHandler;
/**
 * Created by bpa001 on 3/14/16.
 */
abstract public class BaseHelper implements IEmptyViewHandler {
    IEmptyViewHandler errorHandler = new DefaultErrorHandlerLocal(this);
    abstract public Resources getResources();
    abstract public void retry();
    public Drawable getEmptyViewDrawable() {
        return errorHandler.getEmptyViewDrawable();
    }
    public String getEmptyViewMessage() {
        return errorHandler.getEmptyViewMessage();
    }
    public Drawable getInternetViewDrawable() {
        return errorHandler.getInternetViewDrawable();
    }
    public String getInternetViewMessage() {
        return errorHandler.getInternetViewMessage();
    }
    public class DefaultErrorHandlerLocal extends DefaultErrorHandler {
        BaseHelper baseHelper;
        public DefaultErrorHandlerLocal(BaseHelper baseHelper) {
            this.baseHelper = baseHelper;
        }
        public Resources getResources() {
            return baseHelper.getResources();
        }
        public void retry() {
            baseHelper.retry();
        }
    }
}
