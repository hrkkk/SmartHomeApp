package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class BtDeviceDialog extends DialogFragment {
    private ListView listView;
    private List<BluetoothDevice> items;
    private CustomAdapter adapter;
    private HomeFragment.DialogCallback callback;
    public void setCallback(HomeFragment.DialogCallback callback) {
        this.callback = callback;
    }
    public BtDeviceDialog(List<BluetoothDevice> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_btdevice, null);
        builder.setView(dialogView);

        listView = dialogView.findViewById(R.id.listView); // 确保您的布局中有一个ListView
        adapter = new CustomAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        return builder.create();
    }
    public void updateListview(List<BluetoothDevice> newItems) {
        items.clear();
        items.addAll(newItems);
        adapter.notifyDataSetChanged();
        Log.i("info", "update");
    }
    // 自定义适配器，用于将数据绑定到ListView
    private class CustomAdapter extends ArrayAdapter<BluetoothDevice> {
        public CustomAdapter(Context context, List<BluetoothDevice> items) {
            super(context,0, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 重用convertView或者inflate新的视图
            View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_btdevice, parent, false);
            TextView deviceName = view.findViewById(R.id.deviceName);
            TextView deviceAddr = view.findViewById(R.id.deviceAddr);
            String name = getItem(position).getName();
            if (name == null) {
                name = "未命名设备";
            }
            String addr = getItem(position).getAddress();
            deviceName.setText(name);
            deviceAddr.setText(addr);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = listView.getFirstVisiblePosition() + position;
                    Log.i("info", getItem(index).getAddress());
                    callback.onDialogResult(getItem(index));
                    dismiss();
                }
            });
            return view;
        }
    }
}
