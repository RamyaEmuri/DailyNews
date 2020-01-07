package com.example.dailynews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PopularFragment extends Fragment {

    ArrayList<Data2> data2;
    RecyclerView recyclerView;

    JSONArray jsonArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        recyclerView = view.findViewById(R.id.recyclerView_2);

        data2 = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        AdapterTwo adapterTwo = new AdapterTwo(data2);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapterTwo)
//        ((MainActivity) getActivity()).callAsyncTask();
        new Tasker2().execute();
        return view;
    }


    public class Tasker2 extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;
        boolean apiLimitExceeded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
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

                url = new URL("https://api.nytimes.com/svc/mostpopular/v2/shared/1/facebook.json?api-key=F4bsn22LWqBDqKsJuBhwVuP44tsOiN0y");
                Log.e("url Values", url.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int httpStatus = httpURLConnection.getResponseCode();
                Log.e("httpstatus", "The response is: " + httpStatus);

                if (httpStatus != HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getErrorStream();
                    Map<String, List<String>> map = httpURLConnection.getHeaderFields();
                    System.out.println("Printing Response Header....\n");
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                } else {
                    inputStream = httpURLConnection.getInputStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }
                Log.e("webApi json object", response);

                if (response.contains("API Limit Rate Exceeded")) {
                    apiLimitExceeded = true;
                } else {
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    JSONArray jsonArray = object.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String headline = jsonObject.getString("title");

                        String date = jsonObject.getString("published_date");

                        JSONArray mediaArray=jsonObject.getJSONArray("media");
                        for ( int j=0; j<mediaArray.length();j++){
                            JSONObject mediaObject=mediaArray.getJSONObject(j);
                            JSONArray mediaDb=mediaObject.getJSONArray("media-metadata");
                            JSONObject jsonObject1 = mediaDb.getJSONObject(0);
                            String image = jsonObject1.getString("url");

                            Data2 data1 = new Data2(image, headline, date);
                            data2.add(data1);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            AdapterTwo adaptertwo = new AdapterTwo(data2, getActivity());
            recyclerView.setAdapter(adaptertwo);

        }
    }

}

