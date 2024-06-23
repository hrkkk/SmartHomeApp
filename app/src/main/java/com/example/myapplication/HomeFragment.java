package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView deviceState;
    private RecyclerView recyclerView;
    private ImageView homeAddImageView;
    public static DeviceListAdapter adapter;
    public static List<Device> deviceList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private static Boolean isFirst = true;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private List<BluetoothDevice> btDeviceList = new ArrayList<>();
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BtDeviceDialog btDeviceDialog;
    private static OutputStream outputStream;
    private String defaultDeviceAddr = "33:79:6C:4C:C2:B4";
    private AlertDialog alertDialog;
    private AlertDialog connectDialog;

    public interface DialogCallback {
        void onDialogResult(BluetoothDevice device);
    }

    public static void sendData(byte[] data) {
        if (outputStream != null) {
            try {
                outputStream.write(data);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createConnection(BluetoothDevice device) {
        Log.i("info", "try to connect");
        try {
            // 建立连接
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()) {
                // 建立数据通道
                outputStream = bluetoothSocket.getOutputStream();
                if (connectDialog != null && connectDialog.isShowing()) {
                    connectDialog.dismiss();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void destroyConnection() {
        // 断开数据通道
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 断开socket连接
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 资源清理
        outputStream = null;
        bluetoothSocket = null;
    }

    class CustomBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {      // 找到设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    btDeviceList.add(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {   // 开始搜索
                Log.i("info", "蓝牙事件触发：开始搜索");
                btDeviceList.clear();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {   // 搜索完成
                Log.i("info", "蓝牙事件触发：搜索结束");
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                // 弹出搜索到的设备列表
                btDeviceDialog = new BtDeviceDialog(btDeviceList);
                btDeviceDialog.setCallback(callback);
                btDeviceDialog.show(getActivity().getSupportFragmentManager(), "BtDeviceDialog");
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.i("info", "蓝牙事件触发：设备已连接");
                Toast.makeText(getContext(), "蓝牙设备已连接", Toast.LENGTH_SHORT).show();
                deviceState.setText("设备状态：已连接");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.i("info", "蓝牙事件触发：设备断开连接");
                Toast.makeText(getContext(), "蓝牙设备断开连接", Toast.LENGTH_SHORT).show();
                deviceState.setText("设备状态：未连接");
                destroyConnection();
            }
        }
    }

    private DialogCallback callback = new DialogCallback() {
        @Override
        public void onDialogResult(BluetoothDevice device) {
            // 配对设备
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.e("error", "permission.BLUETOOTH_CONNECT error");
                return;
            }
            if (device != null) {
                // 如果设备已经绑定
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.i("info", "窗口回调事件：设备已绑定，建立连接");
                    // 直接建立连接
                    createConnection(device);
                } else {
                    Log.i("info", "窗口回调事件：绑定设备");
                    // 绑定设备
                    device.createBond();
                    while (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        createConnection(device);
                    }
                }
            }
        }
    };

    private final CustomBroadcastReceiver broadcastReceiver = new CustomBroadcastReceiver();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
//    public void setOnDeviceAddedListener(OnDeviceAddedListener listener) {
//        mListener = listener;

//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        checkPermissions();
        initBluetooth();
//        autoConnect(defaultDeviceAddr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homefragment, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new DeviceListAdapter(getContext(), deviceList);
        homeAddImageView = rootView.findViewById(R.id.homeadd);
        deviceState = rootView.findViewById(R.id.deviceState);
        // 获取FragmentManager实例
        fragmentManager = getFragmentManager();
        // 设置点击事件监听器
        homeAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // 设置 RecyclerView 布局管理器为 GridLayoutManager，列数为 2
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // 设置FragmentManager到适配器中
        adapter.setFragmentManager(getFragmentManager());

        // 添加一些设备数据用于展示
        if (isFirst) {
            addDevices();
            isFirst = false;
        }

        return rootView;
    }

    /*
        自动连接指定设备
     */
    private void autoConnect(String addr) {
        // 创建Device对象
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(addr);
        // 检查设备是否已配对
        if (device != null) {
            // 弹出窗口
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Info");
            builder.setMessage("正在连接默认设备：\n" + defaultDeviceAddr + "\n");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            connectDialog = builder.create();
            connectDialog.setCancelable(false);
            connectDialog.show();

            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                // 设备已配对，可以直接连接
                createConnection(device);
            } else {
                // 设备未配对，需要先配对
                device.createBond();
                while (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    createConnection(device);
                }
            }
        }
    }

    private void initBluetooth() {
        // 检查蓝牙设备是否可用
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("info", "蓝牙硬件不可用");
            //  return -1;  // 蓝牙硬件不可用
        }
        // 检查蓝牙是否打开
        if (!bluetoothAdapter.isEnabled()) {
            // 蓝牙未启用,弹出对话框请求用户启用
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // 注册广播事件
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getContext().registerReceiver(broadcastReceiver, filter);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH},
                    REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_ADMIN},
                    REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_SCAN},
                    REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ENABLE_BT);
        }
    }

    private void showPopupMenu(View view) {
        // 创建一个PopupMenu实例
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        // 为PopupMenu添加菜单项
        inflater.inflate(R.menu.menu, popup.getMenu());

        // 设置菜单项点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // 根据菜单项的ID处理点击事件
                switch (item.getItemId()) {
                    case R.id.adddevice:
                        addDevice fragment = new addDevice();
                        FragmentTransaction transaction = fragmentManager.beginTransaction(); // 获取父级 FragmentManager
                        transaction.replace(R.id.main_body, fragment, "addDeviceTag"); // 使用父级 FragmentManager 替换视图
                        transaction.addToBackStack("addDeviceTag"); // 将事务添加到返回栈
                        transaction.commit(); // 提交事务
                        // 处理第一个菜单项的点击事件
                        return true;
                    case R.id.bluetooth:
                        Toast.makeText(getContext(), "开始搜索附近设备", Toast.LENGTH_SHORT).show();

                        if (bluetoothAdapter == null) {
                            return true;
                        }
                        // 开始搜索
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        bluetoothAdapter.startDiscovery();

                        // 弹出窗口
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Info");
                        builder.setMessage("正在搜索附近设备\n请等候...");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 结束搜索
                                if (bluetoothAdapter.isDiscovering()) {
                                    bluetoothAdapter.cancelDiscovery();
                                    Toast.makeText(getContext(), "停止搜索", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        return true;
                    case R.id.disconnect:
                        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                            Toast.makeText(getContext(), "设备连接已断开", Toast.LENGTH_SHORT).show();
                            destroyConnection();
                            deviceState.setText("设备状态：未连接");
                        }
                        return true;
                    case R.id.aboutme:
                        // 弹出窗口
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setTitle("版本信息");
                        builder1.setMessage("V3.2.0");
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        return true;
                }
                return false;
            }
        });

        // 显示PopupMenu
        popup.show();
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // 用户启用了蓝牙
            } else {
                // 用户未启用蓝牙
            }
        }
    }
    // 添加设备数据
    private void addDevices() {
        deviceList.add(new Device("床头灯", "主卧","OFF",R.drawable.chuangtoudeng));
        deviceList.add(new Device("单吊灯", "主卧","OFF",R.drawable.dandiaodeng));
        deviceList.add(new Device("吊灯","客厅","OFF", R.drawable.diaodeng));
        deviceList.add(new Device("吸顶灯","走廊","OFF",R.drawable.xidingdeng));
        deviceList.add(new Device("节能灯","卫生间","OFF",R.drawable.jienengdeng));
        deviceList.add(new Device("落地灯","次卧","OFF",R.drawable.luodideng));
        deviceList.add(new Device("台灯","书房","OFF", R.drawable.taideng));
        // 添加更多设备数据..

        // 刷新 RecyclerView
    }

    public  void addDevice1(Device device) {
        deviceList.add(device);
        String name= device.getName();
        Log.e(name,name);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Log.e("Adapter Error", "Adapter is null when trying to notify data set changed.");
        }
//        recyclerView.invalidate(); // 强制刷新 RecyclerView
    }
}