package com.centura.e_commerce;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Place_OrderActivity extends AppCompatActivity {
    TextView tv_slot,
            tv_user_name,tv_email,tv_contactNo,
            tv_address, tv_amount;

    LinearLayout address_layout;

    private DatabaseHelper db;

    Toolbar toolbar;

    ArrayList<UserDetails> userDetailModelArrayList;
    JSONObject  jsonObject1;

    String str_total;

    Button btn_Place_Order;
    SharedPreferences sharedPreferences;
    Context context;


    int total,user_Id;

    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;


    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__order);
        context=Place_OrderActivity.this;
        db = new DatabaseHelper(context);

        coordinatorLayout=findViewById(R.id.Place_order_layout);

        tv_user_name=findViewById(R.id.tv_name);
        tv_address=findViewById(R.id.tv_address);

        tv_contactNo=findViewById(R.id.tv_phone_no);
        tv_amount=findViewById(R.id.total_amount);
        tv_email=findViewById(R.id.tv_email);

        address_layout=findViewById(R.id.address_layout);
        toolbar=findViewById(R.id.toolbar1);


        tv_slot=findViewById(R.id.tv_slot);
        btn_Place_Order=findViewById(R.id.btn_Place_Order);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Place_order",true);
                startActivity(intent);
                finish();
            }
        });

        sharedPreferences=getSharedPreferences("MyPrefe",Context.MODE_PRIVATE);
        user_Id=sharedPreferences.getInt("user_id",0);
        userDetails();


        Intent intent=getIntent();
        str_total=intent.getStringExtra("total");
        total=Integer.parseInt(str_total);
        tv_amount.setText(str_total);
        Log.d("total_value",str_total);
        onClick();


    }

    private void userDetails() {
        userDetailModelArrayList = new ArrayList<>();
        userDetailModelArrayList.addAll(db.getAllUsersDetails());
        for (int i=0;i<userDetailModelArrayList.size();i++){
            if(userDetailModelArrayList.get(i).getId()== user_Id){
                String json=userDetailModelArrayList.get(i).getUser_detail();
                try {
                    jsonObject1 = new JSONObject(json);
                    tv_user_name.setText(jsonObject1.getString("name"));
                    tv_email.setText(jsonObject1.getString("email"));
                    tv_contactNo.setText(jsonObject1.getString("phoneNo"));
                    tv_address.setText(jsonObject1.getString("address"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void onClick() {

        btn_Place_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Place Order").setMessage("Your order have been placed!!!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent=new Intent(context,UserDashboard.class);
                                startActivity(intent);
                            }
                        }).show();
                db.deleteProduct(String.valueOf(user_Id));

            }
        });


    }

}
