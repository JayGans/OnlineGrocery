package my.online.grocery;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class homepage extends Fragment
{
    String url_bal="http://hsoftech.in/E-App/MobileApi/getslider.php";
    View rootView;
    private RecyclerView recyclerView;


    private SliderAdapterExample adapter;

    String url_cat="http://hsoftech.in/E-App/MobileApi/getcat.php";
String uid="";

    public homepage()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_homepage, container, false);
uid=SaveSharedPreference.getUserId(getActivity());
        SliderView sliderView = rootView.findViewById(R.id.imageSlider);

         adapter = new SliderAdapterExample(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        renewItems();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.cat_list);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
getListt();

        return  rootView;
    }

    public void renewItems() {

      getslider();

    }

    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(sliderItem);
    }

    private void Reflink() {

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "GRAMVIKAS MULTISERVICES APP");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=gram.vikas.financeapp&hl=en \n\n";
            sAux = sAux + "use Refer code : " + SaveSharedPreference.getRefCode(getActivity()) + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }



    public void getslider()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_bal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print("data" + response);
                        //  Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("slider");
                            // ArrayList<SetGetMethode> result = new ArrayList<>();
                            List<SliderItem> sliderItemList = new ArrayList<>();
                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject c = heroArray.getJSONObject(i);
                                SliderItem sliderItem = new SliderItem();
                                sliderItem.setDescription(c.getString("nm"));

                                    sliderItem.setImageUrl(c.getString("img"));
                                //Toast.makeText(getActivity(), ""+c.getString("img"), Toast.LENGTH_SHORT).show();

                                sliderItemList.add(sliderItem);
                            }
                            adapter.renewItems(sliderItemList);
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

    private void getListt() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_cat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        //  progressBar.setVisibility(View.INVISIBLE);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            double ertt=0;
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("cat");
                            //  Toast.makeText(Withdrawal.this, ""+heroArray, Toast.LENGTH_SHORT).show();
                            List<SetGetMethode> list1 = new ArrayList<>();
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject c = heroArray.getJSONObject(i);


                                SetGetMethode s= new SetGetMethode();
                                s.setId(c.getString("id"));
                                s.setName(c.getString("nm"));
                                s.setImgUrl(c.getString("img"));


                                list1.add(s);



                            }
                            recyclerView.setAdapter(new ItemAdapter(list1));




                        } catch (JSONException e) {
                            e.printStackTrace();
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //swipeRefreshLayout.setRefreshing(false);
                        //displaying the error in toast if occurrs
                        //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("uid",SaveSharedPreference.getUserId(SelectCourse.this));


                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

        private List<SetGetMethode> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtnm;
            ImageView img;


            public MyViewHolder(View view) {
                super(view);
                txtnm = (TextView) itemView.findViewById(R.id.txtcatnm);
                img=(ImageView) itemView.findViewById(R.id.catimg);

            }
        }


        public ItemAdapter(List<SetGetMethode> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cat_adapter, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final SetGetMethode movie = moviesList.get(position);

            holder.txtnm.setText(movie.getName());
            Picasso.with(getActivity()).load(movie.getImgUrl()).into(holder.img);





        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}
