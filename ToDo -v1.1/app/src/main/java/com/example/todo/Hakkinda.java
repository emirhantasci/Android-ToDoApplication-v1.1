package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Hakkinda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);
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
            Intent intent = new Intent(Hakkinda.this, yapilacakislem.class);
            intent.putExtra("info", "new");
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.go_todo){

            Intent intent = new Intent(Hakkinda.this, MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.go_hakkinda){
            Toast.makeText(this, "Zaten hakkında sayfasındasınız.", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}