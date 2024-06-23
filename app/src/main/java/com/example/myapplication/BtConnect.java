package com.example.myapplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BtConnect {
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private static final int REQUEST_ENABLE_BT = 1;

    public int init() {
//        // 检查蓝牙设备是否可用
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            return -1;  // 蓝牙硬件不可用
//        }
//        // 检查蓝牙是否打开
//        if (!bluetoothAdapter.isEnabled()) {
//            // 蓝牙未启用,弹出对话框请求用户启用
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
        return 0;
    }

    @SuppressLint("MissingPermission")
    public void searchDevice() {
        // 检查权限
        Log.i("info", "start search");
        // 获取蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 搜索设备
        bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
                Log.i("info", bluetoothDevice.getName());
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void connectDevice() {
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendData() {
//        try {
//            OutputStream outputStream = bluetoothSocket.getOutputStream();
//            outputStream.write(data);
//        } catch (IOException e) {
//
//        }
    }
}
