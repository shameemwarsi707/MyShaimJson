package com.shaim.myshaimjson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<DataModel> dataModels;

    private String TAG = MainActivity.class.getSimpleName();
   // private ListView mylisyview;

    //ArrayList<DataModel> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // mylisyview=findViewById(R.id.mylistview);
        recyclerView=findViewById(R.id.myRv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataModels=new ArrayList<>();
        new GetContacts().execute();
    }
    private class GetContacts extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Toast.makeText(MainActivity.this,
                    "Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
           // String url = "http://jsonplaceholder.typicode.com/users";
            String url = "https://jsonplaceholder.typicode.com/users";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonObj = new JSONArray(jsonStr);
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                   // Toast.makeText(MainActivity.this, "Error"+jsonObj, Toast.LENGTH_SHORT).show();
                   // JSONObject jsonObject=new JSONObject((Map) jsonObj);

                    // Getting JSON Array node
                  // JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    dataModels=new ArrayList<>();
                    for (int i = 0; i < jsonObj.length(); i++) {
                        DataModel mydata=new DataModel();
                        JSONObject c = jsonObj.getJSONObject(i);
                       mydata.setId(c.getInt("id"));
                        //Log.d("MainError",""+id);
                        mydata.setName(c.getString("name"));
                        mydata.setEmail(c.getString("email"));
                       // String address = c.getString("address");
                        //String gender = c.getString("gender");

                        // Phone node is JSON Object
                       /* JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");*/

                        // tmp hash map for single contact
                       // HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                       // contact.put("id", id);
                       // contact.put("name", name);
                      //  contact.put("email", email);
                        //contact.put("mobile", mobile);

                        // adding contact to contact list
                        dataModels.add(mydata);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            recyclerAdapter=new RecyclerAdapter(getApplicationContext(),dataModels);
            LinearLayoutManager manager=new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerAdapter);
            /*ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    contactList,
                    R.layout.my_list_item, new String[]{ "email","name"},
                    new int[]{R.id.email, R.id.name});
            mylisyview.setAdapter(adapter);*/
        }
    }
}