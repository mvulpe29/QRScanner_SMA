package mvulpe.scanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mihai on 06/01/2017.
 */
public class ResultAdapter extends ArrayAdapter<ScanResult> {

    private Context context;
    private List<ScanResult> results;
    private int layoutResID;

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

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final ScanResult hItem = results.get(position);

        itemHolder.tIndex.setText(String.valueOf(position + 1));
        itemHolder.tName.setText(hItem.getContent());
        itemHolder.tDate.setText("Date: " + hItem.getTimestamp().substring(0, 10));
        itemHolder.tTime.setText("Time: " + hItem.getTimestamp().substring(11));

        return view;
    }

    private static class ItemHolder {
        TextView tIndex;
        TextView tName;
        RelativeLayout lHeader;
        TextView tDate, tTime;
    }
}