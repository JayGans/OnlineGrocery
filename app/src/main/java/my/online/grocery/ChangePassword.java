package my.online.grocery;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class ChangePassword extends AppCompatActivity {

    EditText edold,ednew,edrenew;
    Button btcancel,btsave;
   ConnectionDetector cd;
    String UID;
   // String usid;
   String url="http://hsoftech.in/Mcq/MobileApi/changepass.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
UID=SaveSharedPreference.getUserId(ChangePassword.this);
        setTitle("Change Password");
        cd= new ConnectionDetector(ChangePassword.this);
        ednew=(EditText)findViewById(R.id.edpassnew);
        edold=(EditText)findViewById(R.id.edpassold);
        edrenew=(EditText)findViewById(R.id.edpassrenew);

        btcancel=(Button)findViewById(R.id.btpasscancel);
        btsave=(Button)findViewById(R.id.btpasssave);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cd.isConnectingToInternet())
                {
                    String old,pnew,renew;
                    old=edold.getText().toString();
                    pnew=ednew.getText().toString();
                    renew=edrenew.getText().toString();
                if(validation(old,pnew,renew))
                {
               try
               {
                  updateprofile(old,renew);
               }catch (Exception e)
               {

               }
                }

                }else
                {
                    InternetError.showerro(ChangePassword.this);
                }
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){}

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();

    }

    public boolean validation(String nm, String pass1, String pass2)
    {
        boolean valid=true;

        if (nm.isEmpty() ) {

            edold.setError("Enter old password");
            valid = false;
        } else {
            edold.setError(null);
        }

        if (pass1.isEmpty()) {
            ednew.setError("Enter Password please");
            valid = false;
        } else {
            ednew.setError(null);
        }
        if (pass2.isEmpty() || !pass1.equals(pass2)) {
            edrenew.setError("Enter Password doesn't match");
            valid = false;
        } else {
            edrenew.setError(null);
        }

        return valid;
    }
    private void updateprofile(final String old, final String newpass) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(ChangePassword.this);
        // pDialog.setMessage("loading data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        //  progressBar.setVisibility(View.INVISIBLE);
                        // swipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();

                        if(response.trim().equals("yes"))
                        {
                            Toast.makeText(ChangePassword.this, "Password updated successfully..", Toast.LENGTH_SHORT).show();
                           finish();
                        }else  if(response.trim().equals("no"))
                        {
                            Toast.makeText(ChangePassword.this, "Enter correct information", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            Toast.makeText(ChangePassword.this, "some thing went wrong try again.", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        pDialog.dismiss();
                        Toast.makeText(ChangePassword.this, "Some thing went wrong try again ! Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UID);
                params.put("old", old);
                params.put("newps", newpass);


                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);


        pDialog.show();
        requestQueue.add(stringRequest);

    }
}

