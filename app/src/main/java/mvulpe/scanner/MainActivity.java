package mvulpe.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;


import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import mvulpe.scanner.Reader.BarcodeCaptureActivity;


public class MainActivity extends AppCompatActivity {

    TextView decText, decContent;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private ShareActionProvider mShareActionProvider;
    Toolbar myToolbar;
    static final String DECODED_TEXT = "decodedText";
    private String decodedText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decText = (TextView) findViewById(R.id.decText);
        decContent = (TextView) findViewById(R.id.decContent);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setVisibility(View.GONE);

        if(savedInstanceState!=null){
            decodedText=savedInstanceState.getString(DECODED_TEXT);
        }

        if(!decodedText.equals("")){
            decText.setVisibility(View.VISIBLE);
            decContent.setVisibility(View.VISIBLE);
            myToolbar.setVisibility(View.VISIBLE);
            decContent.setText(decodedText);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString(DECODED_TEXT,decodedText);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void history(View v){
        if(v.getId()==R.id.bHistory){
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu2, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_item_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareText)+decContent.getText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share to"));
                setShareIntent(sendIntent);
                break;
            default:
                break;
        }
        return true;
    }


    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    //product qr code mode
    public void scanQR(View v) {
        if (v.getId() == R.id.bScan) {

            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);

        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    myToolbar.setVisibility(View.VISIBLE);

                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
                    decodedText = barcode.displayValue;
                    decContent.setText(barcode.displayValue);
                    decText.setVisibility(View.VISIBLE);
                    decContent.setVisibility(View.VISIBLE);

                    String textToWrite = getCurrentTimeDate()+" "+decodedText+"\n";


                    FileOutputStream outputStream;
                    String file = "results";
                    try{
                        outputStream = openFileOutput(file, Context.MODE_APPEND);
                        outputStream.write(textToWrite.getBytes());
                        outputStream.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
//                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                decText.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static String getCurrentTimeDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

}
