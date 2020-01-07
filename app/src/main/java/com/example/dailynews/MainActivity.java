package com.example.dailynews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.net.wifi.WifiConfiguration.Status.strings;

public class MainActivity extends AppCompatActivity {

    ArrayList<Data> dataitems;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    JSONArray jsonArray;
    RecyclerView recyclerView;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.my_event);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        recyclerView  = findViewById(R.id.recyclerView_1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        dataitems = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        new Tasker().execute();

    }

    public class  Tasker extends AsyncTask<String, Void, Void>{

        private ProgressDialog progressDialog;
        boolean apiLimitExceeded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading......");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            HttpURLConnection httpURLConnection;
            URL url;
            InputStream inputStream;
            String response = "";
            try {

                url = new URL("https://api.nytimes.com/svc/archive/v1/2019/11.json?api-key=F4bsn22LWqBDqKsJuBhwVuP44tsOiN0y");
                Log.e("url Values", url.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int httpStatus = httpURLConnection.getResponseCode();
                Log.e("httpstatus", "The response is: " + httpStatus);

                if (httpStatus != HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getErrorStream();
                    Map<String, List<String>> map = httpURLConnection.getHeaderFields();
                    System.out.println("Printing Response Header....\n");
                    for (Map.Entry<String, List<String>> entry : map.entrySet()){
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                }else {
                    inputStream = httpURLConnection.getInputStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while((temp = bufferedReader.readLine()) != null ){
                    response += temp;
                }
                Log.e("webApi json object", response);

                if (response.contains("API Limit Rate Exceeded")){
                    apiLimitExceeded = true;
                }else {
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject responseJsonObject= object.getJSONObject("response");
                    jsonArray = responseJsonObject.getJSONArray("docs");

                    for (int i = 0; i< jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);

                         JSONObject headlineJsonObject= jsonObject.getJSONObject("headline");
                         String headline= headlineJsonObject.getString("main");

                         String date = jsonObject.getString("pub_date");

                         String description = jsonObject.getString("lead_paragraph");

                        JSONArray multimediaJsonArray = jsonObject.getJSONArray("multimedia");
                        JSONObject object1= multimediaJsonArray.getJSONObject(0);
                        String image="https://static01.nyt.com/"+object1.getString("url");

                        Data data1 = new Data(image, headline, date, description);
                        dataitems.add(data1);
                    }
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            AdapterOne adapterOne = new AdapterOne(dataitems, MainActivity.this);
            recyclerView.setAdapter(adapterOne);


//            ViewFragment.Tasker1 tasker1 = new ViewFragment.Tasker1();
//            tasker1.execute();

        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] childFragments;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            childFragments = new Fragment[]{
                    new ViewFragment(),
                    new PopularFragment(),
                    new ViewFragment(),
            };
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Newest";
                case 1:
                    return "Popular";
                case 2:
                    return "Featured";
            }
            return null;
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }

    }
}
//