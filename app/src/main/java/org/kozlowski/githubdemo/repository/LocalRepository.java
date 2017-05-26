package org.kozlowski.githubdemo.repository;

import android.content.SharedPreferences;
import android.text.BoringLayout;

import com.securepreferences.SecurePreferences;

import org.kozlowski.githubdemo.data.error.ReadUserDataException;
import org.kozlowski.githubdemo.data.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by and on 16.05.17.
 */
@Singleton
public class LocalRepository {
    private static final String USER_NAME_KEY = "user_name_key";
    private static final String USER_TOKEN = "user_token";
    private final SharedPreferences sharedPreferences;

    @Inject
    public LocalRepository(SecurePreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Observable<Boolean> writeUser(final User user){

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) {
                sharedPreferences.edit().putString(USER_NAME_KEY,user.getUserName()).commit();
                sharedPreferences.edit().putString(USER_TOKEN,user.getUserToken()).commit();
                e.onNext(true);
            }
        });
    }

    public Observable<User> readUser(){
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e)  {
                try {
                    final String user = sharedPreferences.getString(USER_NAME_KEY, null);
                    final String token = sharedPreferences.getString(USER_TOKEN, null);
                    if (user == null || token == null) {
                        e.onError(new ReadUserDataException());
                    } else {
                        e.onNext(new User(user, token));
                    }
                }catch (Exception ex){
                    e.onError(new ReadUserDataException());
                }
            }
        });
    }
}
