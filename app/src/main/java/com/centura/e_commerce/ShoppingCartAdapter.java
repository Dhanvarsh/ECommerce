package com.centura.e_commerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.VirtualShoopingCart;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {

    private DatabaseHelper db;
    private Context context;
    Dialog dialog;

    RecyclerView recyclerView;

    private ArraySize arraySize;
    String product_qunt,name,price,image1,qunt,mrp,unit;
    int sum=0,user_Id;



    JSONObject jsonObject,jsonObject1;
    private JSONArray jsonArray= new JSONArray(), jsonArray1=new JSONArray();
    private ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList;
    ArrayList<ProductDetailModel> productDetailModelArrayList;



    public ShoppingCartAdapter(Context context, ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList,
                               ArrayList<ProductDetailModel> productDetailModelArrayList, int sum, int user_Id, JSONArray jsonArray, ArraySize arraySize){
        this.context = context;
        this.virtualShoopingCartArrayList = virtualShoopingCartArrayList;
        this.user_Id=user_Id;
        this.jsonArray=jsonArray;
        this.productDetailModelArrayList=productDetailModelArrayList;
        this.sum=sum;
        //this.str_json_array=str_json_array;
        db=new DatabaseHelper(context);
        this.arraySize=arraySize;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_cart_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            name=jsonObject.getString("name");
            price=jsonObject.getString("price");
            mrp=jsonObject.getString("mrp");
            unit=jsonObject.getString("weight");
            image1=jsonObject.getString("image1");
            qunt=jsonObject.getString("prod_qtn");
            myViewHolder.tv_Name.setText(name);
            myViewHolder.tv_price.setText(price);
            myViewHolder.tv_units.setText(unit);
            myViewHolder.tv_mrp.setText(mrp);
            Picasso.get().load(image1).into(myViewHolder.img);
            myViewHolder.tv_item_increment.setText(qunt);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        myViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsonObject=jsonArray.getJSONObject(position);
                    int qunt=Integer.parseInt(jsonObject.getString("prod_qtn"));
                    int price= Integer.parseInt(jsonObject.getString("price"));
                    int mul=qunt*price;
                    sum=sum-mul;
                    jsonArray.remove(position);
                    notifyDataSetChanged();
                    updateProductDetails(jsonArray.toString(),String.valueOf(user_Id));
                    ((UserDashboard)context).updateData(String.valueOf(jsonArray.length()));
                    int len=jsonArray.length();
                    arraySize.getarraySize(len,sum);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                        JSONObject jsonObject1=jsonArray.getJSONObject(position);
                        if(jsonObject.getString("name").equals(jsonObject1.getString("name"))) {
                            product_qunt = jsonObject.getString("prod_qtn");
                            int value = Integer.parseInt(product_qunt);
                            value = value + 1;
                            myViewHolder.tv_item_increment.setText(String.valueOf(value));
                            jsonObject.put("prod_qtn", String.valueOf(value));
                            finalJsonArray.put(jsonObject);
                            int price= Integer.parseInt(jsonObject.getString("price"));
                            sum=sum+price;
                            arraySize.getarraySize(jsonArray.length(),sum);
                            Log.i("Product added JSON", jsonObject.toString());
                        }
                        else {
                            finalJsonArray.put(jsonObject);
                        }

                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Log.i("Product added JSON", finalJsonArray.toString());
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
                    jsonObject1=jsonArray.getJSONObject(position);
                    jsonArray1 = new JSONArray(json);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        jsonObject = jsonArray1.getJSONObject(i);
                        if(jsonObject.getString("name").equals(jsonObject1.getString("name"))) {
                            product_qunt = jsonObject.getString("prod_qtn");
                            value = Integer.parseInt(product_qunt);
                            value=value-1;
                            if(value==0){
                                int price= Integer.parseInt(jsonObject.getString("price"));
                                sum=sum-price;
                                jsonArray.remove(position);
                                arraySize.getarraySize(jsonArray.length(),sum);

                                notifyDataSetChanged();
                                ((UserDashboard)context).updateData(String.valueOf(jsonArray.length()));


                            }
                            else {
                                myViewHolder.tv_item_increment.setText(String.valueOf(value));

                                jsonObject=jsonArray.getJSONObject(i);
                                jsonObject.put("prod_qtn", String.valueOf(value));
                                int price= Integer.parseInt(jsonObject.getString("price"));
                                sum=sum-price;
                                arraySize.getarraySize(jsonArray.length(),sum);
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
                updateProductDetails(finalJsonArray.toString(),String.valueOf(user_Id));

            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
    private void updateProductDetails(String product_detail,String product_id){
        virtualShoopingCartArrayList=new ArrayList<>();
        virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
        for(int i=0;i<virtualShoopingCartArrayList.size();i++){
            if(virtualShoopingCartArrayList.get(i).getId().equals(product_id)){
                db.updateProduct(product_detail,product_id);
            }
        }


    }
    public interface ArraySize {
        public void getarraySize(int size,int sum);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout weight_layout;
        public TextView tv_Name,tv_price,tv_item_increment,tv_units,tv_mrp;
        public ImageView img,img_delete;
        public Button bnt_add,bnt_plus,bnt_minu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name=itemView.findViewById(R.id.product_name);
            tv_price=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.image_view);
            img_delete=itemView.findViewById(R.id.img_delete);
            tv_units=itemView.findViewById(R.id.unit);
            tv_item_increment=itemView.findViewById(R.id.item_increment);
            bnt_plus=itemView.findViewById(R.id.btn_plus);
            bnt_minu=itemView.findViewById(R.id.btn_minus);
            weight_layout=itemView.findViewById(R.id.weight_layout);
            tv_mrp=itemView.findViewById(R.id.MRP);
        }
    }
}
