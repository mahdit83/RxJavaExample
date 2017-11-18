package ir.mtajik.android.rxjavaexample;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;


public class Utils {


    private static ArrayList<UserDetail> userDetailList = new ArrayList<>();
    private static String TAG = "mahdi";
    private static int id = 1;

    public static List<String> convert2UpperCase(List<String> list) {

        List<String> result = new ArrayList<>();

        for (String user :
                list) {
            result.add(user.toUpperCase());
        }
        return result;

    }

    public static List<String> filterUserWhoLovesBoth(List<String> basketFans, List<String>
            footballFans) {
        List<String> userWhoLovesBoth = new ArrayList<>();
        for (String basketfan : basketFans) {
            for (String footballFan : footballFans) {
                if (basketfan == footballFan) {
                    userWhoLovesBoth.add(basketfan);
                }
            }
        }
        return userWhoLovesBoth;
    }

    public static ArrayList<User> generateRandomUsers(int count) {

        ArrayList<User> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            result.add(generateRandomUser());
            userDetailList.add(generateRandomUserDetail());
            id++;
        }

        return result;

    }

    private static User generateRandomUser() {

        ArrayList<String> nameList = new ArrayList<>(Arrays.asList("Mahdi", "Ali", "Shabnam",
                "Reza", "Zahra", "Zeynab", "Fatemeh", "Aliakbar"));
        ArrayList<String> lastNameList = new ArrayList<>(Arrays.asList("Tajik", "Mansouri",
                "Khosroshahi", "Hadidi", "Laizadeh"));
        Random r1 = new Random();
        Random r2 = new Random();
        int i1 = r1.nextInt(nameList.size() - 1);
        int i2 = r2.nextInt(lastNameList.size() - 1);
        boolean isFollowMe = r1.nextBoolean();

        User user = new User();

        user.id = id;
        user.name = nameList.get(i1) + " " + lastNameList.get(i2);
        user.isFollowing = isFollowMe;

        Log.i(TAG, "user: " + user.toString());

        return user;

    }

    private static UserDetail generateRandomUserDetail() {


        Random r1 = new Random();
        Random r2 = new Random();
        int age = r1.nextInt(85);
        int weight = r2.nextInt(130 - 65) + 65;
        int height = r2.nextInt(210 - 160) + 160;
        boolean sexBoolean = r1.nextBoolean();

        UserDetail userDetail = new UserDetail();

        userDetail.id = id;
        userDetail.height = height;
        userDetail.weight = weight;
        userDetail.age = age;
        if (sexBoolean) {
            userDetail.sex = "Male";

        } else {
            userDetail.sex = "Female";

        }

        Log.i(TAG, "userDetail: " + userDetail.toString());

        return userDetail;

    }

    static public Observable<UserDetail> getDetailOfUser(int userId){

        for (UserDetail detail :
                userDetailList) {
            if (detail.id == userId){
                return Observable.just(detail);
            }
        }
        return null;



    }
}
