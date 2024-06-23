package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private Context context;
    private List<Device> deviceList;
    private FragmentManager fragmentManager;
    public DeviceListAdapter(Context context,List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Device device = deviceList.get(position);
        holder.deviceImageView.setImageResource(device.getImageResId());
        holder.deviceNameTextView.setText(device.getName());
        holder.PotionNameTextView.setText(device.getPotion());
        holder.state.setText(device.getState());
        // 设置开关按钮的状态和监听器
        holder.dswitchButton.setChecked(device.isSwitchChecked());
        holder.dswitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            device.setSwitchChecked(isChecked);
            if (isChecked) {
                // 当按钮为开时，设置开关按钮为蓝色
                holder.dswitchButton.getTrackDrawable().setTint(buttonView.getContext().getResources().getColor(R.color.blue));
                holder.state.setText(device.setState("ON"));
                // 发送指令
                int deviceID = 0x01;
                if (device.getName() != "床头灯") {
                    deviceID = 0x02;
                }
                byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                        (byte) deviceID,     // 设备号
                        (byte) 0x01,     // 开关指令
                        (byte) 0x80,     // 红色色值
                        (byte) 0x80,     // 绿色色值
                        (byte) 0x80,     // 蓝色色值
                        (byte) 0x00};   // 报警
                HomeFragment.sendData(data);
            } else {
                // 当按钮为关时，设置开关按钮为灰色
                holder.dswitchButton.getTrackDrawable().setTint(buttonView.getContext().getResources().getColor(R.color.gray));
                holder.state.setText(device.setState("OFF"));
                // 发送指令
                int deviceID = 0x01;
                if (device.getName() != "床头灯") {
                    deviceID = 0x02;
                }
                byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                        (byte) deviceID,     // 设备号
                        (byte) 0x00,     // 开关指令
                        (byte) 0x00,     // 红色色值
                        (byte) 0x00,     // 绿色色值
                        (byte) 0x00,     // 蓝色色值
                        (byte) 0x00};   // 报警
                HomeFragment.sendData(data);
            }
        });
//        // 设置点击事件监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在点击事件中执行跳转逻辑
                // 使用FragmentManager执行Fragment事务
                LightFragment fragment = new LightFragment();
                // 传递参数
                Bundle bundle = new Bundle();
                bundle.putString("device_name", device.getName());
                bundle.putInt("device_img", device.getImageResId());
                bundle.putString("device_state", device.getState());
                bundle.putInt("device_index", position);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_body, fragment,"LightFragmentTag");
                transaction.addToBackStack( "LightFragmentTag");
                transaction.commit();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 显示删除确认的AlertDialog
                showDeleteConfirmationDialog(position);
//                Log.e("长按","长按");
//                // 假设你有一个方法来处理删除操作
//                // 长按列表项的时候弹出 确认删除对话框
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("确认删除");
//                builder.setMessage("您确定要删除这个设备吗？");
//                // 点击对话框的 确认 按钮后的操作
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        handleDelete(v, position);
////                        HistoryDataFragment.adapter.notifyDataSetChanged(); // 适配器数据刷新
//                        Toast.makeText(v.getRootView().getContext(), "已删除", Toast.LENGTH_SHORT).show();
//                    }
//                });

                return true;
            }
        });

    }
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认删除");
        builder.setMessage("您确定要删除这个设备吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击确定，执行删除操作
                deviceList.remove(position); // 从数据源中移除设备
                notifyItemRemoved(position); // 通知RecyclerView数据项被移除
                notifyItemRangeChanged(position, deviceList.size()); // 可选，刷新剩余项
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消，关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
//    private void handleDelete(View view, int position) {
//        // 从数据源中移除项目
//        deviceList.remove(position);
//        // 通知数据集发生变更
//        notifyItemRemoved(position);
//        // 可以在这里添加更多的删除逻辑，例如更新数据库或通知其他组件
//    }

    // 设置FragmentManager，用于执行Fragment事务
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deviceImageView;
        TextView deviceNameTextView;
        TextView PotionNameTextView;
        TextView state;
        Switch dswitchButton;
        TextView textdevice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceImageView = itemView.findViewById(R.id.deviceImageView);
            deviceNameTextView = itemView.findViewById(R.id.deviceNameTextView);
            PotionNameTextView = itemView.findViewById(R.id.potionTextView);
            state = itemView.findViewById(R.id.state);
            dswitchButton = itemView.findViewById(R.id.dswitchButton);
            textdevice=itemView.findViewById(R.id.textdevice);
        }
    }
}

