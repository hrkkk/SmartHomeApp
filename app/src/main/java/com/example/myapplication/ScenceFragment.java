package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScenceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    public static List<Scence> scenceList;
    public static ScenceListAdapter adapter;
    private static boolean isFirst = true;

    public ScenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment scencefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScenceFragment newInstance(String param1, String param2) {
        ScenceFragment fragment = new ScenceFragment();
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
        View view = inflater.inflate(R.layout.fragment_scencefragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 准备数据
        if (isFirst) {
            prepareItemList();
            isFirst = false;
        }

        // 设置适配器
        adapter = new ScenceListAdapter(scenceList);
        recyclerView.setAdapter(adapter);
        // 设置FragmentManager到适配器中
        adapter.setFragmentManager(getFragmentManager());
        return view;
    }

    private void prepareItemList() {
        scenceList = new ArrayList<>();
        // 添加三行数据
        scenceList.add(new Scence(R.drawable.leavehome, "智能启停"));
        scenceList.add(new Scence(R.drawable.jierihappy, "节日庆祝"));
        scenceList.add(new Scence(R.drawable.zaihai, "灾害预警"));
    }
}

