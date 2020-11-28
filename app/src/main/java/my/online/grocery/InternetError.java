package my.online.grocery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Sailyhiray on 12/06/2017.
 */
public class InternetError extends Activity {

    public static void showerro(Context ctx)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ctx,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle(" No Network Connection");
        builder.setMessage("Please check your internet connection or try again later !").setCancelable(false)
               .setIcon(R.drawable.ic_connection)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // error = "";
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
