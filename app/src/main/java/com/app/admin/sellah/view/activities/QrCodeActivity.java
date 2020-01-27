package com.app.admin.sellah.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.admin.sellah.view.fragments.PaymentFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PaymentFragment.qr_scan_id="";

        scanBarcodeCustomLayout();


    }



    public void scanBarcodeCustomLayout() {
        IntentIntegrator integrator = new IntentIntegrator(this);
      //  integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
       // integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
      //  finish();

      /*  new IntentIntegrator(this).initiateScan();
        finish();*/

      //  new IntentIntegrator(this).initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {

              //  Log.e("ResultPrint: ","Null");
                finish();
            } else {

             //   Log.e("ResultPrint: ",result.getContents());
                PaymentFragment.qr_scan_id = result.getContents();
                finish();
            }

        }
    }

}
