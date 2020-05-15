package marko.mitrovic.singidroid4.slides;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.github.paolorotolo.appintro.ISlidePolicy;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import org.json.JSONArray;
import org.json.JSONException;


public class InitSlide3 extends Fragment implements ISlidePolicy {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private SharedViewModel viewModel;
    private String toggleID;
    private View view;
    private ProgressBar progressBar;

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




        viewModel.getSelectedYear().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String input) {
                String FacultiesAndYears = "?faks=" + viewModel.getSelectedFaculty().getValue() + "&year=" + viewModel.getSelectedYear().getValue();
                getCourses task = new getCourses(getContext(), "appInit/getCourse" + FacultiesAndYears);
                task.execute();
            }
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

    public void AddButton(JSONArray toSet, boolean b) throws JSONException {
        Log.d("AddButtonInitSlide2", toSet.toString());
        if (toSet == null) {
            return;
        }
        RadioGroup lin = (RadioGroup) view.findViewById(R.id.toggleGroupUserInit3);
        int buttonCount = lin.getChildCount();
        if (buttonCount != 0 && b) {
            lin.removeAllViews();
        }
        for (int i = 0; i < toSet.length(); i++) {
            ToggleButton newBtn = new ToggleButton(getContext());
            newBtn.setText(toSet.getJSONObject(i).getString("title"));
            newBtn.setTextOn(toSet.getJSONObject(i).getString("title"));
            newBtn.setTextOff(toSet.getJSONObject(i).getString("title"));
            newBtn.setTag(toSet.getJSONObject(i).getString("courses_id"));
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





    private class getCourses extends AsyncTask<String, Void, JSONArray> {
        private Context mContext;
        private String module;

        public getCourses(Context mContext, String module) {
            this.mContext = mContext;
            this.module = module;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(view.VISIBLE);

        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            AppNetworking net = new AppNetworking();
            JSONArray response = net.SyncApiCall(mContext, module);
            return response;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.d("getCoursesTask", String.valueOf(jsonArray));
            progressBar.setVisibility(View.INVISIBLE);

            try {
                AddButton(jsonArray, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}

