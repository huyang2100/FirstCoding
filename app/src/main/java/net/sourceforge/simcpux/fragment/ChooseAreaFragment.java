package net.sourceforge.simcpux.fragment;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.ChooseAreaAdapter;
import net.sourceforge.simcpux.bean.AreaItem;
import net.sourceforge.simcpux.constant.ConstantURL;
import net.sourceforge.simcpux.dao.CoolWeatherDao;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.net.OkHttpMananger;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择区域（省/市/区）
 */
public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";
    public static final String KEY_ITEMBEAN = "itembean";
    private RecyclerView recyclerView;
    private ArrayList<AreaItem> provinceList = new ArrayList<>();
    private ChooseAreaAdapter areaAdapter;
    private Gson gson = new Gson();
    private CoolWeatherDao coolWeatherDao;
    private ProgressDialog progressDialog;
    private OnItemClickLisenter onItemClickLisenter;

    public static Fragment newInstance(AreaItem item) {
        ChooseAreaFragment areaFragment = new ChooseAreaFragment();
        if (item != null) {
            Bundle args = new Bundle();
            args.putSerializable(KEY_ITEMBEAN, item);
            areaFragment.setArguments(args);
        }
        return areaFragment;
    }

    public interface OnItemClickLisenter {
        void onItemClick(AreaItem areaItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onItemClickLisenter = (OnItemClickLisenter) getActivity();
            coolWeatherDao = new CoolWeatherDao(getActivity());
        } catch (Exception e) {
            throw new ClassCastException("Activity Not implements OnItemClickLisenter!!!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        areaAdapter = new ChooseAreaAdapter(provinceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(areaAdapter);

        areaAdapter.setOnItemClickLisenter(new ChooseAreaAdapter.OnItemClickLisenter() {
            @Override
            public void itemClick(AreaItem areaItem) {
                if (onItemClickLisenter != null) {
                    onItemClickLisenter.onItemClick(areaItem);
                }
            }
        });

        Bundle args = getArguments();
        if (args == null) {
            showProvinces();
        } else {
            AreaItem areaItem = (AreaItem) args.getSerializable(KEY_ITEMBEAN);
            switch (areaItem.getCurLevel()) {
                case AreaItem.LEVEL_PROVINCE:
                    showCitys(areaItem.getId());
                    break;
                case AreaItem.LEVEL_CITY:
                    showCountrys(areaItem.getUpCode(), areaItem.getId());
                    break;
            }
        }
        return view;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正加载中...");
        }
        if (getActivity() != null && !getActivity().isFinishing()) {
            progressDialog.show();
        }
    }

    /**
     * 展示省列表
     */
    private void showProvinces() {
        //从数据库中获取省列表
        ArrayList<AreaItem> provinces = coolWeatherDao.getProvinces();
        if (provinces == null) {
            getProvincesFromServer();
        } else {
            areaAdapter.update(provinces);
        }
    }

    /**
     * 展示市列表
     */
    private void showCitys(int provinceCode) {
        //从数据库中获取省列表
        ArrayList<AreaItem> citys = coolWeatherDao.getCitys(provinceCode);
        if (citys == null) {
            getCitysFromServer(provinceCode);
        } else {
            areaAdapter.update(citys);
        }
    }

    /**
     * 展示县列表
     */
    private void showCountrys(int provinceCode, int cityCode) {
        //从数据库中获取省列表
        ArrayList<AreaItem> citys = coolWeatherDao.getCountrys(cityCode);
        if (citys == null) {
            getCountrysFromServer(provinceCode, cityCode);
        } else {
            areaAdapter.update(citys);
        }
    }

    /**
     * 从服务器获取省列表
     */
    private void getProvincesFromServer() {
        showProgressDialog();
        OkHttpMananger.get(ConstantURL.BASE_AREA, new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, str);
                ArrayList<AreaItem> provinces = gson.fromJson(str, new TypeToken<List<AreaItem>>() {
                }.getType());

                for (AreaItem item : provinces) {
                    item.setCurLevel(AreaItem.LEVEL_PROVINCE);
                }
                coolWeatherDao.saveProvinces(provinces);
                showProvinces();
                progressDialog.dismiss();
            }

            @Override
            public void onErr(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 从服务器获取市列表
     */
    private void getCitysFromServer(final int provinceCode) {
        showProgressDialog();
        OkHttpMananger.get(Uri.parse(ConstantURL.BASE_AREA).buildUpon().appendPath(String.valueOf(provinceCode)).build().toString(), new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, str);
                ArrayList<AreaItem> citys = gson.fromJson(str, new TypeToken<List<AreaItem>>() {
                }.getType());
                for (AreaItem item : citys) {
                    item.setCurLevel(AreaItem.LEVEL_CITY);
                    item.setUpCode(provinceCode);
                }
                coolWeatherDao.saveCitys(citys);
                showCitys(provinceCode);
                progressDialog.dismiss();
            }

            @Override
            public void onErr(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 从服务器获取县列表
     */
    private void getCountrysFromServer(final int provinceCode, final int cityCode) {
        showProgressDialog();
        String url = Uri.parse(ConstantURL.BASE_AREA).buildUpon()
                .appendPath(String.valueOf(provinceCode))
                .appendPath(String.valueOf(cityCode)).build().toString();
        OkHttpMananger.get(url, new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, str);
                ArrayList<AreaItem> countrys = gson.fromJson(str, new TypeToken<List<AreaItem>>() {
                }.getType());
                for (AreaItem item : countrys) {
                    item.setCurLevel(AreaItem.LEVEL_COUNTRY);
                    item.setUpCode(cityCode);
                }
                coolWeatherDao.saveCountrys(countrys);
                showCountrys(provinceCode, cityCode);
                progressDialog.dismiss();
            }

            @Override
            public void onErr(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
