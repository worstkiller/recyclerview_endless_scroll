package android.vikas.com.recyclerendlessscrolling.adapter;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.vikas.com.recyclerendlessscrolling.model.MovieFeeder;
import android.vikas.com.recyclerendlessscrolling.listener.OnLoadMoreListener;
import android.vikas.com.recyclerendlessscrolling.R;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OFFICE on 12/5/2016.
 */

public class AdapterEndlessScrolling extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private boolean isLoading;
   private int visibleThreshold = 5;
   private int lastVisibleItem, totalItemCount;

   private final int VIEW_TYPE_ITEM = 0;
   private final int VIEW_TYPE_LOADING = 1;

   private OnLoadMoreListener mOnLoadMoreListener;
   List<MovieFeeder> movieFeederList = new ArrayList<>();

   RecyclerView recyclerView;

    public AdapterEndlessScrolling(List<MovieFeeder> movieFeederList,RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.movieFeederList = movieFeederList;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    //since we are loading, make it true
                    isLoading = true;
                }
            }
        });

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //here we are initialising the view for holder

        if (viewType == VIEW_TYPE_ITEM) {

            //for items if position is not null
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies,parent,false);
            return new AdapterEndlessScrolling.ViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADING) {
            //for items if position is null
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_progress_layout,parent,false);
            return new  AdapterEndlessScrolling.ViewHolderProgress(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //here we are setting the values into our views per row items

        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder)holder;
            MovieFeeder movieFeeder = movieFeederList.get(position);
            viewHolder.ivThumb.setBackgroundColor(Color.argb(256,movieFeeder.movieThumb,movieFeeder.movieThumb,movieFeeder.movieThumb));
            viewHolder.tvTitle.setText(movieFeeder.movieTitle);
            viewHolder.tvRating.setText(movieFeeder.movieRating);

        }else{
            ViewHolderProgress viewHolderProgress = (ViewHolderProgress)holder;
            viewHolderProgress.progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        //define here the size
        return mOnLoadMoreListener == null ? 0 : movieFeederList.size();
    }

    @Override
    public int getItemViewType(int position) {
       //decide if the list is empty if so return the respective view type
        return movieFeederList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //define here the widgets in view
        TextView tvTitle,tvRating;
        ImageView ivThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRating  = (TextView)itemView.findViewById(R.id.tvRecyclerRating);
            tvTitle  = (TextView)itemView.findViewById(R.id.tvRecyclerTitle);
            ivThumb = (ImageView)itemView.findViewById(R.id.ivRecyclerThumb);
        }
    }

    public class ViewHolderProgress extends RecyclerView.ViewHolder{

        //here define the progress bar
        ProgressBar progressBar;
        public ViewHolderProgress(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.pbRecyclerLoader);
        }
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        //set loading to false after data loaded
        isLoading = false;
    }
}
