package com.centura.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.VirtualShoopingCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShoppingCartFragment extends Fragment {

    int sum=0;
    Context context;
    int userId;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter cartAdapter;
    private ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList=new ArrayList<>();
    private DatabaseHelper db;

    private LinearLayout emptyCart,total_layout;
    Button btn_shopping,btn_checkout;
    SharedPreferences sharedPreferences;
    CoordinatorLayout coordinatorLayout;

    TextView tv_total;
    Snackbar snackbar;
    String str_json_array;
    ArrayList<ProductDetailModel> productDetailModelArrayList;

    int flag=0;
    JSONArray jsonArray=new JSONArray();

    public ShoppingCartFragment() {
        // Required empty public constructor
    }


    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        db = new DatabaseHelper(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        tv_total=view.findViewById(R.id.tv_total);
        recyclerView=view.findViewById(R.id.recyclerView);
        emptyCart = view.findViewById(R.id.empty_view);


        coordinatorLayout=view.findViewById(R.id.cart_fragment_layout);


        total_layout = view.findViewById(R.id.total_layout);
        btn_shopping= view.findViewById(R.id.start_shopping);
        btn_checkout=view.findViewById(R.id.bnt_checkout);


        sharedPreferences=getActivity().getSharedPreferences("MyPrefe",Context.MODE_PRIVATE);
        userId=sharedPreferences.getInt("user_id",0);
        getArrayOfItem();
        total();
        cartAdapter = new ShoppingCartAdapter(context, virtualShoopingCartArrayList,productDetailModelArrayList,sum, userId, jsonArray, new ShoppingCartAdapter.ArraySize() {
            @Override
            public void getarraySize(int size,int total) {
                if(size>0){
                    total_layout.setVisibility(View.VISIBLE);
                    emptyCart.setVisibility(View.GONE);
                    sum=total;
                    tv_total.setText(String.valueOf(total));
                }
                else {
                    total_layout.setVisibility(View.GONE);
                    emptyCart.setVisibility(View.VISIBLE);
                }

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(cartAdapter);
        toggleEmptyCart();
        onClick();
        return view;
    }

    private void total() {

        if(jsonArray.length()>0) {
            int price=0,qunt=0;
            JSONObject jsonObject1 = new JSONObject();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonObject1 = jsonArray.getJSONObject(i);
                    String str_price=jsonObject1.getString("price");
                    String str_quantity=jsonObject1.getString("prod_qtn");
                    price = Integer.parseInt(str_price);
                    qunt = Integer.parseInt(str_quantity);
                    sum = sum + (qunt*price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tv_total.setText(String.valueOf(sum));
            }
        }
        else {
            tv_total.setText(String.valueOf(sum));
        }
    }


    private void onClick(){
        btn_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserDashboard.class);
                intent.putExtra("Place_order",false);
                startActivity(intent);

            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Place_OrderActivity.class);
                intent.putExtra("total",String.valueOf(sum));
                startActivity(intent);
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyCart() {
        // you can check notesList.size() > 0
        if (db.getProductCount() > 0) {
            if(jsonArray.length()>0){
                total_layout.setVisibility(View.VISIBLE);
                emptyCart.setVisibility(View.GONE);}
            else {
                total_layout.setVisibility(View.GONE);
                emptyCart.setVisibility(View.VISIBLE);
            }
        } else {
            total_layout.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }
    private void getArrayOfItem(){
        virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
        if(virtualShoopingCartArrayList.size()>0){
            for(int i=0;i<virtualShoopingCartArrayList.size();i++){
                if(virtualShoopingCartArrayList.get(i).getId().equals(String.valueOf(userId))){
                    str_json_array = virtualShoopingCartArrayList.get(i).getProduct();
                    flag=1;
                }
            }
        }
        if(flag==0) {
            jsonArray = new JSONArray();
        }
        else {

            try {
                jsonArray = new JSONArray(str_json_array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
