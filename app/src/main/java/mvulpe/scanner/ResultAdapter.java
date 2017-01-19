package mvulpe.scanner;

import android.app.Activity;
import android.content.Context;
import android.icu.util.Output;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Mihai on 06/01/2017.
 */
public class ResultAdapter extends ArrayAdapter<ScanResult> {

    private Context context;
    private List<ScanResult> results;
    private int layoutResID;
    private ResultAdapter me = this;
    private TextView statusText;

    public ResultAdapter(Context context, int layoutResourceID, List<ScanResult> results) {
        super(context, layoutResourceID, results);
        this.context = context;
        this.results = results;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tIndex = (TextView) view.findViewById(R.id.tIndex);
            itemHolder.tName = (TextView) view.findViewById(R.id.tName);
            itemHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            itemHolder.tDate = (TextView) view.findViewById(R.id.tDate);
            itemHolder.tTime = (TextView) view.findViewById(R.id.tTime);
            itemHolder.tDelete = (ImageButton) view.findViewById(R.id.tDelete);
            statusText = (TextView) view.findViewById(R.id.tStatus);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final ScanResult hItem = results.get(position);

        itemHolder.tIndex.setText(String.valueOf(position + 1)+". ");
        itemHolder.tName.setText(hItem.getContent());
        itemHolder.tDate.setText("Date: " + hItem.getTimestamp().substring(0, 10));
        itemHolder.tTime.setText("Time: " + hItem.getTimestamp().substring(11));
        itemHolder.tDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputStream inputStream = context.openFileInput("results");
                    OutputStream outputStream = context.openFileOutput("tempResults",Context.MODE_APPEND);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

                    String lineToRemove = hItem.getTimestamp();
                    String currentLine;

                    while ((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if (trimmedLine.startsWith(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    results.remove(hItem);
                    me.notifyDataSetChanged();
                    statusText.setText("Found " + results.size() + " scan results.");
                    writer.close();
                    reader.close();

                    File f1 = context.getFileStreamPath("results");
                    File f2 = context.getFileStreamPath("tempResults");

                    boolean successful = f2.renameTo(f1);

                    Toast.makeText(context,"Successfully deleted entry",Toast.LENGTH_LONG).show();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private static class ItemHolder {
        TextView tIndex;
        TextView tName;
        RelativeLayout lHeader;
        TextView tDate, tTime;
        ImageButton tDelete;
    }
}