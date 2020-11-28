package my.online.grocery;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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


public class MyProfile extends Fragment {
    String url="http://hsoftech.in/Mcq/MobileApi/getprofile.php";

    View rootView;
    String uid="";
    String imgnm="";
    private Uri mHighQualityImageUri ;
    TextView txtnm,txtmno,txtadrs,txtemail,txtcity;

    public MyProfile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_my_profile, container, false);

       uid=SaveSharedPreference.getUserId(getActivity());
        txtnm=(TextView)rootView.findViewById(R.id.protnm);
        txtmno=(TextView)rootView.findViewById(R.id.protmno);
        txtadrs =(TextView)rootView.findViewById(R.id.protpoint);
        txtemail =(TextView)rootView.findViewById(R.id.protemail);
        txtcity =(TextView)rootView.findViewById(R.id.txtcitys);





       // requestStoragePermission();
        if(new ConnectionDetector(getActivity()).isConnectingToInternet()) {
           loaddataList();
        }
        else InternetError.showerro(getActivity());


        rootView.findViewById(R.id.txteditpro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(),EditProfile.class),2);
            }
        });

        rootView.findViewById(R.id.txtchangepass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getActivity(),ChangePassword.class));
            }
        });




        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 ) {

           loaddataList();




        }
    }


    public void loaddataList() {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(getActivity());
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

                                txtnm.setText(c.getString("nm"));
                                txtmno.setText(c.getString("mno"));
                                txtadrs.setText(c.getString("adrs"));
                                txtemail.setText(c.getString("email"));

                                txtcity.setText(c.getString("city"));





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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        // pDialog.show();
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }




}
