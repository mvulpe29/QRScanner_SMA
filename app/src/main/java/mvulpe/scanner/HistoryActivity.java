package mvulpe.scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ListView listResults;
    private ArrayList<ScanResult> results = new ArrayList<ScanResult>();
    private ResultAdapter adapter;
    private TextView tStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tStatus = (TextView) findViewById(R.id.tStatus);

        listResults = (ListView) findViewById(R.id.listResults);
        adapter = new ResultAdapter(this, R.layout.item, results);

        try {
            InputStream inputStream = openFileInput("results");
            if(inputStream!=null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    Log.d("StackOverflow", line);
                    String timestamp = line.substring(0,"yyyy-MM-dd HH:mm:ss".length());
                    String content = line.substring("yyyy-MM-dd HH:mm:ss".length()+1);
                    results.add(new ScanResult(timestamp,content));
                    line = reader.readLine();
                }

            }
            adapter.notifyDataSetChanged();
            tStatus.setText("Found " + results.size() + " scan results.");
        }catch (FileNotFoundException e) {
            Log.e("history activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("history activity", "Can not read file: " + e.toString());
        }
        listResults.setAdapter(adapter);

//        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AppState.get().setCurrentPayment(payments.get(i));
//                startActivity(new Intent(getApplicationContext(), AddActivity.class));
//            }
//        });
    }
}
