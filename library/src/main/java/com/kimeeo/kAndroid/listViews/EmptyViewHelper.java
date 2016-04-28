package com.kimeeo.kAndroid.listViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kimeeo.kAndroid.core.utils.NetworkUtilities;

import java.util.List;

/**
 * Created by bpa001 on 3/14/16.
 */
public class EmptyViewHelper {
    protected ImageView mEmptyViewImage;
    protected TextView mEmptyViewMessage;
    protected View mRetry;
    protected IEmptyViewHandler mEmptyViewHandler;
    protected View mEmptyView;
    protected Context context;
    private boolean showInetnetError;
    private boolean showRetryButton;

    public EmptyViewHelper(Context context, View emptyView, IEmptyViewHandler emptyViewHandler, boolean showInetnetError, boolean showRetryButton) {
        this.context = context;
        if(emptyView!=null)
        {
            mEmptyView = emptyView;
            this.showInetnetError = showInetnetError;
            this.showRetryButton = showRetryButton;
            this.mEmptyViewHandler = emptyViewHandler;
            if (emptyView.findViewById(R.id.emptyViewImage) != null && emptyView.findViewById(R.id.emptyViewImage) instanceof ImageView) {
                mEmptyViewImage = (ImageView) emptyView.findViewById(R.id.emptyViewImage);
                mEmptyViewImage.setImageDrawable(mEmptyViewHandler.getEmptyViewDrawable());
            }

            if (emptyView.findViewById(R.id.emptyViewMessage) != null && emptyView.findViewById(R.id.emptyViewMessage) instanceof TextView) {
                mEmptyViewMessage = (TextView) emptyView.findViewById(R.id.emptyViewMessage);
                mEmptyViewMessage.setText(mEmptyViewHandler.getEmptyViewMessage());
            }
            if (emptyView.findViewById(R.id.retry) != null && emptyView.findViewById(R.id.retry) instanceof TextView) {
                mRetry = emptyView.findViewById(R.id.retry);
                mRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEmptyViewHandler.retry();
                    }
                });
            }
            if (emptyView != null)
                emptyView.setVisibility(View.GONE);
        }
    }

    public void clean() {
        mEmptyView = null;
        if (mEmptyViewImage != null)
            mEmptyViewImage.setImageBitmap(null);
        mEmptyViewImage = null;
        mEmptyViewMessage = null;
    }

    public void updateView(List dataManager) {
        if (mEmptyView != null && dataManager != null) {
            if (dataManager.size() == 0) {
                if (showInetnetError && NetworkUtilities.isConnected(context) == false) {
                    if (mEmptyViewImage != null)
                        mEmptyViewImage.setImageDrawable(mEmptyViewHandler.getInternetViewDrawable());
                    if (mEmptyViewMessage != null)
                        mEmptyViewMessage.setText(mEmptyViewHandler.getInternetViewMessage());
                    if (mRetry != null) {
                        if (showRetryButton)
                            mRetry.setVisibility(View.VISIBLE);
                        else
                            mRetry.setVisibility(View.GONE);
                    }
                } else {
                    if (mEmptyViewImage != null)
                        mEmptyViewImage.setImageDrawable(mEmptyViewHandler.getEmptyViewDrawable());
                    if (mEmptyViewMessage != null)
                        mEmptyViewMessage.setText(mEmptyViewHandler.getEmptyViewMessage());
                    if (mRetry != null)
                        mRetry.setVisibility(View.GONE);
                }
                mEmptyView.setVisibility(View.VISIBLE);
            } else
                mEmptyView.setVisibility(View.GONE);
        }
    }

    public void updatesStart() {
        if (mEmptyView != null)
            mEmptyView.setVisibility(View.GONE);
    }
}
