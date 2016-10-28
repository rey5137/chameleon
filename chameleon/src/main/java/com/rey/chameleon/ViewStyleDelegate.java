package com.rey.chameleon;

import android.support.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Rey on 10/28/2016.
 */
class ViewStyleDelegate implements View.OnAttachStateChangeListener, ThemeManager.OnThemeChangedListener{

    private int mStyleId;
    private WeakReference<View> mViewRef;
    private int mCurrentStyle = ThemeManager.STYLE_UNDEFINED;

    ViewStyleDelegate(View view, int styleId){
        mViewRef = new WeakReference<>(view);
        mStyleId = styleId;
    }

    @Override
    public void onThemeChanged(@Nullable OnThemeChangedEvent event) {
        View view = mViewRef.get();
        if(view == null)
            return;
        int style = ThemeManager.getInstance().getCurrentStyle(view.getContext(), mStyleId);
        if(mCurrentStyle != style){
            mCurrentStyle = style;
            ThemeManager.getInstance().applyStyle(view, mCurrentStyle);
        }
    }

    @Override
    public void onViewAttachedToWindow(View view) {
        if(mStyleId != 0) {
            ThemeManager.getInstance().registerOnThemeChangedListener(this);
            onThemeChanged(null);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View view) {
        if(mStyleId != 0)
            ThemeManager.getInstance().unregisterOnThemeChangedListener(this);
    }

}
