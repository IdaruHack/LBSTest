package com.example.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public AMapLocationClient mapLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        setContentView(R.layout.activity_main);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
        permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.
        permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            requsetLocation();
        }
        mapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                positionText = (TextView) findViewById(R.id.position_text_view);
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append("纬度：").append(aMapLocation.getLatitude()).append("\n");
                currentPosition.append("经度：").append(aMapLocation.getLongitude()).append("\n");
                currentPosition.append("定位方式：");
                if(aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS){
                    currentPosition.append("GPS");
                }else if(aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_WIFI){
                    currentPosition.append("网络定位");
                }
                positionText.setText(currentPosition);
            }
        });
    }
    private void requsetLocation(){
        mapLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须统一所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        requsetLocation();
                    }
                    }else {
                    Toast.makeText(this,"权限许可未通过",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
