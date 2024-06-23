package com.example.myapplication;

public class Device {
    private String name;
    private String potion;
    private String state;
    private int imageResId;
    private boolean dswitchChecked;
    public Device(String name, String potion, String state, int imageResId) {
        this.name = name;
        this.potion = potion;
        this.state = state;
        this.imageResId = imageResId;
        this.dswitchChecked = false; // 默认设置为关闭状态
    }
    public String getName() {
        return name;
    }
    public String setName(String name) {
        this.name = name;
        return potion;
    }
    public String getPotion() {
        return potion;
    }
    public String setPotion(String potion) {
        this.potion = potion;
        return potion;
    }
    public String getState() {
        return state;
    }
    public String setState(String state) {
        this.state = state;
        return state;
    }
    public int getImageResId() {
        return imageResId;
    }
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
    public boolean isSwitchChecked() {
        return dswitchChecked;
    }
    public void setSwitchChecked(boolean dswitchChecked) {
        this.dswitchChecked = dswitchChecked;
    }
}

