package ir.mtajik.android.rxjavaexample;

public class User{

    String name;
    int id;
    boolean isFollowing;

    @Override
    public String toString() {
        return id+" | "+name+" | follow you:"+isFollowing;
    }
}
