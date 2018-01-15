package mtrzepacz.androidgraphicfilters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class listOfFilters extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_filters);
        listView = (ListView) findViewById(R.id.ListOfFilters);

        String[] filters = {"BlackWhite","Negative","Gaussian Blur"};
        ArrayList filtersList = new ArrayList<String>();
        for( int i = 0 ; i < filters.length ; i++)
            filtersList.add(i,filters[i]);

        adapter = new ArrayAdapter<String>(this,R.layout.one_string_item,filtersList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String string = (String) o;
                Intent intent = new Intent();
                intent.putExtra("Filter Name",string);
                setResult(RESULT_OK,intent);
                finishActivity();
            }
        });
        listView.setAdapter(adapter);
    }

    public void finishActivity()
    {
        this.finish();
    }
}
