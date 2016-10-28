package com.rey.chameleon.theme;

import android.view.View;

/**
 * Created by Rey on 10/26/2016.
 */
public interface Styler<V extends View> {

    void applyStyle(V view, int styleRes);

}
