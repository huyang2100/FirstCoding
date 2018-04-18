package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import net.sourceforge.simcpux.R;

import java.util.ArrayList;

public class LBSBaiduActivity extends AppCompatActivity {

    private TextView tv;
    private ArrayList<String> permissionList = new ArrayList<>();
    private LocationClient locationClient;
    private MyLocationLisenter locationLisenter;
    private StringBuilder sb = new StringBuilder();
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_lbsbaidu);
        mapView = findViewById(R.id.mapview);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int permission : grantResults) {
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        init();
                    }
                } else {
                    finish();
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init() {
        tv = findViewById(R.id.tv);
        tv.setMovementMethod(new ScrollingMovementMethod());
        locationClient = new LocationClient(this);
        locationLisenter = new MyLocationLisenter();
        locationClient.registerLocationListener(locationLisenter);
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setScanSpan(3000);
        locationOption.setCoorType("bd09ll");
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setIsNeedAddress(true);
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LBSBaiduActivity.class);
        context.startActivity(intent);
    }

    class MyLocationLisenter extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            sb.setLength(0);
//            sb.append("纬度：").append(bdLocation.getAltitude()).append("\r\n")
//                    .append("经度：").append(bdLocation.getLatitude()).append("\r\n")
//                    .append("国家：").append(bdLocation.getCountry()).append("\r\n")
//                    .append("省份：").append(bdLocation.getProvinces()).append("\r\n")
//                    .append("城市：").append(bdLocation.getCity()).append("\r\n")
//                    .append("区：").append(bdLocation.getDistrict()).append("\r\n")
//                    .append("街道：").append(bdLocation.getStreet()).append("\r\n")
//                    .append("定位方式：");
//            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
//                sb.append("GPS").append("\r\n");
//            }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//                sb.append("网络").append("\r\n");
//            }
//            tv.setText(sb.toString());

            if (isFirst) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, 16f));
                isFirst = false;
            }

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(myLocationData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        if (locationClient != null && locationLisenter != null) {
            locationClient.unRegisterLocationListener(locationLisenter);
        }
    }
}
