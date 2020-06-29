package marko.mitrovic.singidroid4.repo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NewsModel{
    @SerializedName("post_id")
    int post_id;
    @SerializedName("post_title")
    String post_title;
    @SerializedName("post_permalink")
    String post_permalink;
    @SerializedName("post_description")
    String post_description;
    @SerializedName("post_date")
    String post_date;
    @SerializedName("cover_image_path")
    String cover_image_path;
    @SerializedName("post_images")
    List<String> post_images;
    @SerializedName("plain_text")
    String plain_text;

    public NewsModel() {
    }

    public NewsModel(int post_id, String post_title, String post_permalink, String post_description, String post_date, String cover_image_path, List<String> post_images, String plain_text) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_permalink = post_permalink;
        this.post_description = post_description;
        this.post_date = post_date;
        this.cover_image_path = cover_image_path;
        this.post_images = post_images;
        this.plain_text = plain_text;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_permalink() {
        return post_permalink;
    }

    public void setPost_permalink(String post_permalink) {
        this.post_permalink = post_permalink;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getCover_image_path() {
        return cover_image_path;
    }

    public void setCover_image_path(String cover_image_path) {
        this.cover_image_path = cover_image_path;
    }

    public List<String> getPost_images() {
        return post_images;
    }

    public void setPost_images(List<String> post_images) {
        this.post_images = post_images;
    }

    public String getPlain_text() {
        return plain_text;
    }

    public void setPlain_text(String plain_text) {
        this.plain_text = plain_text;
    }
}


