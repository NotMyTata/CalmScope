package com.example.calmscope.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Daos.UsersDao;
import com.example.calmscope.CalmDatabase.DateUtils;
import com.example.calmscope.CalmDatabase.Entities.Results;
import com.example.calmscope.R;
import com.example.calmscope.ResultsAdapter;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static ResultsAdapter resultsAdapter;
    private static ArrayList<Results> resultsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadDatabase(view);

        return view;
    }

    private void loadDatabase(View view){
        CalmDB db = CalmDB.getInstance(getContext());

        // Result List

        TextView header = view.findViewById(R.id.homeWelcome);
        SharedPreferences prefs = getContext().getSharedPreferences("com.example.calmscope", Context.MODE_PRIVATE);
        header.setText("Welcome " + prefs.getString("currentUser", ""));

        ListView listView = view.findViewById(R.id.homeRecapListView);
        resultsList = new ArrayList<>();
        resultsAdapter = new ResultsAdapter(getContext(), R.layout.fragment_home, resultsList);
        resultsList.addAll(db.resultsDao().fetchLastThree());
        listView.setAdapter(resultsAdapter);

        // Statistics

        Date thisWeekStart = DateUtils.getThisWeekStart();
        Date thisWeekEnd = DateUtils.getThisWeekEnd();
        int thisWeekCount = db.resultsDao().countFromDate(thisWeekStart, thisWeekEnd);

        Date thisMonthStart = DateUtils.getThisMonthStart();
        Date thisMonthEnd = DateUtils.getThisMonthEnd();
        int thisMonthCount = db.resultsDao().countFromDate(thisMonthStart, thisMonthEnd);

        Date thisYearStart = DateUtils.getThisYearStart();
        Date thisYearEnd = DateUtils.getThisYearEnd();
        int thisYearCount = db.resultsDao().countFromDate(thisYearStart, thisYearEnd);

        int total = db.resultsDao().getTotal();

        TextView amountWeek = view.findViewById(R.id.homeAmountCheckWeek);
        TextView amountMonth = view.findViewById(R.id.homeAmountCheckMonth);
        TextView amountYear = view.findViewById(R.id.homeAmountCheckYear);
        TextView amountTotal = view.findViewById(R.id.homeAmountCheckTotal);

        amountWeek.setText(Integer.toString(thisWeekCount));
        amountMonth.setText(Integer.toString(thisMonthCount));
        amountYear.setText(Integer.toString(thisYearCount));
        amountTotal.setText(Integer.toString(total));
    }
}