package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ScenceListAdapter extends RecyclerView.Adapter<ScenceListAdapter.ItemViewHolder> {

    private List<Scence> ScenceList;
    private FragmentManager fragmentManager;
    public ScenceListAdapter(List<Scence> ScenceList) {
        this.ScenceList = ScenceList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scence, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Scence item = ScenceList.get(position);

        // 设置背景图片
        holder.backgroundImageView.setImageResource(item.getImageResource());

        // 设置文本
        holder.textView.setText(item.getText());

        // 设置开关按钮的状态和监听器
        holder.switchButton.setChecked(item.isSwitchChecked());
        holder.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSwitchChecked(isChecked);
            if (isChecked) {
                // 当按钮为开时，设置开关按钮为蓝色
                holder.switchButton.getTrackDrawable().setTint(buttonView.getContext().getResources().getColor(R.color.blue));
            } else {
                // 当按钮为关时，设置开关按钮为灰色
                holder.switchButton.getTrackDrawable().setTint(buttonView.getContext().getResources().getColor(R.color.gray));
            }
        });
        // 设置点击事件监听器

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                // 在点击事件中执行跳转逻辑
                // 使用FragmentManager执行Fragment事务
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // 检查点击的是第一个itemView还是第二个itemView
                if (position == 0) {
                    // 第一个itemView被点击，实例化FragmentOne
                    fragment = new AutocontrolFragment();
                } else if (position == 1) {
                    // 第二个itemView被点击，实例化FragmentTwo
                    fragment = new FestivalFragment();
                }else if (position == 2) {
                    // 第二个itemView被点击，实例化FragmentThree
                    fragment = new DisasterFragment();
                }
                bundle.putBoolean("state", item.isSwitchChecked());
                bundle.putInt("index", position);
                fragment.setArguments(bundle);

                // 如果fragment不为null，执行替换操作
                if (fragment != null) {
                    transaction.replace(R.id.main_body, fragment); // 替换主内容区域的Fragment
                    transaction.addToBackStack(null); // 将事务添加到回栈列表，允许用户后退
                    transaction.commit(); // 提交事务
                }
            }
        });
    }
    // 设置FragmentManager，用于执行Fragment事务
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
    @Override
    public int getItemCount() {
        return ScenceList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView backgroundImageView;
        TextView textView;
        Switch switchButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImageView = itemView.findViewById(R.id.backgroundImageView);
            textView = itemView.findViewById(R.id.textView);
            switchButton = itemView.findViewById(R.id.switchButton);
        }
    }


}