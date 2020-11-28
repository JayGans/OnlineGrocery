package my.online.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    String url="http://hsoftech.in/E-App/MobileApi/Login.php";
    EditText ednm;
    EditText edpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign In");
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backcolor)));
        } catch (Exception e)
        {

        }


        String uid=SaveSharedPreference.getUserId(Login.this);
        if(!uid.isEmpty())
        {
            startActivity(new Intent(Login.this,Home.class));
            finish();
        }

        findViewById(R.id.goreg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registration.class));

            }
        });


        ednm=(EditText)findViewById(R.id.edusnm);
        edpass=(EditText) findViewById(R.id.edpass);

        findViewById(R.id.btlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mno=ednm.getText().toString();
                String pass=edpass.getText().toString();
                if(!mno.isEmpty() && !pass.isEmpty())
                {
                    if(new ConnectionDetector(Login.this).isConnectingToInternet()) {
                        loaddataList(mno,pass);
                    }else InternetError.showerro(Login.this);
                }else
                {
                    // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(view, "Enter User Name And  Password .", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {

       // startActivity(new Intent(Registration.this,MainActivity.class));
        finish();

    }


    private void loaddataList(final String mno, final String pass) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(Login.this);
        // pDialog.setMessage("loading data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(""+response);
                        //  Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("login");
                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject c = heroArray.getJSONObject(i);
                                String str=c.getString("result");

                                 if(str.equals("yes"))
                                {
                                    SaveSharedPreference.setUserId(Login.this,c.getString("id"));
                                    SaveSharedPreference.setUserName(Login.this,c.getString("name"));

                                    Intent intent=new Intent(Login.this,Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(Login.this, "User Not Exit !", Toast.LENGTH_SHORT).show();
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        //displaying the error in toast if occurrs
                        //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mno);
                params.put("pass", pass);

                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        pDialog.show();
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }


}
