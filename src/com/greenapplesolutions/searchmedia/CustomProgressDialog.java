package com.greenapplesolutions.searchmedia;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context,R.style.CustomProgressDialog);
    }

}