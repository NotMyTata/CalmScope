package com.example.calmscope;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        String apiKey = "xtNjvb73MHyrib6LVzz5";
        String imageUri = "https://source.roboflow.com/5aQUIxk7OeaQuF5bOXf2AKjtuSr2/FKsD4p2WTT643JHbtUOj/original.jpg";

        Call<InferenceResponse> call = apiService.detectEmotions(apiKey, imageUri);

        call.enqueue(new Callback<InferenceResponse>() {
            @Override
            public void onResponse(Call<InferenceResponse> call, Response<InferenceResponse> response) {
                if(response.isSuccessful()){
                    InferenceResponse inferenceResponse = response.body();
                    System.out.println("Inference ID: " + inferenceResponse.getInferenceId());
                } else {
                    System.out.println("Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<InferenceResponse> call, Throwable t) {
                System.out.println("Network failure: " + t.getMessage());
            }
        });
    }
}