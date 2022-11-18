package com.example.emotionbasedmusicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotionbasedmusicplayer.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ResultsActivity extends AppCompatActivity {

    TextView result;
    public static String Results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toast.makeText(ResultsActivity.this, "Results" +getIntent().getStringExtra("results") , Toast.LENGTH_SHORT).show();

        result = findViewById(R.id.emotion);

        if (TextUtils.isEmpty(Results)) {
            Results = getIntent().getStringExtra("results");
            result.setText(Results);
        }
    }

    public void goBack(View view) {
//        Intent intent = new Intent(ResultsActivity.this, CameraActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

}