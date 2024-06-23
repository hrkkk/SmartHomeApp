package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addDevice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addDevice extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageViewBack;
    private Spinner spinnerLocation;
    private ArrayAdapter<String> spinnerAdapter;
    private EditText editTextDeviceName;
    private Button yes;
    private String selectedLocation;
    public addDevice() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addDevice.
     */
    // TODO: Rename and change types and number of parameters
    public static addDevice newInstance(String param1, String param2) {
        addDevice fragment = new addDevice();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewBack = view.findViewById(R.id.image_view);
        spinnerLocation = view.findViewById(R.id.spinner_location);
        editTextDeviceName = view.findViewById(R.id.editTextDeviceName);
        yes = view.findViewById(R.id.yes);
        String[] locations = {"请选择一个位置","客厅", "主卧", "次卧", "走廊", "卫生间", "书房"};
        String name = editTextDeviceName.getText().toString();
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, locations);

        // 设置下拉视图的布局

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(spinnerAdapter);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                { selectedLocation = (String) adapterView.getItemAtPosition(i);}

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        yes.setOnClickListener(view1 -> {
            String deviceName = editTextDeviceName.getText().toString();
//            if(name == "台灯1") {
//                Log.e(name,"111111111111");
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.taideng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("床头灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.chuangtoudeng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("单吊灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.dandiaodeng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("吊灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.diaodeng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("吸顶灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.xidingdeng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("节能灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.jienengdeng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else if(name != null && name.contains("落地灯")){
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.luodideng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
//            else
//            {
//                Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.deng);
//                homefragment homeFragment = new homefragment();
//                homeFragment.addDevice1(newDevice);
//            }
            Device newDevice = new Device(deviceName, selectedLocation, "OFF", R.drawable.deng);
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.addDevice1(newDevice);
            getFragmentManager().popBackStack();

//            if (!deviceName.isEmpty() && !selectedLocation.equals("请选择一个位置")) {
//                // Get the homefragment instance and add the new device
//                homefragment homeFragment = (homefragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
//                Log.e("2222222222222","222222222");
//                if (homeFragment != null) {
//                    Log.e("11111111111","1111111111111");
//                    homeFragment.addDevice(newDevice);
//                }
//                getActivity().getSupportFragmentManager().popBackStack();
//            }else {
//                Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
//            }
//            if (!deviceName.isEmpty() && !selectedLocation.equals("请选择一个位置")) {
//                // Get the homefragment instance and add the new device
////                (homefragment)getContext().addDevice1(newDevice);
//                Log.e("11111111111","1111111111");
//                getFragmentManager().popBackStack();
//            }else {
//                Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
//            }
//            if (!deviceName.isEmpty() && !selectedLocation.equals("请选择一个位置")) {
//                if (getActivity() instanceof homefragment.OnDeviceAddedListener) {
//                    ((homefragment.OnDeviceAddedListener) getActivity()).onDeviceAdded(newDevice);
//                    // 后退到homefragment
//                    ((NavigationActivity) getActivity()).switchToHomeFragment();
//                }
//            } else {
//                Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
//            }
        });
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

}
