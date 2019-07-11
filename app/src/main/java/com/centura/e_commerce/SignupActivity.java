package com.centura.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignupActivity extends BaseActivity {
    Button btn_SignIn;
    EditText et_userName,et_password,et_phoneNo,et_address,et_email;
    String str_userName,str_password,str_phoneNo,str_address,str_email;
    Snackbar snackbar;
    ImageView imageView_back;
    CoordinatorLayout coordinatorLayout;

    DatabaseHelper db;
    Context context;

    ArrayList<UserDetails> userDetailModelArrayList;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray = new JSONArray();

    SharedPreferences sharedPreferences;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = SignupActivity.this;
        db = new DatabaseHelper(context);
        coordinatorLayout=findViewById(R.id.SignUp_Container);
        et_userName=findViewById(R.id.et_UserName);
        et_phoneNo=findViewById(R.id.et_phoneNo);
        et_email=findViewById(R.id.et_email);
        et_address=findViewById(R.id.et_address);
        et_password=findViewById(R.id.et_password);
        btn_SignIn=findViewById(R.id.SignIn);
        imageView_back=findViewById(R.id.img_back);
        onClick();
    }
    private void onClick(){
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateValue();
            }
        });

        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validateValue();
                }
                return false;
            }
        });



    }

    private void validateValue(){
        str_userName=et_userName.getText().toString().trim();
        str_phoneNo=et_phoneNo.getText().toString().trim();
        str_email=et_email.getText().toString().trim();
        str_address=et_address.getText().toString().trim();

        str_password=et_password.getText().toString().trim();

        if(str_userName.isEmpty()){
            et_userName.setError("Enter UserName");
            snackbar_(coordinatorLayout,"Enter UserName");

        }
        else if(str_phoneNo.isEmpty()){
            et_phoneNo.setError("Enter PhoneNo");
            snackbar_(coordinatorLayout,"Enter PhoneNo");
        }
        else if(str_phoneNo.length()<10){
            et_phoneNo.setError("PhoneNo Length should be 10 digit.");
            snackbar_(coordinatorLayout,"PhoneNo Length should be 10 digit.");
        }
        else if(str_email.isEmpty()){
            et_email.setError("Enter Email");
            snackbar_(coordinatorLayout,"Enter Email");
        }
        else if(!isValidEmail(str_email)){
            et_email.setError("Enter Valid Email");
            snackbar_(coordinatorLayout,"Enter Valid Email");
        }
        else if(str_address.isEmpty()){
            et_address.setError("Enter BlockNo");
            snackbar_(coordinatorLayout,"Enter Address");
        }

        else if(str_password.isEmpty()){
            et_password.setError("Enter Password");
            snackbar_(coordinatorLayout,"Enter Password");
        }
        else if (str_password.length()<6){
            et_password.setError("Password should be of minimum 6 character or digit.");
            snackbar_(coordinatorLayout,"Password should be of minimum 6 character or digit.");
        }

        else {
            addUserDetail();
            sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("has Logged In", true);
            editor.putInt("user_id", (int) id);

            editor.commit();
            Intent intent = new Intent(context, UserDashboard.class);
            startActivity(intent);

        }
    }


    public  static Boolean isValidEmail(CharSequence target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void addUserDetail() {
        boolean count = false;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", str_userName);
            jsonObject.put("email", str_email);
            jsonObject.put("phoneNo", str_phoneNo);
            jsonObject.put("password", str_password);
            jsonObject.put("address",str_address);

            //jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userDetailModelArrayList = new ArrayList<>();
        userDetailModelArrayList.addAll(db.getAllUsersDetails());
        if (userDetailModelArrayList.size() == 0) {
            id = db.insertUserDetail(jsonObject.toString());

        } else {
            for (int i = 0; i < userDetailModelArrayList.size(); i++) {
                String json = userDetailModelArrayList.get(i).getUser_detail();
                try {

                    jsonObject1 = new JSONObject(json);
                    if (jsonObject1.getString("email").equals(str_email)) {
                        snackbar_(coordinatorLayout, "Already Exist.");
                        count = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (count == false) {
                id = db.insertUserDetail(jsonObject.toString());

            }

        }
    }

}
