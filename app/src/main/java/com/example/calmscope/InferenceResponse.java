package com.example.calmscope;

import android.gesture.Prediction;
import android.media.Image;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InferenceResponse {
    @SerializedName("inference_id")
    private String inferenceId;
    private double time;
    private Image image;
    private List<Prediction> predictions;

    public String getInferenceId() {
        return inferenceId;
    }

    public void setInferenceId(String inferenceId) {
        this.inferenceId = inferenceId;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }
}
