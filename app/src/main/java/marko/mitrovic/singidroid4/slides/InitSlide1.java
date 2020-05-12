package marko.mitrovic.singidroid4.slides;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.github.paolorotolo.appintro.ISlidePolicy;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import org.json.JSONArray;
import org.json.JSONException;

public class InitSlide1 extends Fragment implements ISlidePolicy {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private Switch switchBtn;
    private String toggleID;
    private SharedViewModel viewModel;

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
        switchBtn = view.findViewById(R.id.switch12);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        viewModel.getFacultiesArray().observe(getViewLifecycleOwner(), new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray jsonArray) {

            }
        });

        if(viewModel.getFacultiesArray().getValue() == null){
            getFaculties task = new getFaculties(getContext(),"getFaculties");
            task.execute();
        }else{
            try {
                AddButton(viewModel.getFacultiesArray().getValue(),false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //savedInstanceState
    }


    @Override
    public boolean isPolicyRespected() {
        /*try{
            if(switchBtn != null){
                switchState = switchBtn.isChecked();
            }else{
                switchState = false;
            }
        }catch (Exception e){
            Log.e("Error", String.valueOf(e));
        }*/

        if(switchState){
            return true;
        }else{
            return false;
        }

    }



    @Override
    public void onUserIllegallyRequestedNextPage() {
        Log.d("SampleSlide","Illegal Request made");
    }


    public void AddButton(JSONArray toSet, boolean b) throws JSONException {

        if(toSet == null){
            return;
        }
        RadioGroup lin = (RadioGroup) view.findViewById(R.id.toggleGroup);
        int buttonCount = lin.getChildCount();
        if(buttonCount != 0 && b){
            lin.removeAllViews();
        }
        for(int i = 0; i  < toSet.length();i++){
            ToggleButton newBtn = new ToggleButton(getContext());
            newBtn.setText(toSet.getJSONObject(i).getString("title"));
            newBtn.setTextOn(toSet.getJSONObject(i).getString("title"));
            newBtn.setTextOff(toSet.getJSONObject(i).getString("title"));
            newBtn.setTag(toSet.getJSONObject(i).getString("id"));
            newBtn.setId(ViewCompat.generateViewId());
            newBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.init_button));
            newBtn.setHeight(60);
            newBtn.setTextColor(Color.parseColor("#ffffff"));
            newBtn.setTextSize(18);

            if(newBtn.getText().toString().equals(toggleID)){
                newBtn.setChecked(true);
            }

            newBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RadioGroup radioGroup = (RadioGroup)v.getParent();
                    for (int j = 0; j < radioGroup.getChildCount(); j++) {
                        ToggleButton toggleButton = (ToggleButton) radioGroup.getChildAt(j);
                        toggleButton.setChecked(v.getId() == toggleButton.getId());
                        if(v.getId() == toggleButton.getId()) {
                            toggleID = toggleButton.getText().toString();
                            viewModel.setSelectedFaculty(toggleButton.getTag().toString()); //Replace with var that'll be used to save to shared pref
                            Log.d("initSlide1",toggleButton.getTag().toString());
                        }

                        switchState = true;
                    }

                }
            });;
            lin.addView(newBtn);
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) view.findViewById(newBtn.getId()).getLayoutParams();
            layoutParams.setMargins(70, 50, 70, 0);

        }



    }



    private class getFaculties extends AsyncTask<String,Void, JSONArray> {
        private Context mContext;
        private String faks;

        public getFaculties(Context mContext, String faks) {
            this.mContext = mContext;
            this.faks = faks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(view.VISIBLE);

        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            AppNetworking net = new AppNetworking();
            JSONArray response = net.SyncApiCall(mContext,faks);
            viewModel.setFacultiesArray(response);
            return response;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.d("getFacultiesTask", String.valueOf(jsonArray));
            progressBar.setVisibility(View.INVISIBLE);

            try {
                AddButton(jsonArray, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



}