package com.thanguit.imusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.thanguit.imusic.API.APIService;
import com.thanguit.imusic.API.DataService;
import com.thanguit.imusic.R;
import com.thanguit.imusic.SharedPreferences.DataLocalManager;
import com.thanguit.imusic.animations.ScaleAnimation;
import com.thanguit.imusic.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalPageActivity extends AppCompatActivity {
    private ImageView ivBack;
    private ImageView civAvatarFrame;
    private TextView tvPersonalName;
    private TextView tvYourInfoName;
    private TextView tvYourInfoEmail;
    private Button btnLogout;

    private ScaleAnimation scaleAnimation;

    private ArrayList<User> userArrayList;

    private static final String TAG = "PersonalPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        DataLocalManager.init(this);

        Mapping();
        Event();
    }

    private void Mapping() {
        this.ivBack = findViewById(R.id.ivBack);
        this.civAvatarFrame = findViewById(R.id.civAvatarFrame);

        this.tvPersonalName = findViewById(R.id.tvPersonalName);
        this.tvPersonalName.setSelected(true); // Text will be moved

        this.tvYourInfoName = findViewById(R.id.tvYourInfoName);
        this.tvYourInfoName.setSelected(true); // Text will be moved

        this.tvYourInfoEmail = findViewById(R.id.tvYourInfoEmail);
        this.tvYourInfoEmail.setSelected(true); // Text will be moved

        this.btnLogout = findViewById(R.id.btnLogout);

        Handler_Display_Info_User(DataLocalManager.getUserID());
    }

    private void Event() {
        this.scaleAnimation = new ScaleAnimation(PersonalPageActivity.this, this.ivBack);
        this.scaleAnimation.Event_ImageView();
        this.ivBack.setOnClickListener(v -> finish());

        this.scaleAnimation = new ScaleAnimation(PersonalPageActivity.this, this.btnLogout);
        this.scaleAnimation.Event_Button();
        this.btnLogout.setOnClickListener(v -> {
            Open_Dialog(Gravity.CENTER);
        });
    }

    private void Open_Dialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_signout);

        Window window = (Window) dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Set màu mờ mờ cho background dialog, che đi activity chính, nhưng vẫn có thể thấy được một phần

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true); // Bấm ra chỗ khác sẽ thoát dialog

        Button btnDialog1 = dialog.findViewById(R.id.btnDialog1);
        Button btnDialog2 = dialog.findViewById(R.id.btnDialog2);

        this.scaleAnimation = new ScaleAnimation(PersonalPageActivity.this, btnDialog1);
        this.scaleAnimation.Event_Button();
        btnDialog1.setOnClickListener(v -> {
            dialog.dismiss();
        });

        this.scaleAnimation = new ScaleAnimation(PersonalPageActivity.this, btnDialog2);
        this.scaleAnimation.Event_Button();
        btnDialog2.setOnClickListener(v -> {
            dialog.dismiss();
            LoginManager.getInstance().logOut();
            DataLocalManager.deleteAllData();
            finish();
            moveTaskToBack(true);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, R.string.toast9, Toast.LENGTH_SHORT).show();
        });

        dialog.show(); // câu lệnh này sẽ hiển thị Dialog lên
    }

    private void Handler_Display_Info_User(String userID) {
        DataService dataService = APIService.getService();
        Call<List<User>> callBack = dataService.getUserFromID(userID);
        callBack.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userArrayList = (ArrayList<User>) response.body();
                if (userArrayList != null) {
                    Picasso.get()
                            .load(userArrayList.get(0).getImg())
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(civAvatarFrame);
                    tvPersonalName.setText(userArrayList.get(0).getName());
                    tvYourInfoName.setText(userArrayList.get(0).getName());
                    tvYourInfoEmail.setText(userArrayList.get(0).getEmail());

                    Log.d(TAG, "User Infomation: " + userArrayList.get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Handler_Display_Info_User(Error): " + t.getMessage());
            }
        });
    }
}