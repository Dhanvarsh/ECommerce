package com.centura.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.centura.e_commerce.database.DatabaseHelper;
import com.centura.e_commerce.database.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {
    Button bnt_login;
    TextView txt_ctreat_account;
    EditText et_username, et_password;
    String str_password, str_username;
    Context context;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;

    DatabaseHelper db;
    ArrayList<UserDetails> userDetailModelArrayList;
    JSONObject  jsonObject1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
        coordinatorLayout = findViewById(R.id.Login_container);
        context = LoginActivity.this;
        db=new DatabaseHelper(context);
        bnt_login = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        txt_ctreat_account = findViewById(R.id.create_account);
        onclick();
    }

    private void onclick() {
        txt_ctreat_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        bnt_login.setOnClickListener(new View.OnClickListener() {
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

    private void validateValue() {
        str_username = et_username.getText().toString().trim();
        str_password = et_password.getText().toString().trim();

        if (str_username.isEmpty()) {
            et_username.setError("Enter UserName");
            snackbar_(coordinatorLayout, "Enter UserName");
        } else if (str_password.isEmpty()) {
            et_password.setError("Enter Password");
            snackbar_(coordinatorLayout, "Enter Password");
        } else {

           authentication();

        }
    }


        private void authentication () {
            boolean count = false;
            userDetailModelArrayList = new ArrayList<>();
            userDetailModelArrayList.addAll(db.getAllUsersDetails());
            if (userDetailModelArrayList.size() == 0) {
                snackbar_(coordinatorLayout, "Register yourself by clicking on NewUser");
            } else {
                for (int i = 0; i < userDetailModelArrayList.size(); i++) {
                    String json = userDetailModelArrayList.get(i).getUser_detail();
                    try {

                        jsonObject1 = new JSONObject(json);
                        if ((jsonObject1.getString("email").equals(str_username)
                                || jsonObject1.getString("phoneNo").equals(str_username))
                                && jsonObject1.getString("password").equals(str_password)) {
                            sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("has Logged In", true);
                            editor.putInt("user_id", userDetailModelArrayList.get(i).getId());
                            editor.putString("email", jsonObject1.getString("email"));
                            editor.commit();
                            count = true;
                            Intent intent = new Intent(context, UserDashboard.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (count == false) {
                    snackbar_(coordinatorLayout, "Id/Password is Invalid");
                }


            }

        }


        @Override
        public void onBackPressed () {
            super.onBackPressed();
            ActivityCompat.finishAffinity(this);
        }
    }




