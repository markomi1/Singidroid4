package marko.mitrovic.singidroid4.api;

import com.google.gson.JsonArray;
import marko.mitrovic.singidroid4.repo.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface ApiCalls{


    @GET("appInit/getFaculties")
    Call<JsonArray> getFaculties();

    @GET("appInit/getYears")
    Call<JsonArray> getYears();

    @GET("appInit/getCourse")
    Call<JsonArray> getCourse(@Query("faculty") String faks,
                              @Query("year") String year);


    @GET("news/getSources")
    Call<JsonArray> getNewsSources();


    @GET("news/getNews")
        //TODO Change to POST
    Call<List<NewsModel>> getNews(@Query("newsSourceCategories") String newsSourceCategories,
                                  @Query("page") String page);

    @POST("api/getStudentBalance")
    Call<List<String>> getStudentBalance(@Query("index") String index, @Query("jmbg") String jmbg);
}
