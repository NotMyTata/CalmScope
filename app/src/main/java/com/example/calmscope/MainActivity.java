package com.example.calmscope;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "best_float32.tflite";
    private static final String LABEL_PATH = "labelmap.txt";
    private Interpreter interpreter;
    private int tensorWidth, tensorHeight, numChannel, numElements;
    private List<String> labels = new ArrayList<>();
    private ImageView imageView;
    private Button takePictureBtn;
    private TextView outputTxt;
    private static final float CONFIDENCE_THRESHOLD = 0.1f;
    private static final float IOU_THRESHOLD = 0.5f;

    String namaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.previewView);
        takePictureBtn = findViewById(R.id.captureBtn);
        outputTxt = findViewById(R.id.outputTxt);
        initializeModel();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap bm = BitmapFactory.decodeFile(namaFile, new BitmapFactory.Options());
                imageView.setImageBitmap(bm);

                detectObjects(bm);
                Toast.makeText(this, "Image captured successfully!", Toast.LENGTH_LONG).show();
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

    private void initializeModel() {
        try {
            Interpreter.Options options = new Interpreter.Options();
            options.setNumThreads(4);
            interpreter = new Interpreter(FileUtil.loadMappedFile(this, MODEL_PATH), options);

            int[] inputShape = interpreter.getInputTensor(0).shape();
            int[] outputShape = interpreter.getOutputTensor(0).shape();

            tensorWidth = inputShape[1];
            tensorHeight = inputShape[2];
            numChannel = outputShape[1];
            numElements = outputShape[2];

            // Load labels
            loadLabels();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading model", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLabels() {
        try {
            InputStream inputStream = getAssets().open(LABEL_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                labels.add(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TensorImage preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, tensorWidth, tensorHeight, false);
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(resizedBitmap);
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new NormalizeOp(0f, 255f))  // Normalize pixel values to [0, 1]
                .add(new CastOp(DataType.FLOAT32))
                .build();

        return imageProcessor.process(tensorImage);
    }

    private void detectObjects(Bitmap bitmap) {
        TensorImage tensorImage = preprocessImage(bitmap);
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(new int[]{1, numChannel, numElements}, DataType.FLOAT32);

        interpreter.run(tensorImage.getBuffer(), outputBuffer.getBuffer());
        List<BoundingBox> boundingBoxes = processOutput(outputBuffer.getFloatArray());

        Bitmap resultBitmap = drawBoundingBoxes(bitmap, boundingBoxes);

        imageView.setImageBitmap(resultBitmap);

        StringBuilder result = new StringBuilder();

        StringBuilder resultString = new StringBuilder();
        for (BoundingBox box : boundingBoxes) {
            resultString.append(box.clsName).append(": ").append(box.cnf).append("\n");
            result.append(box.clsName);
        }

        outputTxt.setText(resultString.toString());

        if(result.toString().equals("sedih")){

        }

        Toast.makeText(this, "Object detection completed!", Toast.LENGTH_LONG).show();
    }

    private List<BoundingBox> processOutput(float[] outputArray) {
        List<BoundingBox> boundingBoxes = new ArrayList<>();
        for (int c = 0; c < numElements; c++) {
            float maxConf = -1.0f;
            int maxIdx = -1;
            for (int j = 4; j < numChannel; j++) {
                int arrayIdx = c + numElements * j;
                if (outputArray[arrayIdx] > maxConf) {
                    maxConf = outputArray[arrayIdx];
                    maxIdx = j - 4;
                }
            }

            if (maxConf > CONFIDENCE_THRESHOLD) {
                String className = labels.get(maxIdx);
                Log.d("ObjectDetection", "Detected: " + className + " with confidence: " + maxConf);

                float cx = outputArray[c];
                float cy = outputArray[c + numElements];
                float w = outputArray[c + numElements * 2];
                float h = outputArray[c + numElements * 3];
                float x1 = cx - (w / 2f);
                float y1 = cy - (h / 2f);
                float x2 = cx + (w / 2f);
                float y2 = cy + (h / 2f);

                if (x1 >= 0f && x1 <= 1f && y1 >= 0f && y1 <= 1f && x2 >= 0f && x2 <= 1f && y2 >= 0f && y2 <= 1f) {
                    boundingBoxes.add(new BoundingBox(x1, y1, x2, y2, cx, cy, w, h, maxConf, maxIdx, labels.get(maxIdx)));
                }
            }
        }

        return applyNMS(boundingBoxes);
    }

    private List<BoundingBox> applyNMS(List<BoundingBox> boxes) {
        List<BoundingBox> sortedBoxes = new ArrayList<>(boxes);
        sortedBoxes.sort((a, b) -> Float.compare(b.cnf, a.cnf)); // Sort by confidence
        List<BoundingBox> selectedBoxes = new ArrayList<>();

        while (!sortedBoxes.isEmpty()) {
            BoundingBox first = sortedBoxes.remove(0);
            selectedBoxes.add(first);

            sortedBoxes.removeIf(nextBox -> calculateIoU(first, nextBox) >= IOU_THRESHOLD);
        }

        return selectedBoxes;
    }

    private float calculateIoU(BoundingBox box1, BoundingBox box2) {
        float x1 = Math.max(box1.x1, box2.x1);
        float y1 = Math.max(box1.y1, box2.y1);
        float x2 = Math.min(box1.x2, box2.x2);
        float y2 = Math.min(box1.y2, box2.y2);
        float intersectionArea = Math.max(0f, x2 - x1) * Math.max(0f, y2 - y1);
        float box1Area = box1.w * box1.h;
        float box2Area = box2.w * box2.h;
        return intersectionArea / (box1Area + box2Area - intersectionArea);
    }

    private Bitmap drawBoundingBoxes(Bitmap bitmap, List<BoundingBox> boxes) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        for (BoundingBox box : boxes) {
            RectF rect = new RectF(box.x1 * mutableBitmap.getWidth(), box.y1 * mutableBitmap.getHeight(),
                    box.x2 * mutableBitmap.getWidth(), box.y2 * mutableBitmap.getHeight());
            canvas.drawRect(rect, paint);
            canvas.drawText(box.clsName, rect.left, rect.bottom, textPaint);
        }

        return mutableBitmap;
    }
}
