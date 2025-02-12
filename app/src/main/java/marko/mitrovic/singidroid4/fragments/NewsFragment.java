package marko.mitrovic.singidroid4.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import marko.mitrovic.singidroid4.Article;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.newsPager.PaginationAdapter;
import marko.mitrovic.singidroid4.newsPager.PaginationScrollListener;
import marko.mitrovic.singidroid4.repo.NewsModel;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.util.RecyclerViewClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private static final int PAGE_START = 0;
    private RecyclerView recyclerView;
    private PaginationAdapter paginationAdapter;
    private ApiCalls api;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int TOTAL_PAGES = 50;
    private int currentPage = PAGE_START;
    private SharedPreferences studentPerfs;
    private SharedViewModel viewModel;

    SwipeRefreshLayout swipeLayout;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class); //get repo
        RecyclerView recyclerView = view.findViewById(R.id.news_recycler_view);
        studentPerfs = this.getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        api = AppNetworking.getClient(getContext()).create(ApiCalls.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerViewClickListener listener = (view, title, permalink, date, imageUrl, content, imageList) -> {
            Intent intent = new Intent(getActivity(), Article.class);
            intent.putExtra("title", title);
            intent.putExtra("permalink", permalink);
            intent.putExtra("date", date);
            intent.putExtra("imageurl", imageUrl);
            intent.putExtra("content", content);
            intent.putExtra("imageList", (ArrayList<String>) imageList);
            startActivity(intent);
        };
        paginationAdapter = new PaginationAdapter(getContext(), listener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paginationAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager){
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage(studentPerfs.getString("Categories", "3,4"));
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage(studentPerfs.getString("Categories", "3,4"), false);
        viewModel.getSelectedNewsSource().observe(getViewLifecycleOwner(), s -> loadFirstPage(s, true));
        return view;
    }

    private void loadNextPage(String faksID) {

        api.getNews(faksID, String.valueOf(currentPage)).enqueue(new Callback<List<NewsModel>>(){
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                paginationAdapter.removeLoadingFooter();
                isLoading = false;

                List<NewsModel> results = response.body();
                paginationAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) {
                    paginationAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Log.e("Call_getNews", "Failed, dumping stack trace:");
                t.printStackTrace();
                Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadFirstPage(String faksID, Boolean refreshing) {
        api.getNews(faksID, "0").enqueue(new Callback<List<NewsModel>>(){
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                List<NewsModel> results = response.body();
                //Log.d("NewsFragment", results.toString());
                if (results == null) {
                    Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
                    return;
                }
                if (refreshing) {
                    paginationAdapter.newsList.clear(); //Clear the newsList in adapter
                    currentPage = 0; //We have to reset this because it may be something greater than 0
                    paginationAdapter.addAll(results); //Add all the new stuff in
                    paginationAdapter.notifyDataSetChanged(); //Notify the adapter that data has changed
                    swipeLayout.setRefreshing(false); //Turn off the refresh spinner
                } else {
                    paginationAdapter.addAll(results); //In case refresh flag isn't set
                }
                if (currentPage <= TOTAL_PAGES) {
                    paginationAdapter.addLoadingFooter();
                }
            }
            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Log.e("Call_getNews_0_Page", "Failed, dumping stack trace:");
                t.printStackTrace();
                Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeLayout = view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        int swiperColor = Color.parseColor(studentPerfs.getString("Color", "#A8011D"));// Sets it at first run
        swipeLayout.setColorSchemeColors(swiperColor); //Not gonna change the color of it immediately but it'll have a nice animation doing so
    }

    @Override
    public void onRefresh() {

        int swiperColor = Color.parseColor(studentPerfs.getString("Color", "#A8011D"));
        swipeLayout.setColorSchemeColors(swiperColor); //Not gonna change the color of it immediately but it'll have a nice animation doing so


        loadFirstPage(studentPerfs.getString("Categories", "3,4"), true); //Passes data from shared preferences

        paginationAdapter.notifyDataSetChanged(); //We notify it here again just in case

        Toast.makeText(view.getContext(), R.string.refreshing, Toast.LENGTH_LONG).show(); //Shows toast


    }
}
