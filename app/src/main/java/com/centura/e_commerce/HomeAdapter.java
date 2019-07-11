package com.centura.e_commerce;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.VirtualShoopingCart;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>  {
DatabaseHelper db;
Context context;
ArrayList<ProductDetailModel> productDetailModelArrayList=new ArrayList<>();

    ArrayList<ProductDetailModel> filteredproductList=new ArrayList<>();
    Dialog dialog;

    RecyclerView recyclerView;
    private ItemPosition itemPosition;
    JSONObject jsonObject;
    String product_qunt;
    JSONArray jsonArray=new JSONArray();
    private ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList;
    int user_Id;

    public HomeAdapter(ArrayList<ProductDetailModel> productDetailModelArrayList, Context context, int userId,ItemPosition itemPosition) {
    this.productDetailModelArrayList=productDetailModelArrayList;
    this.context=context;
    this.filteredproductList=productDetailModelArrayList;
    this.user_Id=userId;
    this.itemPosition=itemPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_home_adapter, parent, false);
       db=new DatabaseHelper(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final ProductDetailModel productDetailModel=productDetailModelArrayList.get(i);
        myViewHolder.tv_Name.setText(productDetailModel.getName());
        myViewHolder.tv_mrp.setText(productDetailModel.getMrp());
        myViewHolder.tv_price.setText(productDetailModel.getPrice());
        myViewHolder.tv_weight.setText(productDetailModel.getWeight());
        Picasso.get().load(productDetailModel.getImage()).into(myViewHolder.img);
        itemPosition.getitemPostion(myViewHolder.getAdapterPosition(),myViewHolder.bnt_add,myViewHolder.linearLayout,myViewHolder.tv_item_increment);
        /**/
        myViewHolder.bnt_add.setOnClickListener(new View.OnClickListener() {
            int flag=0;
            @Override
            public void onClick(View v) {

                myViewHolder.linearLayout.setVisibility(View.VISIBLE);
                myViewHolder.bnt_add.setVisibility(View.INVISIBLE);
                virtualShoopingCartArrayList=new ArrayList<>();
                virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
                if(virtualShoopingCartArrayList.size()>0) {
                    for (int j = 0; j < virtualShoopingCartArrayList.size(); j++) {
                        if (virtualShoopingCartArrayList.get(j).getId().equals(String.valueOf(user_Id))) {
                            String str_json_array = virtualShoopingCartArrayList.get(j).getProduct();
                            try {
                                jsonArray=new JSONArray(str_json_array);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            flag=flag+1;
                        }
                    }
                }
                else {
                    jsonArray=new JSONArray();
                }
                if(flag==0){
                    jsonArray=new JSONArray();
                }
                //String jsonArray=productDetailsModelArrayList.ge
                try {

                    jsonObject=new JSONObject();
                    jsonObject.put("name",productDetailModel.getName());
                    jsonObject.put("price",productDetailModel.getPrice());
                    jsonObject.put("mrp",productDetailModel.getMrp());
                    jsonObject.put("weight",productDetailModel.getWeight());
                    jsonObject.put("image1",productDetailModel.getImage());
                    jsonObject.put("prod_qtn","1");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myViewHolder.tv_item_increment.setText("1");

                Log.d("Product added JSON",new Gson().toJson(jsonArray));
                addProductDetails(jsonArray.toString(),String.valueOf(user_Id));
                Toast.makeText(context,"Added to Cart",Toast.LENGTH_LONG).show();
            }
        });
        myViewHolder.bnt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VirtualShoopingCart virtualShoopingCart=db.getProductDetail(String.valueOf(user_Id));
                JSONArray finalJsonArray=new JSONArray();

                String json= virtualShoopingCart.getProduct();
                try {
                    jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("name").equals(productDetailModel.getName())){
                            product_qunt = jsonObject.getString("prod_qtn");
                            int value = Integer.parseInt(product_qunt);
                            value = value + 1;
                            myViewHolder.tv_item_increment.setText(String.valueOf(value));
                            jsonObject.put("prod_qtn", String.valueOf(value));
                            finalJsonArray.put(jsonObject);
                            Log.i("Product added JSON", jsonObject.toString());
                        }
                        else{
                            finalJsonArray.put(jsonObject);
                        }
                    }



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                updateProductDetails(finalJsonArray.toString(),String.valueOf(user_Id));

            }
        });
        myViewHolder.bnt_minu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 0;
                JSONArray finalJsonArray=new JSONArray();
                VirtualShoopingCart virtualShoopingCart=db.getProductDetail(String.valueOf(user_Id));
                String json= virtualShoopingCart.getProduct();
                try {
                    jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("name").equals(productDetailModel.getName())){
                            product_qunt = jsonObject.getString("prod_qtn");
                            value = Integer.parseInt(product_qunt);
                            value=value-1;

                            if(value==0){
                                myViewHolder.linearLayout.setVisibility(View.GONE);
                                myViewHolder.bnt_add.setVisibility(View.VISIBLE);
                                //unit_Display(myViewHolder,position,detailArrayList);
                                //jsonArray.remove(i);
                                // finalJsonArray.put(jsonArray);
                            }
                            else {
                                myViewHolder.tv_item_increment.setText(String.valueOf(value));
                                jsonObject.put("prod_qtn",String.valueOf(value));
                                finalJsonArray.put(jsonObject);
                                Log.i("Product added JSON", jsonObject.toString());
                            }

                        }
                        else{
                            finalJsonArray.put(jsonObject);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                ((UserDashboard)context).updateData(String.valueOf(finalJsonArray.length()));

                updateProductDetails(finalJsonArray.toString(),String.valueOf(user_Id));

            }
        });
    }


    @Override
    public int getItemCount() {
        return filteredproductList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Name,tv_price,tv_item_increment,tv_weight,tv_mrp;
        public Button bnt_add,bnt_plus,bnt_minu;
        public LinearLayout linearLayout,sold_out;
        public RelativeLayout Layout_weight;
        public ImageView img;
        public int number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name=itemView.findViewById(R.id.product_name);
            tv_price=itemView.findViewById(R.id.price);
            bnt_add=itemView.findViewById(R.id.btn_add);
            img=itemView.findViewById(R.id.image_view);
            linearLayout=itemView.findViewById(R.id.plus_minu);
            tv_item_increment=itemView.findViewById(R.id.item_increment);
            bnt_plus=itemView.findViewById(R.id.btn_plus);
            bnt_minu=itemView.findViewById(R.id.btn_minus);
            tv_weight=itemView.findViewById(R.id.weight);
            tv_mrp=itemView.findViewById(R.id.tv_mrp);
            Layout_weight=itemView.findViewById(R.id.layout_weight);
            sold_out=itemView.findViewById(R.id.sold);
        }
    }

    private void addProductDetails(String procduct_detail, String user_Id){
        // inserting note in db and getting
        // newly inserted note id
        virtualShoopingCartArrayList=new ArrayList<>();
        virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
        if(virtualShoopingCartArrayList.size()== 0){
            db.insertProduct(procduct_detail,user_Id);
            ((UserDashboard)context).updateData(String.valueOf(jsonArray.length()));
        }
        else{
            int flag=0;
            for(int i=0;i<virtualShoopingCartArrayList.size();i++){
                if(virtualShoopingCartArrayList.get(i).getId().equals(String.valueOf(user_Id))){
                    db.updateProduct(procduct_detail,user_Id);
                    ((UserDashboard)context).updateData(String.valueOf(jsonArray.length()));
                    flag=flag+1;
                }
            }
            if(flag==0){
                db.insertProduct(procduct_detail,user_Id);

                ((UserDashboard)context).updateData(String.valueOf(jsonArray.length()));

            }
        }


    }

    private void updateProductDetails(String product_detail,String user_id){
       virtualShoopingCartArrayList=new ArrayList<>();
        virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
        for(int i=0;i<virtualShoopingCartArrayList.size();i++){
            if(virtualShoopingCartArrayList.get(i).getId().equals(user_id)){
                db.updateProduct(product_detail,user_id);
            }
        }


    }

    public interface ItemPosition{
        public void getitemPostion(int position, Button add_bnt, LinearLayout linearLayout, TextView tv_item_increment);
    }
}
