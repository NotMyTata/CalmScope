package com.example.calmscope;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Interpreter tflite;
    private String modelPath = "best_float32.tflite";
    Button takePictureBtn;
    ImageView fotoView;
    private final Integer kode_kamera = 222;
    private final Integer image_size = 640;

    String namaFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            tflite = new Interpreter(FileUtil.loadMappedFile(this,modelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] inputShape = tflite.getInputTensor(0).shape();
        Log.d("TensorFlow", "Model input shape: " + Arrays.toString(inputShape));


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        takePictureBtn = findViewById(R.id.captureBtn);
        fotoView = findViewById(R.id.previewView);

        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Process the image from the result
                Bitmap bm = BitmapFactory.decodeFile(namaFile, new BitmapFactory.Options());
                fotoView.setImageBitmap(bm);
                Toast.makeText(this, "Image captured successfully!", Toast.LENGTH_LONG).show();

                if (tflite != null) {
                    // Preprocess and classify the image
                    float[][][] output = classifyImage(bm);
                    displayPrediction(output);
                } else {
                    // Log an error or handle the null tflite object here
                    Toast.makeText(this, "Interpreter is not initialized", Toast.LENGTH_SHORT).show();
                }
            }
        });

        takePictureBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagesFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CalmScope");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            Date d = new Date();
            namaFile = imagesFolder.getAbsolutePath() + File.separator + DateFormat.format("MM-dd-yy_hh-mm-ss", d.getTime()) + ".jpg";
            File file = new File(namaFile);
            Uri uriSavedImage = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            takePictureLauncher.launch(intent);
        });

    }

    // Function to find the index of the maximum value
    // Function to find the predicted emotion class (highest confidence score)
    private int argmax(float[][][] output) {
        int predictedClassIndex = -1;
        float maxConfidence = Float.MIN_VALUE;

        // Iterate over each emotion class (8 emotions)
        for (int emotionIndex = 0; emotionIndex < output[0].length; emotionIndex++) {
            // Calculate the maximum confidence for this emotion class across all 8400 values
            float maxEmotionConfidence = Float.MIN_VALUE;
            for (int i = 0; i < output[0][emotionIndex].length; i++) {
                if (output[0][emotionIndex][i] > maxEmotionConfidence) {
                    maxEmotionConfidence = output[0][emotionIndex][i];
                }
            }

            // Update the predicted class if the current emotion's confidence is higher than the previous max
            if (maxEmotionConfidence > maxConfidence) {
                maxConfidence = maxEmotionConfidence;
                predictedClassIndex = emotionIndex;
            }
        }

        return predictedClassIndex;
    }



    private void displayPrediction(float[][][] output) {
        int predictedClassIndex = argmax(output);

        // Here you can map predicted class index to the actual emotion label
        String[] emotionClasses = {
                "Happy", "Sad", "Angry", "Surprised", "Disgusted", "Fearful", "Neutral", "Bored"
        };

        // Get the confidence value for the predicted class
        float maxConfidence = Float.MIN_VALUE;
        for (int i = 0; i < output[0][predictedClassIndex].length; i++) {
            if (output[0][predictedClassIndex][i] > maxConfidence) {
                maxConfidence = output[0][predictedClassIndex][i];
            }
        }

        // Show the predicted emotion and its confidence
        String predictionMessage = "Predicted Emotion: " + emotionClasses[predictedClassIndex] +
                "\nConfidence: " + maxConfidence;

        Toast.makeText(this, predictionMessage, Toast.LENGTH_LONG).show();
        Log.d("TensorFlow", predictionMessage);
    }



    public float[][][] classifyImage(Bitmap bitmap) {
        // Resize the image to match the input size expected by the model (640x640)
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, image_size, image_size, true);

        // Create a ByteBuffer to hold the image data
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 1 * image_size * image_size * 3); // 1 image, 640x640x3
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[image_size * image_size];
        resizedBitmap.getPixels(intValues, 0, image_size, 0, 0, image_size, image_size);

        // Normalize the pixel values (scale them to [0, 1] range) and put them in the byteBuffer
        for (int i = 0; i < intValues.length; i++) {
            int pixel = intValues[i];
            float r = ((pixel >> 16) & 0xFF) / 255.0f;
            float g = ((pixel >> 8) & 0xFF) / 255.0f;
            float b = (pixel & 0xFF) / 255.0f;

            byteBuffer.putFloat(r);  // Put each channel's normalized value
            byteBuffer.putFloat(g);
            byteBuffer.putFloat(b);
        }

        // Create an array to store the model's output
        float[][][] output = new float[1][8][8400]; // Adjust output shape according to your model

        // Run the model
        tflite.run(byteBuffer, output);

        // Return the model's output as the result
        return output;  // You can process this output further based on what the model returns
    }





}
