package my.online.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText ednm,edmno,edusnm,edemail,edcity;
    String uid="";
    String url_up="http://hsoftech.in/Mcq/MobileApi/updateprofile.php";
    String url="http://hsoftech.in/Mcq/MobileApi/getprofile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e)
        {

        }
        uid= SaveSharedPreference.getUserId(EditProfile.this);
        setTitle("Update Profile");
         ednm=(EditText)findViewById(R.id.edprofnm);
        edmno=(EditText)findViewById(R.id.edpromno);
        edmno=(EditText)findViewById(R.id.edpromno);
        edusnm=(EditText)findViewById(R.id.edprousnm);
        edemail=(EditText)findViewById(R.id.edproemail);
        edcity=(EditText)findViewById(R.id.proedcity);






        if(new ConnectionDetector(EditProfile.this).isConnectingToInternet()) {
            loaddataList();
        }
        else InternetError.showerro(EditProfile.this);
        findViewById(R.id.edprosubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm,mno,email,usnm,city;
                nm=ednm.getText().toString();
                mno=edmno.getText().toString();

                city=edcity.getText().toString();
                usnm=edusnm.getText().toString();
                email=edemail.getText().toString();
                if(validate(nm,mno,email,city))
                {
                    if(new ConnectionDetector(EditProfile.this).isConnectingToInternet()) {
                        Update(nm,mno,usnm,email,city);
                    }
                    else InternetError.showerro(EditProfile.this);

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
        Intent intent=new Intent();
        intent.putExtra("MESSAGE","no");
        setResult(2,intent);
        finish();

    }

    public boolean validate(String nm, String mno, String email, String city) {
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

       if(!email.isEmpty()) {
            if (!email.matches(emailPattern)) {
                edemail.setError("Enter Valid Email Id");
                valid = false;
            } else {
                edemail.setError(null);
            }
        }
        if (city.isEmpty())  {

            edcity.setError("Enter City Name");

            valid = false;
        } else {
            edcity.setError(null);
        }


       /* if (pass1.isEmpty() ) {
            edpass1.setError("Enter Password");
            valid = false;
        } else {
            edpass1.setError(null);
        }

        if (!pass2.equals(pass1)) {
            edpass2.setError("Enter Password doesn't match !");
            valid = false;
        } else {
            edpass2.setError(null);
        }*/

        return valid;
    }

    private void Update(final String nm, final String mno, final String usnm, final String email, final String city) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(EditProfile.this);
        pDialog.setMessage("Wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_up,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print("data" + response);
                        //  Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();

                        try {

                                String s=response.trim();
                                if (s.equals("yes")) {
                                    Toast.makeText(EditProfile.this, "Profile Updated Successfully ", Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent();
                                    intent.putExtra("MESSAGE","load");
                                    setResult(2,intent);
                                    finish();

                                } else if (s.equals("no")) {
                                    Toast.makeText(EditProfile.this, "Profile  not Updated try again !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(EditProfile.this, "poor internet connection try again !", Toast.LENGTH_SHORT).show();
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

                        //displaying the error in toast if occurrs
                        //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nm", nm);
                params.put("mno", mno);
                params.put("email", email);
                params.put("adrs", usnm);
                params.put("uid", uid);

                params.put("city", city);
                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        // pDialog.show();
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public void loaddataList() {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(EditProfile.this);
        pDialog.setMessage("loading data...");
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
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("profile");
                            // ArrayList<SetGetMethode> result = new ArrayList<>();

                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject c = heroArray.getJSONObject(i);

                                ednm.setText(c.getString("nm"));
                                edmno.setText(c.getString("mno"));
                                edusnm.setText(c.getString("adrs"));
                                edemail.setText(c.getString("email"));


                                edcity.setText(c.getString("city"));



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  pDialog.dismiss();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        // pDialog.show();
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}
