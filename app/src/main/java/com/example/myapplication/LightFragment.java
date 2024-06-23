package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private List<Device> deviceList;
    private FragmentManager fragmentManager;
    private ImageView imageViewBack;
    private TextView textDevice;
    private ImageView imageViewLightBulb;
    private SeekBar seekBarBrightness;
    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;
    private Switch lightSwitch;
    private TextView brightnessTextView;
    private TextView textViewRed;
    private TextView textViewGreen;
    private TextView textViewBlue;
    private FrameLayout fragment_light;
    private int deviceIndex;
    private int light = 50;
    private int rgb[] = { 128, 128, 128 };

    public LightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LightFragment newInstance(String param1, String param2) {
        LightFragment fragment = new LightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment_light = view.findViewById(R.id.fragment_light);

        // 获取 ImageView 的引用
        imageViewBack = view.findViewById(R.id.image_view);
        textDevice = view.findViewById(R.id.textdevice);
        imageViewLightBulb = view.findViewById(R.id.imageViewLightBulb);
        seekBarBrightness = view.findViewById(R.id.seekBarBrightness);
        brightnessTextView = view.findViewById(R.id.brightness_value);
        seekBarRed = view.findViewById(R.id.seekBarRedValue);
        seekBarGreen = view.findViewById(R.id.seekBarGreenValue);
        seekBarBlue = view.findViewById(R.id.seekBarBlueValue);
        lightSwitch = view.findViewById(R.id.switch1);
        textViewRed = view.findViewById(R.id.textViewRed);
        textViewGreen = view.findViewById(R.id.textViewGreen);
        textViewBlue = view.findViewById(R.id.textViewBlue);

        seekBarBrightness.setProgress(light);
        brightnessTextView.setText(light + "%");
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                light = i;
                brightnessTextView.setText(light + "%");

                if (lightSwitch.isChecked()) {
                    Log.i("info", "发送指令：打开");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }
//                    rgb = colorTempToRGB(colorTemp);

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x01,     // 开关指令
                            (byte) (int) (rgb[0] * (light / 100.0)),     // 红色色值
                            (byte) (int) (rgb[1] * (light / 100.0)),     // 绿色色值
                            (byte) (int) (rgb[2] * (light / 100.0)),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarRed.setProgress((int)(rgb[0] / 255.0 * 100));
        seekBarGreen.setProgress((int)(rgb[1] / 255.0 * 100));
        seekBarBlue.setProgress((int)(rgb[2] / 255.0 * 100));
        textViewRed.setText((int)(rgb[0] / 255.0 * 100) + "%");
        textViewGreen.setText((int)(rgb[1] / 255.0 * 100) + "%");
        textViewBlue.setText((int)(rgb[2] / 255.0 * 100) + "%");

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    HomeFragment.deviceList.get(deviceIndex).setSwitchChecked(true);
                    HomeFragment.deviceList.get(deviceIndex).setState("ON");
                    HomeFragment.adapter.notifyDataSetChanged();
                    lightSwitch.setText("打开");
                    Log.i("info", "发送指令：打开");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }
//                    rgb = colorTempToRGB(colorTemp);

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x01,     // 开关指令
                            (byte) (int) (rgb[0] * (light / 100.0)),     // 红色色值
                            (byte) (int) (rgb[1] * (light / 100.0)),     // 绿色色值
                            (byte) (int) (rgb[2] * (light / 100.0)),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                } else {
                    HomeFragment.deviceList.get(deviceIndex).setSwitchChecked(false);
                    HomeFragment.deviceList.get(deviceIndex).setState("OFF");
                    HomeFragment.adapter.notifyDataSetChanged();
                    lightSwitch.setText("关闭");
                    Log.i("info", "发送指令：关闭");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x00,     // 开关指令
                            (byte) (0x00),     // 红色色值
                            (byte) (0x00),     // 绿色色值
                            (byte) (0x00),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                }
            }
        });

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 将0-100的进度值映射为色温值

                rgb[0] = (int)(progress / 100.0 * 255);
                textViewRed.setText(progress + "%"); // 更新色温值显示

                if (lightSwitch.isChecked()) {
                    Log.i("info", "发送指令：打开");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x01,     // 开关指令
                            (byte) (int) (rgb[0] * (light / 100.0)),     // 红色色值
                            (byte) (int) (rgb[1] * (light / 100.0)),     // 绿色色值
                            (byte) (int) (rgb[2] * (light / 100.0)),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 将0-100的进度值映射为色温值
                rgb[1] = (int)(progress / 100.0 * 255);
                textViewGreen.setText(progress + "%"); // 更新色温值显示

                if (lightSwitch.isChecked()) {
                    Log.i("info", "发送指令：打开");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x01,     // 开关指令
                            (byte) (int) (rgb[0] * (light / 100.0)),     // 红色色值
                            (byte) (int) (rgb[1] * (light / 100.0)),     // 绿色色值
                            (byte) (int) (rgb[2] * (light / 100.0)),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 将0-100的进度值映射为色温值
                rgb[2] = (int)(progress / 100.0 * 255);
                textViewBlue.setText(progress + "%"); // 更新色温值显示

                if (lightSwitch.isChecked()) {
                    Log.i("info", "发送指令：打开");

                    int deviceID = 0x01;
                    if (textDevice.getText() != "床头灯") {
                        deviceID = 0x02;
                    }

                    byte[] data = new byte[]{(byte) 0xff, (byte) 0xff,      // 帧头
                            (byte) deviceID,     // 设备号
                            (byte) 0x01,     // 开关指令
                            (byte) (int) (rgb[0] * (light / 100.0)),     // 红色色值
                            (byte) (int) (rgb[1] * (light / 100.0)),     // 绿色色值
                            (byte) (int) (rgb[2] * (light / 100.0)),     // 蓝色色值
                            (byte) 0x00};   // 报警
                    HomeFragment.sendData(data);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 接收传递来的参数
        Bundle args = getArguments();
        if (args != null) {
            String deviceName = args.getString("device_name");
            int imageResId = args.getInt("device_img");
            String deviceState = args.getString("device_state");
            deviceIndex = args.getInt("device_index");
            textDevice.setText(deviceName);
            textDevice.setTextColor(getResources().getColor(android.R.color.black));
            if (deviceState == "ON") {
                lightSwitch.setChecked(true);
            } else {
                lightSwitch.setChecked(false);
            }
            changeImageViewBackground(imageResId);
        }
        // 设置点击事件监听器
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回到上一个页面
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    // 在适当的时机（例如，响应某个事件）改变 ImageView 的背景图片
    private void changeImageViewBackground(int newImageResourceId) {
        imageViewLightBulb.setImageResource(newImageResourceId);
    }
}
