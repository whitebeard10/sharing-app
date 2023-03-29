package com.example.vit_share;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

public class RequestListAdapter extends BaseAdapter {

    private ArrayList<Request> requestList;

    public RequestListAdapter(ArrayList<Request> requestList) {
        this.requestList = requestList;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
        }

        Request request = requestList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);

        titleTextView.setText(request.getTitle());
        descriptionTextView.setText(request.getDescription());

        if (request.getAcceptedBy() == -1) {
            statusTextView.setText("Open");
        } else {
            statusTextView.setText("Accepted by user " + request.getAcceptedBy());
        }

        return convertView;
    }
}
