package com.example.myapplication;

public class Scence {
    private int imageResource;
    private String text;
    private boolean switchChecked;

    public Scence(int imageResource, String text) {
        this.imageResource = imageResource;
        this.text = text;
        this.switchChecked = false; // 默认设置为关闭状态
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getText() {
        return text;
    }

    public boolean isSwitchChecked() {
        return switchChecked;
    }

    public void setSwitchChecked(boolean switchChecked) {
        this.switchChecked = switchChecked;
    }
}