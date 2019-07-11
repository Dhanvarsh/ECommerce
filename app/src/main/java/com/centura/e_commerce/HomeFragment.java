package com.centura.e_commerce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.VirtualShoopingCart;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private ArrayList<ProductDetailModel> productDetailModelArrayList = new ArrayList<>();
    private ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList;
    RecyclerView recyclerView;
    int userId;
    HomeAdapter homeAdapter;
    Context context;
    CoordinatorLayout coordinatorLayout;
    private DatabaseHelper db;
    String str_json_array;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    public HomeFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= getContext();
        db = new DatabaseHelper(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        coordinatorLayout=view.findViewById(R.id.Home_container);

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        productDetails();
        sharedPreferences=getActivity().getSharedPreferences("MyPrefe",Context.MODE_PRIVATE);
        userId=sharedPreferences.getInt("user_id",0);
        homeAdapter=new HomeAdapter(productDetailModelArrayList, context, userId, new HomeAdapter.ItemPosition() {
            @Override
            public void getitemPostion(int position, Button add_bnt, LinearLayout linearLayout, TextView tv_item_increment) {
                String product_qunt = null;
                Log.d("position", "pos" + position);
                 virtualShoopingCartArrayList= new ArrayList<>();
                virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
                Log.i("Arraly Lisy == ", new Gson().toJson(virtualShoopingCartArrayList));

                    for (int j = 0; j < virtualShoopingCartArrayList.size(); j++) {
                        if (virtualShoopingCartArrayList.get(j).getId().equals(String.valueOf(userId))) {
                            str_json_array = virtualShoopingCartArrayList.get(j).getProduct();
                            try {
                                jsonArray = new JSONArray(str_json_array);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    if (productDetailModelArrayList.get(position).getName().equals(jsonObject.getString("name"))) {
                                        if (add_bnt.getVisibility() == View.VISIBLE) {
                                            add_bnt.setVisibility(View.GONE);
                                            linearLayout.setVisibility(View.VISIBLE);
                                            VirtualShoopingCart virtualShoopingCart = virtualShoopingCartArrayList.get(j);

                                            try {

                                                product_qunt = jsonObject.getString("prod_qtn");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            tv_item_increment.setText(product_qunt);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

        });
        recyclerView.setAdapter(homeAdapter);
        return view;
    }

    private void productDetails() {
        ProductDetailModel productDetailModel = new ProductDetailModel("Green Apple", "120","150","1", R.drawable.greenapple);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Apple", "120","150","1", R.drawable.apple);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Cucumber", "23","30","1", R.drawable.cucumber);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Cabbage", "30","35","1",R.drawable.cabbage);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Green Chilly", "20","20","1",R.drawable.greenchilly);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Orange", "75","90","1",R.drawable.orange);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Grapes", "80","85","1",R.drawable.grapes);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Banana", "50","55","1",R.drawable.banana);
        productDetailModelArrayList.add(productDetailModel);
        productDetailModel = new ProductDetailModel("Broccoli", "80","100","1",R.drawable.broccoli);
        productDetailModelArrayList.add(productDetailModel);

    }

}
