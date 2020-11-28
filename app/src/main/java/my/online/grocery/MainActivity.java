package my.online.grocery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;



public class MainActivity extends Activity {

//DotLoader dotLoader;
    private static int TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uss = SaveSharedPreference.getUserId(MainActivity.this);
      //  dotLoader=(DotLoader)findViewById(R.id.text_dot_loader) ;
//        dotLoader.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dotLoader.setNumberOfDots(5);
//            }
//        }, 3000);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#FFFFFFFF"));
                // window.setNavigationBarColor(Color.parseColor("#c10702"));
            }
        } catch (Exception e) {
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);


        }



}

