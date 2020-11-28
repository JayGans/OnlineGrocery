package my.online.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText ednm,edmno,edemail,edadrs,edcity;
    EditText edpass;

    Button btsave;
    CheckBox ck;
    String url="http://hsoftech.in/E-App/MobileApi/Registration.php";
   // String url_refisval="http://hsoftech.in/Mcq/MobileApi/isrefvalid.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Sign Up");
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backcolor)));
        } catch (Exception e)
        {

        }
        ck=(CheckBox)findViewById(R.id.isread);
        btsave=(Button)findViewById(R.id.btreg);
        ednm=(EditText)findViewById(R.id.edregnm) ;
        edmno=(EditText)findViewById(R.id.edregmno) ;
        edemail=(EditText)findViewById(R.id.edregemail) ;
        edadrs=(EditText)findViewById(R.id.edregadrs) ;

        edcity=(EditText)findViewById(R.id.edregcity) ;

        edpass=(EditText) findViewById(R.id.edregpass) ;
        findViewById(R.id.btclosereg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,MainActivity.class));
                finish();
            }
        });
        findViewById(R.id.txtcondi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,TermsConditions.class));

            }
        });
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          String nm,mno,email,pass,adrs,city;
                nm=ednm.getText().toString();
                mno=edmno.getText().toString();
                email=edemail.getText().toString();
                adrs=edadrs.getText().toString();
                city=edcity.getText().toString();
                pass=edpass.getText().toString();
                if(validate(nm,mno,pass,city,adrs))
                {

                        loaddataList(nm, mno, email, adrs,city, pass);

                }else {
                    Toast.makeText(Registration.this, "Fill all fields !", Toast.LENGTH_SHORT).show();
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

        startActivity(new Intent(Registration.this,Login.class));
        finish();



    }

    public boolean validate(String nm, String mno, String pass, String city, String adrs) {
        boolean valid = true;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (nm.isEmpty() || Character.isWhitespace(nm.charAt(0)))  {

            ednm.setError("Enter Full Name");
            ednm.setText("");
            valid = false;
        } else {
            ednm.setError(null);
        }


        if (mno.isEmpty() || mno.length()<10) {
            edmno.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            edmno.setError(null);
        }


        if(city.isEmpty()) {

            edcity.setError("Enter City Name");
            valid = false;
        } else {
            edcity.setError(null);
        }
        if(adrs.isEmpty()) {

            edadrs.setError("Enter Address");
            valid = false;
        } else {
            edcity.setError(null);
        }
        if (pass.isEmpty() || pass.length()<6) {
            edpass.setError("Enter Password(min 6 character)");
            valid = false;
        } else {
            edpass.setError(null);
        }



        return valid;
    }
    private void loaddataList(final String nm, final String mno, final String email, final String adrs, final String ref, final String pass) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(Registration.this);
        pDialog.setMessage("Wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print("data" + response);
                        //  Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("result");
                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject c = heroArray.getJSONObject(i);
                                String s=c.getString("res");
                                if (s.equals("yes")) {

                                   Toast.makeText(Registration.this, "Registration Completed Successfully ", Toast.LENGTH_SHORT).show();

finish();
                                } else if (s.equals("no")) {
                                    Toast.makeText(Registration.this, "Registration not completed try again !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Registration.this, "poor internet connection try again !", Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //  pDialog.dismiss();
                        pDialog.dismiss();
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nm", nm);
                params.put("mno", mno);
                params.put("email", email);
                params.put("adrs", adrs);
                params.put("city", ref);
                 params.put("pass", pass);
                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        // pDialog.show();
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }



}
