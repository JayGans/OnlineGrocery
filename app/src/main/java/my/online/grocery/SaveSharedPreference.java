package my.online.grocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 1/27/2016.
 */
public class SaveSharedPreference
{
    static final String UserId="swiggyid";
    static final String UserName="mmusnm";
    static final String RefCode="refcpode";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserId(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserId, userName);
        editor.commit();
    }

    public static String getUserId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(UserId, "");
    }


    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserName, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(UserName, "");
    }

    public static void setRefCode(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RefCode, userName);
        editor.commit();
    }

    public static String getRefCode(Context ctx)
    {
        return getSharedPreferences(ctx).getString(RefCode, "");
    }



}
