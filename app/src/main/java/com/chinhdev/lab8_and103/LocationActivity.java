package com.chinhdev.lab8_and103;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.chinhdev.lab8_and103.API.GHNRequest;
import com.chinhdev.lab8_and103.API.GHNService;
import com.chinhdev.lab8_and103.Adapter.Adapter_Item_District_Select_GHN;
import com.chinhdev.lab8_and103.Adapter.Adapter_Item_Province_Select_GHN;
import com.chinhdev.lab8_and103.Adapter.Adapter_Item_Ward_Select_GHN;
import com.chinhdev.lab8_and103.databinding.ActivityLocationBinding;
import com.chinhdev.lab8_and103.models.District;
import com.chinhdev.lab8_and103.models.DistrictRequest;
import com.chinhdev.lab8_and103.models.Province;
import com.chinhdev.lab8_and103.models.ResponseGHN;
import com.chinhdev.lab8_and103.models.Ward;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationActivity extends AppCompatActivity {
    private ActivityLocationBinding binding;
    private GHNRequest request;
    private GHNService ghnServices;
    private String productId, productTypeId, productName, description, WardCode;
    private double rate, price;
    private int image, DistrictID, ProvinceID ;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request = new GHNRequest();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
             productId = bundle.getString("productId");
             productTypeId = bundle.getString("productTypeId");
             productName = bundle.getString("productName");
             description = bundle.getString("description");
             rate = bundle.getDouble("rate");
             price = bundle.getDouble("price");
             image = bundle.getInt("image");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);

        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);


    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                request.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                request.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };

    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){

                    if(response.body().getData() == null)
                        return;

                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());

                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapter_item_ward_select_ghn );
    }

}