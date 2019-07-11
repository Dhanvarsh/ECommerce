package com.centura.e_commerce;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.UserDetails;
import com.centura.e_commerce.database.model.VirtualShoopingCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    Context context;
    Toolbar title;
    TextView tv_toolbar,shopping_cart,tv_name,tv_email;
    SearchView searchView;
    public String str_name,str_email,str_userId;
   private ArrayList<VirtualShoopingCart> virtualShoopingCartArrayList=new ArrayList<>();
   ArrayList<UserDetails> userDetailModelArrayList;
    int count,userId;
    private View notificationBadge;
    SharedPreferences sharedPreferences;
    private DatabaseHelper db;
    SharedPreferences.Editor editor;
    JSONArray jsonArray;
    JSONObject jsonObject, jsonObject1;
    View content_View;
    Snackbar snackbar;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        context=UserDashboard.this;
        db=new DatabaseHelper(context);
        //count=db.getProductCount();

        sharedPreferences=getSharedPreferences("MyPrefe",Context.MODE_PRIVATE);

        userId=sharedPreferences.getInt("user_id",0);
        userDetailModelArrayList = new ArrayList<>();
        userDetailModelArrayList.addAll(db.getAllUsersDetails());
        for (int i = 0; i < userDetailModelArrayList.size(); i++) {
            if (userDetailModelArrayList.get(i).getId() == userId) {
                String json = userDetailModelArrayList.get(i).getUser_detail();
                try {
                    jsonObject1 = new JSONObject(json);
                    str_email = jsonObject1.getString("email");
                    str_name = jsonObject1.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
       count=countProduct();
        title=findViewById(R.id.toolbar1);
        tv_toolbar=findViewById(R.id.texttoolbar);
        setSupportActionBar(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tv_toolbar.setText("Home");
        loadFragment(new HomeFragment());


        drawer = findViewById(R.id.drawer_home_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);
        tv_name=headerView.findViewById(R.id.tv_name);
        tv_name.setText(str_name);
        tv_email=headerView.findViewById(R.id.tv_email);
        tv_email.setText(str_email);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, title,
                R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationItemView notificationsTab = (BottomNavigationItemView)findViewById (R.id.nav_cart);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.customize_action_bar, notificationsTab, false);
        shopping_cart = notificationBadge.findViewById(R.id.cart_badge);

        shopping_cart.setText(String.valueOf(count));
        notificationsTab.addView(notificationBadge);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private int countProduct() {
        int len = 0;
        virtualShoopingCartArrayList.addAll(db.getAllProductDetails());
        if(virtualShoopingCartArrayList.size()>0){
            for(int i=0;i<virtualShoopingCartArrayList.size();i++){
                if(virtualShoopingCartArrayList.get(i).getId().equals(String.valueOf(userId))){
                    String json=virtualShoopingCartArrayList.get(i).getProduct();
                    try {
                        jsonArray = new JSONArray(json);
                        len=jsonArray.length();
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        else {
            len=0;}
        return len;
    }

    private void loadFragment(Fragment fragment) {

        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                tv_toolbar.setText("Home");
                loadFragment(new HomeFragment() );
                return true;


            case R.id.nav_cart:
                tv_toolbar.setText("My Cart");
                loadFragment(new ShoppingCartFragment());
                return true;

            case R.id.nav_singout:
                signout();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }


    public void updateData(String data){

        shopping_cart.setText(data);

    }

    @Override
    public void onBackPressed() {

        int selectedId=bottomNavigationView.getSelectedItemId();
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            if(R.id.nav_home !=selectedId){
                bottomNavigationView.setSelectedItemId(R.id.nav_home);

            }
            else {
                super.onBackPressed();
                ActivityCompat.finishAffinity(this);
            }
        }

    }

    void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leaving already ?")
                .setMessage("Are you sure you want to sign out ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(context,LoginActivity.class);
                        startActivity(intent);
                        sharedPreferences=getSharedPreferences("MyPrefe",context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("has Logged In",false);
                        editor.commit();
                        editor.apply();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();

        if(intent.getBooleanExtra("Product_view",false)||intent.getBooleanExtra("Place_order",false))
        {
            loadFragment(new ShoppingCartFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
            drawer.closeDrawer(GravityCompat.START);

        }

    }

}

