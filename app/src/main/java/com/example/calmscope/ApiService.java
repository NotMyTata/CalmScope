package com.example.calmscope;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("deteksi-emosi-vbmak/2")
    Call<InferenceResponse> detectEmotions(
            @Query("api_key") String apiKey,
            @Query("image") String imageUrl
    );
}
