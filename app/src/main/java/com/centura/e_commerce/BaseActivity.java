package com.centura.e_commerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    Snackbar snackbar;


    public void snackbar_(CoordinatorLayout coordinatorLayout, String msg) {
        snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setActionTextColor(Color.WHITE).show();
    }





}
