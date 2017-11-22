package ir.mtajik.android.rxjavaexample.paging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import ir.mtajik.android.rxjavaexample.R;

public class PagingActivity extends AppCompatActivity {


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    RecyclerView recyclerView;
    PagingAdapter pagingAdapter;
    ProgressBar progressBar;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeUi();
    }

    private void initializeUi() {

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(layoutManager);
        pagingAdapter = new PagingAdapter();
        recyclerView.setAdapter(pagingAdapter);

        setUpLoadMoreListener();
        subscribeForData();
    }

    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    paginator.onNext(pageNumber);
                    loading = true;
                }
            }
        });
    }

    private void subscribeForData() {

        Disposable disposable = paginator
                .onBackpressureDrop()
                .concatMap(new Function<Integer, Publisher<List<String>>>() {
                    @Override
                    public Publisher<List<String>> apply(Integer page) throws Exception {
                        loading = true;
                        progressBar.setVisibility(View.VISIBLE);
                        return dataFromNetwork(page);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> items) throws Exception {
                        pagingAdapter.addItems(items);
                        pagingAdapter.notifyDataSetChanged();
                        loading = false;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        compositeDisposable.add(disposable);

        paginator.onNext(pageNumber);
    }


    /**
     * Simulation of network data
     */
    private Flowable<List<String>> dataFromNetwork(final int page) {
        return Flowable.just(true)
                .delay(2, TimeUnit.SECONDS)
                .map(new Function<Boolean, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull Boolean value) throws Exception {
                        List<String> items = new ArrayList<>();
                        for (int i = 1; i <= 10; i++) {
                            items.add("Item " + (page * 10 + i));
                        }
                        return items;
                    }
                });
    }

}
