package marko.mitrovic.singidroid4.slides;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitSlide1 extends Fragment implements ISlidePolicy{
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private Switch switchBtn;
    private String toggleID;
    private SharedViewModel viewModel;
    private Gson gson;
    private ApiCalls api;
    private boolean switchState;
    private ProgressBar progressBar;
    private View view;

    public static InitSlide1 newInstance(int layoutResId2) {
        InitSlide1 initSlide1 = new InitSlide1();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId2);
        initSlide1.setArguments(args);

        return initSlide1;
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
        progressBar = view.findViewById(R.id.progressbar);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);


        if (viewModel.getFacultiesArray().getValue() == null) {
            //getFaculties task = new getFaculties(getContext(), "appInit/getFaculties");
            progressBar.setVisibility(view.VISIBLE);

            api = AppNetworking.getClient(getContext()).create(ApiCalls.class);
            api.getFaculties().enqueue(new Callback<JsonArray>(){
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("Call", String.valueOf(response.body()));
                    viewModel.setFacultiesArray(response.body());

                    AddButton(response.body(), false);


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e("Call_getFaculties", "Failed, dumping stack trace:");
                    t.printStackTrace();
                }
            });
        }else{
                AddButton(viewModel.getFacultiesArray().getValue(),false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //savedInstanceState
    }


    @Override
    public boolean isPolicyRespected() {
        return switchState;

    }



    @Override
    public void onUserIllegallyRequestedNextPage() {
        Log.d("SampleSlide","Illegal Request made");
    }


    public void AddButton(JsonArray toSet, boolean b) {

        if (toSet == null) {
            return;
        }

        RadioGroup lin = view.findViewById(R.id.toggleGroup);
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
            newBtn.setTag(t.get("id").getAsString());
            newBtn.setId(ViewCompat.generateViewId());
            newBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.init_button));
            newBtn.setHeight(60);
            newBtn.setTextColor(Color.parseColor("#ffffff"));
            newBtn.setTextSize(18);

            if (newBtn.getText().toString().equals(toggleID)) {
                newBtn.setChecked(true);
            }

            newBtn.setOnClickListener(v -> {
                RadioGroup radioGroup = (RadioGroup) v.getParent();
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    ToggleButton toggleButton = (ToggleButton) radioGroup.getChildAt(j);
                    toggleButton.setChecked(v.getId() == toggleButton.getId());
                    if (v.getId() == toggleButton.getId()) {
                        toggleID = toggleButton.getText().toString();
                        viewModel.setSelectedFaculty(toggleButton.getTag().toString()); //Replace with var that'll be used to save to shared pref
                        Log.d("initSlide1", toggleButton.getTag().toString());
                    }

                    switchState = true;
                }

            });
            ;
            lin.addView(newBtn);
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) view.findViewById(newBtn.getId()).getLayoutParams();
            layoutParams.setMargins(70, 50, 70, 0);

        }



    }

}