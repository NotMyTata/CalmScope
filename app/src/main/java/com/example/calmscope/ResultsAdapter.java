package com.example.calmscope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Entities.Results;

import java.util.ArrayList;

public class ResultsAdapter extends ArrayAdapter<Results> {

    private static class ViewHolder{
        TextView emotion, risk, date;
    }

    public ResultsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Results> objects) {
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

            convertView.setTag(viewResults);
        } else {
            viewResults = (ViewHolder) convertView.getTag();
        }

        CalmDB db = CalmDB.getInstance(getContext());

        viewResults.emotion.setText(db.emotionsDao().getTypeById(result.getEmotionId()));
        if(db.emotionsDao().getRiskById(result.getEmotionId())){
            viewResults.risk.setText("Potential Health Risk");
        } else {
            viewResults.risk.setText("Safe");
        }
        viewResults.date.setText(result.getDate().toString());

        return convertView;
    }
}
