package com.view.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.control.logic.HomeController;
import com.view.R;


public class HomeActivity extends AppCompatActivity {

    private TextView whoAmITextView;
    private HomeController homeController;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");

        whoAmITextView = (TextView) findViewById(R.id.whoAmITextView);
        whoAmITextView.setText("your JWToken: \n" + token);
        homeController = new HomeController(whoAmITextView, token, this);


    }

    public void whoAmI(View view) {
       homeController.whoAmI();
    }
}