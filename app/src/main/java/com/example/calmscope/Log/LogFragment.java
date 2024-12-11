package com.example.calmscope.Log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Entities.Results;
import com.example.calmscope.R;
import com.example.calmscope.ResultsAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends Fragment {
    private static ResultsAdapter resultsAdapter;
    private static ArrayList<Results> resultsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        setDropdown(view);
        loadDatabase(view);

        return view;
    }

    private void setDropdown(View view){
        Spinner dropdownDate = view.findViewById(R.id.logSpinnerDate);

        String[] dateItems = new String[]{"All", "Before", "After", "During"};
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(
                this.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                dateItems);
        dropdownDate.setAdapter(dateAdapter);
    }

    private void loadDatabase(View view){
        CalmDB db = CalmDB.getInstance(getContext());

        ListView listView = view.findViewById(R.id.logListView);
        resultsList = new ArrayList<>();
        resultsAdapter = new ResultsAdapter(getContext(), R.layout.fragment_log, resultsList);

        resultsList.addAll(db.resultsDao().getAllDesc());

        listView.setAdapter(resultsAdapter);
    }

    private void insertResult(Results result){
        resultsList.add(result);
        resultsAdapter.notifyDataSetChanged();
    }

    private void insertResults(List<Results> results){
        resultsList.addAll(results);
        resultsAdapter.notifyDataSetChanged();
    }
}