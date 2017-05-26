package org.kozlowski.githubdemo.di;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.securepreferences.SecurePreferences;

import org.kozlowski.githubdemo.MyApplication;
import org.kozlowski.githubdemo.util.SecureManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.kozlowski.githubdemo.util.SecureManager.*;

/**
 * Created by and on 26.05.17.
 */
@Module
public class SecureModule {

    @Provides
    @Singleton
    SecureManager provideSecureManager(){
        return new SecureManager();
    }

    @Provides
    @Singleton
    SecurePreferences providesSecurePreferences(MyApplication application,SecureManager secureManager) {
        String devSerNr = secureManager.getDeviceSerialNumber(application);
        try {
            //This will only create a certificate once as it checks
            //internally whether a certificate with the given name
            //already exists.
            secureManager.createKeys(application, devSerNr);
        } catch (Exception e) {
            //Probably will never happen.
            throw new RuntimeException(e);
        }
        String pass = secureManager.getSigningKey(devSerNr);
        if (pass == null) {
            //This is a device less than JBMR2 or something went wrong.
            //I recommend eitehr not supporting it or fetching device hardware ID as shown below.
            //do note this is barely better than obfuscation.
            //Compromised security but may prove to be better than nothing
            pass = devSerNr;
            //bitshift everything by some pre-determined amount for added seurity
            pass = secureManager.bitshiftEntireString(pass);
        }
        return new SecurePreferences(application,pass, SECURE.SECURE_PREF_FILE.name());
    }
}
