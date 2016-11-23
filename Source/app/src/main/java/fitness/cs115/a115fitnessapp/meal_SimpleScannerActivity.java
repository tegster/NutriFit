package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 11/8/16.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

//helper class to deal with barcode scan
public class meal_SimpleScannerActivity extends meal_BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private static final int PERMISSION_REQUEST_CODE = 200;

    private ZXingScannerView mScannerView;
    private final static boolean DEBUG = false;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        if(checkPermission()==false){//need to ask for permission
            requestPermission();
        }
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    //means that it has been scanned
    @Override
    public void handleResult(Result rawResult) {
        if (DEBUG) {
            Toast.makeText(this, "Contents = " + rawResult.getText() +
                    ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        }
        System.out.println("1337 Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString());

        //add to database here
        Intent intent = new Intent(meal_SimpleScannerActivity.this, meal_barCodeWebQuerry.class);
        intent.putExtra("barcode", rawResult.getText());
        startActivity(intent);

        this.finish();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(meal_SimpleScannerActivity.this);
            }
        }, 2000);
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if ( cameraAccepted){
                        //   Snackbar.make(, "Permission Granted, Now you can access camera.", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }
                    else {

                       // Snackbar.make(getCurrentFocus(), "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to the permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(meal_SimpleScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
