package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NavigationActivity extends AppCompatActivity  {
//    public class NavigationActivity extends AppCompatActivity implements addDevice.AddDeviceListener {
    private Toolbar toolbar;
    public static String name = "小明";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不显示默认顶部导航栏
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_navigation);
        setMain();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

    }
    //用于打开初始页面
    private void setMain() {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new HomeFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new ScenceFragment()).commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new MyFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    private void readConfigFile() {
//        // 加载YAML文件
//        InputStream inputStream = this.getClassLoader().getResourceAsStream("example.yml");
//        // 创建YAML对象
//        Yaml yaml = new Yaml();
//        // 解析YAML文件
//        Map<String, Object> data = yaml.load(inputStream);
//        // 获取姓名
//        name = (String) data.get("name");
    }
}
