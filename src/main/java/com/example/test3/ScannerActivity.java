package com.example.test3;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ScannerActivity extends AppCompatActivity {


    private TextView textView;
    private Button Camera;
    private Bitmap imageBitmap;

    private ImageView CaptureIm;
    static final int REQUEST_IMAGE_CAPTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        CaptureIm = findViewById(R.id.idIVCaptureImage);
        textView = findViewById(R.id.textViewResult);
        Camera = findViewById(R.id.button6);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermission())
                {
                    captureimage();
                }
                else {
                    requestPermission();
                }
                detectText();
                checkPage();

            }
        });
    }

        private boolean checkPermission(){
            int camerPermission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
            return camerPermission == PackageManager.PERMISSION_GRANTED;
        }

        private void requestPermission(){
            int PERMISSION_CODE = 200;
            ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);

        }

        private void captureimage(){
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takepicture,REQUEST_IMAGE_CAPTURE);
            }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length >0)
        {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this,"Permissions granted..", Toast.LENGTH_SHORT).show();
                captureimage();
            }
            else {
                Toast.makeText(this,"Permissions denied..", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            CaptureIm.setImageBitmap(imageBitmap);
        }


    }
    private void checkPage()
    {
        // searches the markets website to identify the products from our receipt
        // and get info from their properties

    }

    private void detectText()
        {
            InputImage image = InputImage.fromBitmap(imageBitmap,0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    StringBuilder result = new StringBuilder();
                    for(Text.TextBlock block: text.getTextBlocks())
                    {
                        String blockText = block.getText();
                        Point[] blockCornerPoint = block.getCornerPoints();
                        Rect blockFrame = block.getBoundingBox();
                        for(Text.Line line : block.getLines())
                        {
                            String lineText = line.getText();
                            Point[] lineCornerPoint = line.getCornerPoints();
                            Rect linRec = line.getBoundingBox();
                            for(Text.Element element: line.getElements())
                            {
                                String elementText = element.getText();
                                result.append(elementText);

                            }
                            textView.setText(blockText);

                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ScannerActivity.this,"Fail to detect text from image"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
