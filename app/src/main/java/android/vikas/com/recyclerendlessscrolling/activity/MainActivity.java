package android.vikas.com.recyclerendlessscrolling.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.vikas.com.recyclerendlessscrolling.model.MovieFeeder;
import android.vikas.com.recyclerendlessscrolling.listener.OnLoadMoreListener;
import android.vikas.com.recyclerendlessscrolling.R;
import android.vikas.com.recyclerendlessscrolling.adapter.AdapterEndlessScrolling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    AdapterEndlessScrolling adapterEndlessScrolling;
    List<MovieFeeder> movieFeederList = new ArrayList<>();
    RecyclerView recyclerView;
    int initialLoopValue=0,endloopValue=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRecyclerView();
        setScrollListener();
    }

    private void setScrollListener() {
        adapterEndlessScrolling.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                //this will make the list to be null valued. and notify will make adapter to check for getItemViewType which will ne null
                // since there is no position, which in turn will call progress bar layout to display untill items gets updated in adapter.

                movieFeederList.add(null);
                adapterEndlessScrolling.notifyItemInserted(movieFeederList.size() - 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Remove loading item
                        movieFeederList.remove(movieFeederList.size() - 1);
                        adapterEndlessScrolling.notifyItemRemoved(movieFeederList.size());

                        //Load data
                         initialLoopValue = movieFeederList.size();
                         endloopValue = movieFeederList.size() + 10;

                         movieDataLoad();
                         adapterEndlessScrolling.setLoaded();

                    }
                },5000);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        movieDataLoad();
    }

    private void movieDataLoad() {

        //here we are faking that we are loading data from server
        Random random = new Random();
        MovieFeeder movieFeeder;
        String movieTitle,movieRating;

        //on startup we are loading the first 1o  items
        for (int i = initialLoopValue; i< endloopValue; i++){

            //title
            movieTitle = String.valueOf("Movies Scrolling "+i);
            //color thumb
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            int movieThumb = Color.rgb(r,g,b);
            //rating
            movieRating = String.valueOf("Rating : "+r);
            movieFeeder = new MovieFeeder(movieTitle,movieRating,movieThumb);
            movieFeederList.add(movieFeeder);
        }

        //values are inserted into list now update the adapter
        adapterEndlessScrolling.notifyDataSetChanged();

    }

    private void setRecyclerView() {
        //set the recyclerview here
        recyclerView = (RecyclerView)findViewById(R.id.rvMovieFeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterEndlessScrolling = new AdapterEndlessScrolling(movieFeederList,recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterEndlessScrolling);
    }
}
