package com.example.calmscope.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calmscope.CalmDatabase.Entities.Results;
import com.example.calmscope.R;

import java.util.ArrayList;

public class LogAdapter extends ArrayAdapter<Results> {

    private static class ViewHolder{
        TextView emotion, risk, date;
    }

    public LogAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Results> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Results result = getItem(position);
        ViewHolder viewResults;

        if(convertView == null){
            viewResults = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_row, parent, false);

            viewResults.emotion = convertView.findViewById(R.id.resultEmotion);
            viewResults.risk = convertView.findViewById(R.id.resultRisk);
            viewResults.date = convertView.findViewById(R.id.resultDate);

        } else {
            viewResults = (ViewHolder) convertView.getTag();
        }

//        viewResults.emotion.setText(result.getEmotion());
//        if(result.isAtRisk()){
//            viewResults.risk.setText("Potential Health Risk");
//        } else {
//            viewResults.risk.setText("You're Safe");
//        }
//        viewResults.date.setText(result.getDate().toString());

        return convertView;
    }
}
