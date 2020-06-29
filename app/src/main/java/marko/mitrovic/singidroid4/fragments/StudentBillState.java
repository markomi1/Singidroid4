package marko.mitrovic.singidroid4.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class StudentBillState extends Fragment{
    private View view;
    private EditText index, jmbg;
    private TextView billState, billPayin, billPayout;
    private CheckBox saveCheckbox;
    private Button btn;
    private ApiCalls api;
    private SharedPreferences studentPerfs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_bill, container, false);
        index = view.findViewById(R.id.student_index_number);
        jmbg = view.findViewById(R.id.student_jmbg_number);
        btn = view.findViewById(R.id.student_bill_sendrequest);
        billState = view.findViewById(R.id.student_bill_state);
        billPayin = view.findViewById(R.id.student_last_payin);
        billPayout = view.findViewById(R.id.student_last_payout);
        saveCheckbox = view.findViewById(R.id.save_info_checkbox);
        api = AppNetworking.getClient(getContext()).create(ApiCalls.class);
        studentPerfs = getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE); //Prepare shared preferences

        String indexSaved = studentPerfs.getString("studentIndex", "");
        String jmbgSaved = studentPerfs.getString("studentJmbg", "");

        index.setText(indexSaved);
        jmbg.setText(jmbgSaved);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String indexText = index.getText().toString();
                String jmbgText = jmbg.getText().toString();
                if (indexText.isEmpty()) {
                    Toast.makeText(getActivity(), "Index field is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (jmbgText.isEmpty()) {
                    Toast.makeText(getActivity(), "JMBG field is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (indexText.length() < 10) {
                    Toast.makeText(getActivity(), "Index field is less than 10", Toast.LENGTH_SHORT).show();
                    return;
                } else if (jmbgText.length() < 13) {
                    Toast.makeText(getActivity(), "JMBG field is less than 13", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkStudentBalance(indexText, jmbgText);
                if (saveCheckbox.isChecked()) {
                    Toast.makeText(getActivity(), "Info saved", Toast.LENGTH_SHORT).show();
                    saveToSharedPred(indexText, jmbgText);
                }
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        return view;
    }

    public void checkStudentBalance(String index, String jmbg) {
        api.getStudentBalance(index, jmbg).enqueue(new Callback<List<String>>(){
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d("Response", response.body().toString());
                if (response.body().size() == 3) {
                    billState.setText(response.body().get(0));
                    billPayin.setText(response.body().get(1));
                    billPayout.setText(response.body().get(2));
                } else {
                    billState.setText(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    private void saveToSharedPred(String index, String jmbg) {
        studentPerfs.edit().putString("studentIndex", index).apply();
        studentPerfs.edit().putString("studentJmbg", jmbg).apply();
    }
}
