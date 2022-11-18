package com.example.emotionbasedmusicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotionbasedmusicplayer.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnOpen,next;
    int imageSize = 224;
    Bitmap captureImage;
    private String results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        btnOpen = findViewById(R.id.btnRetake);
        next = findViewById(R.id.btnNext);

        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraActivity.this, ResultsActivity.class);
                intent.putExtra("results", results);
                startActivity(intent);
            }
        });

        // camera loads when application starts. If you want to make it through button, comment this two lines and uncomment line 40-46
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,100);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Powerbank", "Remote", "Alexa"};

            Toast.makeText(CameraActivity.this, "Results" +classes[maxPos] , Toast.LENGTH_SHORT).show();

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }

            model.close();

            results=classes[maxPos];

//            proceedFurther(classes[maxPos]);

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private boolean haveNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void complaint(String image) {
        if (!(TextUtils.isEmpty(image))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Image",image);
        } else {
            Toast.makeText(CameraActivity.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void proceedFurther(String final_results) {
        if (haveNetwork()) {

            String results = final_results;
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("results", results);
            this.startActivity(intent);

//            imageView = (ImageView)findViewById(R.id.imageView);
//            String imgdata=getStringImage(captureImage);
//            complaint(imgdata);
//            Toast.makeText(CameraActivity.this, "Image Is" + imgdata, Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(CameraActivity.this, ResultsActivity.class);
//            startActivity(intent);

        } else {
            Toast.makeText(CameraActivity.this, "Unable to Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Toast.makeText(CameraActivity.this, "Result Code " + resultCode, Toast.LENGTH_SHORT).show();
            captureImage = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(captureImage.getWidth(), captureImage.getHeight());
            captureImage = ThumbnailUtils.extractThumbnail(captureImage, dimension, dimension);
            imageView.setImageBitmap(captureImage);

            Toast.makeText(CameraActivity.this, "Capture Image " + captureImage, Toast.LENGTH_SHORT).show();

            captureImage = Bitmap.createScaledBitmap(captureImage, imageSize, imageSize, false);
            classifyImage(captureImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}