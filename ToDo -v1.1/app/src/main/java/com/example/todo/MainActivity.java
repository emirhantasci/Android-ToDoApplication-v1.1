package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> nameArray;
    ArrayList<String> dateArray;
    ArrayList<String> newArray;
    ArrayList<String> newArray2;
    ArrayList<String> alertArray;
    ArrayList<Integer> idArray;
    ArrayAdapter arrayAdapter;
    ArrayAdapter yeniadapter;

    int i;
    int im;

    String ekranayazilacak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);


        nameArray=new ArrayList<String>();
        dateArray=new ArrayList<String>();
        newArray=new ArrayList<String>();
        newArray2=new ArrayList<String>();
        alertArray=new ArrayList<String>();
        idArray=new ArrayList<Integer>();

        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, newArray);
        listView.setAdapter(arrayAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, yapilacakislem.class);
                intent.putExtra("todoid", idArray.get(position));
                intent.putExtra("info", "old");
                intent.putExtra("idgo", position);
                intent.putExtra("alert", alertArray.get(position));
                startActivity(intent);
            }
        });


        getData();

    }



    public void getData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("to-do", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM todo",null);
            System.out.println(cursor);
            int nameIx=cursor.getColumnIndex("todoname");
            int idIx=cursor.getColumnIndex("id");
            int tarIx=cursor.getColumnIndex("date");
            int alaIx=cursor.getColumnIndex("alarm");

            while (cursor.moveToNext()){
                nameArray.add(cursor.getString(nameIx));
                idArray.add(cursor.getInt(idIx));
                dateArray.add(cursor.getString(tarIx));
                alertArray.add(cursor.getString(alaIx));
            }

            while (i<10000) {
                newArray.add(nameArray.get(i) + "        |        " + dateArray.get(i));
                i++;
            }



            arrayAdapter.notifyDataSetChanged();
            cursor.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.todo_add,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_todo){
            Intent intent = new Intent(MainActivity.this, yapilacakislem.class);
            intent.putExtra("info", "new");
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.go_todo){
            Toast.makeText(this, "Zaten yapılacaklar sayfasındasınız.", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.go_hakkinda){
            Intent intent = new Intent(MainActivity.this, Hakkinda.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}