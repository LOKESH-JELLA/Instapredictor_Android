package example.android.astrofivepagerapp;

import android.app.ProgressDialog;
import android.content.Context;


public class Progressing {

    public static ProgressDialog showProgressDialog(ProgressDialog pDialog, Context context) {
        pDialog.setMessage("Please Wait...");
        pDialog.setTitle(context.getResources().getString(R.string.app_name));
        pDialog.setCancelable(false);
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
        return pDialog;
    }

    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
        pDialog.hide();


    }
}
