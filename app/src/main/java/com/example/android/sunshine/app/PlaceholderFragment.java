package com.example.android.sunshine.app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public  class PlaceholderFragment extends Fragment {
    final String API_WEATHER = "http://api.openweathermap.org/data/2.5/forecast/daily";
    ArrayList<String> list = new ArrayList<String>();
    View root ;
    String zipcode = "";
    String unittype = "";
    String durtation = "";

    public PlaceholderFragment() {
        for(int i = 0; i < 10; i++)
            list.add(" hallo");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        root = rootView;
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.textviewlistitem,R.id.list_textView,list);
//            ListView listview = (ListView)rootView.findViewById(R.id.list_item);
//           listview.setAdapter(adapter);

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getActivity().getMenuInflater().inflate(R.menu.forcastfragment,menu);

    }

    @Override
    public void onStart() {
        super.onStart();
        getweather();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.refresh)
        {
            getweather();
            //updateui(list);
        }

        return true;
    }
    private void getweather()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        zipcode = pref.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_defult));
        unittype = "metric";
        durtation= ""+7;
        String Areacode = ",EG";
        Uri base = Uri.parse(API_WEATHER);
        Uri.Builder call = base.buildUpon();

        call.appendQueryParameter("q",zipcode);
        call.appendQueryParameter("units",unittype);
        call.appendQueryParameter("cnt",durtation);
        call.appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY);
        Log.e("Fing...me",call.toString());
        String u = call.toString();
        URL url = null;
        try {
            url = new URL(u);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("wer", "URRRRRRRRRL"+e);

        }
        even e = new even();
        e.execute(url);
    }

    public void updateui(final ArrayList<String> ss)
    {
        final ListView listview = (ListView) root.findViewById(R.id.list_item);
        if (ss != null)
        { ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.textviewlistitem,R.id.list_textView,ss);
            if(listview != null)
            {listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Context context = root.getContext();
                    String text = ss.get(position);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra("keyName",ss.get(position));
                    startActivity(intent);
                }
            });}
        }
    }
    public String getPreferences(int id, int deflut)
    {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return sh.getString(getString(id),getString(deflut));
    }

    public class even extends AsyncTask<URL,Void,String[]> {

        String[] earthquake = new String[7];

        @Override
        protected String[] doInBackground(URL...pra) {


           String s = getPreferences(R.string.Temture_Unit_key,R.string.Temture_Unit_default);
//String s = "Metric";
            Connecting connect = new Connecting(pra[0],s);

            try {
                if (pra[0] != null)
                earthquake =  connect.start();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }


            return earthquake;
        }

        @Override
        protected void onPostExecute(String[] event) {


           if(event != null)
           { ArrayList<String> ss = new ArrayList<String>( Arrays.asList( event ) );


            updateui(ss);}
        }
    }
}