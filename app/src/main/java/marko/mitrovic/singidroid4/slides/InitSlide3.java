package marko.mitrovic.singidroid4.slides;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InitSlide3 extends Fragment implements ISlidePolicy {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private SharedViewModel viewModel;
    private String toggleID;
    private View view;
    private ProgressBar progressBar;
    private ApiCalls api;

    public static InitSlide3 newInstance(int layoutResId2) {
        InitSlide3 InitSlide3 = new InitSlide3();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId2);
        InitSlide3.setArguments(args);
        return InitSlide3;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            this.layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(this.layoutResId, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.userinit3progressbar);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);


        viewModel.getSelectedYear().observe(getViewLifecycleOwner(), input -> {
            progressBar.setVisibility(view.VISIBLE);
            api = AppNetworking.getClient().create(ApiCalls.class);
            String faks = viewModel.getSelectedFaculty().getValue();
            String year = viewModel.getSelectedYear().getValue();

            api.getCourse(faks, year).enqueue(new Callback<JsonArray>(){
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("Call", String.valueOf(response.body()));
                    viewModel.setCoursesArray(response.body());

                    AddButton(response.body(), true);


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e("Call", "Failed, dumping stack trace:");
                    Log.e("Get Faculties Failed", call + " " + t);
                }
            });
        });


    }

    @Override
    public boolean isPolicyRespected() {
        return true;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Log.d("SampleSlide", "Illegal Request made 2");
    }

    public void AddButton(JsonArray toSet, boolean b) {
        Log.d("AddButtonInitSlide2", toSet.toString());
        if (toSet == null) {
            return;
        }
        RadioGroup lin = (RadioGroup) view.findViewById(R.id.toggleGroupUserInit3);
        int buttonCount = lin.getChildCount();
        if (buttonCount != 0 && b) {
            lin.removeAllViews();
        }
        for (int i = 0; i < toSet.size(); i++) {
            JsonObject t = toSet.get(i).getAsJsonObject();

            ToggleButton newBtn = new ToggleButton(getContext());
            newBtn.setText(t.get("title").getAsString());
            newBtn.setTextOn(t.get("title").getAsString());
            newBtn.setTextOff(t.get("title").getAsString());
            newBtn.setTag(t.get("courses_id").getAsString());
            newBtn.setId(ViewCompat.generateViewId());
            newBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.init_button));
            newBtn.setHeight(60);
            newBtn.setTextColor(Color.parseColor("#ffffff"));
            newBtn.setTextSize(18);

            if (newBtn.getText().toString().equals(toggleID)) {
                newBtn.setChecked(true);
            }

            newBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RadioGroup radioGroup = (RadioGroup) v.getParent();
                    for (int j = 0; j < radioGroup.getChildCount(); j++) {
                        ToggleButton toggleButton = (ToggleButton) radioGroup.getChildAt(j);
                        toggleButton.setChecked(v.getId() == toggleButton.getId());
                        if (v.getId() == toggleButton.getId()) {
                            toggleID = toggleButton.getText().toString();
                            viewModel.setSelectedCourse(toggleButton.getTag().toString());
                            Log.d("initSlide3", toggleButton.getTag().toString());
                        }


                    }

                }
            });
            ;
            lin.addView(newBtn);
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) view.findViewById(newBtn.getId()).getLayoutParams();
            layoutParams.setMargins(70, 50, 70, 0);

        }


    }
}

