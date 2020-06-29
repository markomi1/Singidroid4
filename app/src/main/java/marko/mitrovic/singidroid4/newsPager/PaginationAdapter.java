package marko.mitrovic.singidroid4.newsPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.repo.NewsModel;
import marko.mitrovic.singidroid4.util.RecyclerViewClickListener;

import java.util.LinkedList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    public List<NewsModel> newsList;
    private final Context context;
    private boolean isLoadingAdded = false;

    private RecyclerViewClickListener mListener;


    public PaginationAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        mListener = listener;
        newsList = new LinkedList<>();
    }

    public void setnewsList(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.news_list, parent, false);
                viewHolder = new NewsViewHolder(viewItem, mListener);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NewsModel post = newsList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                NewsViewHolder NewsViewHolder = (NewsViewHolder) holder;
                NewsViewHolder.postTitle.setText(post.getPost_title());
                NewsViewHolder.postDescription.setText(post.getPost_description());


                Glide.with(context).load(post.getCover_image_path()).apply(RequestOptions.centerCropTransform()).into(NewsViewHolder.postImage);
                break;
            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == newsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NewsModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = newsList.size() - 1;
        NewsModel result = getItem(position);
        if (result != null) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(NewsModel movie) {
        newsList.add(movie);
        notifyItemInserted(newsList.size() - 1);
    }

    public void addAll(List<NewsModel> moveResults) {
        for (NewsModel result : moveResults) {
            add(result);
        }
    }



    public NewsModel getItem(int position) {
        return newsList.get(position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView postTitle;
        private final TextView postDescription;
        private final ImageView postImage;


        public NewsViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.post_title);
            postDescription = itemView.findViewById(R.id.post_description);
            postImage = itemView.findViewById(R.id.post_image);
            mListener = listener;
            itemView.setOnClickListener(this);

            //itemView.setOnClickListener(new );
        }

        @Override
        public void onClick(View v) {
            NewsModel newsModel = newsList.get(getAdapterPosition());
            mListener.onClick(v, newsModel.getPost_title(), newsModel.getPost_date(), newsModel.getCover_image_path(), newsModel.getPlain_text(), newsModel.getPost_images());
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        private final ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore_progress);
        }
    }
}