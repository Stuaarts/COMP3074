package com.example.ID101500671;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_WWW = findViewById(R.id.btn_WWW);
        btn_WWW.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.georgebrown.ca"));
            startActivity(intent);
        });

        Button btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(v-> {;
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4164155000"));
            startActivity(intent);
        });

        Button btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v-> {;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:43.6577,-79.3788?q= George Brown College"));
            startActivity(intent);
        });

        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> finish());
    }
}