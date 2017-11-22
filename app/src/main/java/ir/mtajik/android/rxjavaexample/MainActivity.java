package ir.mtajik.android.rxjavaexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static ir.mtajik.android.rxjavaexample.Utils.convert2UpperCase;
import static ir.mtajik.android.rxjavaexample.Utils.filterUserWhoLovesBoth;
import static ir.mtajik.android.rxjavaexample.Utils.getDetailOfUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mahdi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                rxTest();
//                zipOperationTest();
//                flatMapWithFilterTest();
//                flatMapWithZipTest(10);

                Intent i = new Intent(MainActivity.this,DissposableActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void rxTest() {

//        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6);
//        Observable<Integer> observable = Observable.range(100, 10);
//        Observable<Integer> observable = Observable.just(234);

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
                                                               @Override

                                                               public void subscribe

                                                                       (ObservableEmitter<Integer> e) throws Exception {
                                                                   //Use onNext to emit each item
                                                                   // in the stream//
                                                                   e.onNext(1);
                                                                   e.onNext(2);
                                                                   e.onNext(3);
                                                                   e.onNext(4);

                                                                   //Once the Observable has
                                                                   // emitted all items in the
                                                                   // sequence, call onComplete//

                                                                   e.onError(new Exception("io"));
                                                                   e.onComplete();
                                                               }
                                                           }
        );


        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: "+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };

        observable.subscribe(observer);
    }

    //todo fail
    private void rxTest2() {

        Observable<String> myStringObservable = Observable.create(new ObservableOnSubscribe<String>() {


            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                //Our Observable emits "Hello, world!" then completes. Now let's create a
                // Subscriber to consume the data:
                e.onNext("hello world!");
                e.onComplete();

            }
        });

        //All this does is print each String emitted by the Observable.
        Subscriber<String> myStringSubscriber = new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        myStringObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<? super String>) myStringSubscriber);

    }

    //todo success
    private void rxTest3() {

        Observable<String> stringObservable = Observable.just("mahdi", "tajik");

        Observer<String> stringObserver = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };


        stringObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver);
    }

    //Map: Map transforms the items emitted by an Observable by applying a function to each item.
    private void mapOperationTest() {

        Observable<List<String>> stringObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return new ArrayList<>(Arrays.asList("Ali", "john", "adam"));
            }
        });

        MyClass myClass = new MyClass();
        int v = myClass.counter;

        Observer<List<String>> myObserver = new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> strings) {
                for (String user :
                        strings) {
                    System.out.println(user);
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        stringObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //it can also convert object to another object
                .map(new Function<List<String>, List<String>>() {
                    @Override
                    public List<String> apply(List<String> strings) throws Exception {
                        return convert2UpperCase(strings);
                    }
                })
                .subscribe(myObserver);
    }

    //Zip combines the emissions of multiple Observables together via a specified function, then
    // emits a single item for each combination based on the results of this function
    private void zipOperationTest() {

        Observable<List<String>> footBallFans = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return new ArrayList<>(Arrays.asList("Ali", "john", "adam"));
            }
        });
        Observable<List<String>> basketballFans = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return new ArrayList<>(Arrays.asList("mahdi", "Ali", "hooman", "kambiz"));
            }
        });

        Observer<List<String>> fanStringObserver = new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> strings) {
                for (String user :
                        strings) {
                    System.out.println(user);
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        Observable.zip(footBallFans, basketballFans, new BiFunction<List<String>, List<String>,
                List<String>>() {

            @Override
            public List<String> apply(List<String> footballfans, List<String> basketfans) throws
                    Exception {
                List<String> userLoveBoth = filterUserWhoLovesBoth(footballfans, basketfans);
                return userLoveBoth;
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanStringObserver);


    }


    //Let say our server return the list of my friends , but we need to filter out only who those
    // friends who is also following me. Here comes the filter operator to do so.
    private void flatMapWithFilterTest() {

        Observable<List<User>> userList = Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return Utils.generateRandomUsers(5);

            }
        });

        userList.flatMap(new Function<List<User>, ObservableSource<User>>() {
            // flatMap - to return users one by one
            @Override
            public ObservableSource<User> apply(List<User> users) throws Exception {
                return Observable.fromIterable(users);
            }
        }).filter(new Predicate<User>() {
            @Override
            public boolean test(User user) throws Exception {
                // filtering user who follows me.
                return user.isFollowing;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        // only the user who is following me comes here one by one
                        Log.i(TAG, "onNext: " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });

    }

    private void flatMapWithZipTest(final int sampleSize) {


        Observable<List<User>> userObservable = Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return Utils.generateRandomUsers(sampleSize);
            }
        });

        userObservable
                .flatMap(new Function<List<User>, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(List<User> users) throws Exception {
                        return Observable.fromIterable(users);
                    }
                })
                .flatMap(new Function<User, ObservableSource<Pair<UserDetail, User>>>() {

                    // here we get the user one by one and then we are zipping
                    // two observable - one getUserDetailObservable (network call to get userDetail)
                    // and another Observable.just(user) - just to emit user

                    @Override
                    public ObservableSource<Pair<UserDetail, User>> apply(User user) throws
                            Exception {
                        return Observable.zip(getDetailOfUser(user.id), Observable.just(user), new
                                BiFunction<UserDetail, User, Pair<UserDetail, User>>() {


                                    @Override
                                    public Pair<UserDetail, User> apply(UserDetail userDetail,
                                                                        User user)
                                            throws Exception {
                                        // runs when network call completes
                                        // we get here userDetail for the corresponding user
                                        return new Pair<>(userDetail, user);
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<UserDetail, User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Pair<UserDetail, User> pair) {
                        // here we are getting the userDetail for the
                        // corresponding user one by one
                        Log.i(TAG, "onNext: pair=" + pair.first + "|" + pair.second);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


}
