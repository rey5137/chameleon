package com.rey.chameleon.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rey.chameleon.ChameleonContextWrapper;
import com.rey.chameleon.theme.ThemeManager;

/**
 * Created by Rey on 10/26/2016.
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChameleonContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeManager.getInstance().setCurrentTheme(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeManager.getInstance().setCurrentTheme(ThemeManager.getInstance().getCurrentTheme() == 0 ? 1 : 0);
            }
        });

    }
}
