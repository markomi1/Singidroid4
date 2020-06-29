package marko.mitrovic.singidroid4.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import marko.mitrovic.singidroid4.R;

import java.util.LinkedList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private final Context context;
    private final ArticleImageViewClickListener mListener;
    public List<String> imageList;

    public ImageListAdapter(Context context, ArticleImageViewClickListener listener) {
        this.context = context;
        mListener = listener;
        imageList = new LinkedList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View viewItem = inflater.inflate(R.layout.article_image_holder, parent, false);
        viewHolder = new ImageListAdapter.ImageHolder(viewItem, mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageHolder imageHolder = (ImageHolder) holder;
        Glide.with(context).load(imageList.get(position)).apply(RequestOptions.centerCropTransform()).into(imageHolder.postImage);
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == imageList.size() - 1) ? LOADING : ITEM;
    }


    public void add(String movie) {
        imageList.add(movie);
        notifyItemInserted(imageList.size() - 1);
    }

    public void addAll(List<String> moveResults) {
        for (String result : moveResults) {
            add(result);
        }
    }


    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView postImage;


        public ImageHolder(View itemView, ArticleImageViewClickListener listener) {
            super(itemView);
            postImage = itemView.findViewById(R.id.article_images);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.imageClick(v, imageList, getAdapterPosition());
        }
    }

}