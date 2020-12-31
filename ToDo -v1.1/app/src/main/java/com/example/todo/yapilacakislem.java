package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class yapilacakislem extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView imageView;
    EditText editText;
    EditText islemText;
    Button button;
    Button alarm;
    TextView tarih;
    Button tarihAyarla;
    Editable tarihim;
    TextView alarmyazisi;
    Button sil;
    Button alarmikapat;
    int yilim;
    int ayim;
    int gunum;
    int saatim;
    int dakikam;
    int yilim2;
    int ayim2;
    int gunum2;
    int saatim2;
    int dakikam2;
    int i=0;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog2;
    private DatePickerDialog datePickerDialog2;
    final static int islem_kodu = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yapilacakislem);
        imageView=findViewById(R.id.imageView);
        editText=findViewById(R.id.editText);
        islemText=findViewById(R.id.islemText);
        button=findViewById(R.id.save);
        tarih = findViewById(R.id.tarihView);
        tarihAyarla=findViewById(R.id.tarihsec);
        sil = findViewById(R.id.sil);
        alarm = findViewById(R.id.alarm);
        //alarm.setOnClickListener((View.OnClickListener) this);

        alarmyazisi=findViewById(R.id.alarmyazisi);
        alarmikapat=findViewById(R.id.alarmikapat);

        Intent intent= getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")){
            editText.setText("");
            islemText.setText("");
            tarih.setText("Tarih: ");
            button.setEnabled(true);
            tarihAyarla.setVisibility(View.VISIBLE);
            sil.setEnabled(false);
            alarm.setEnabled(true);
            Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.selectfoto);

        }else{
            int todoid=intent.getIntExtra("todoid",1);
            button.setEnabled(false);
            sil.setEnabled(true);
            alarm.setEnabled(false);
            tarihAyarla.setVisibility(View.INVISIBLE);
            imageView.setEnabled(false);

            try {
                SQLiteDatabase database = openOrCreateDatabase("to-do", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("SELECT * FROM todo WHERE id=?", new String[] {String.valueOf(todoid)});

                int todonameIx = cursor.getColumnIndex("todoname");
                int aciklamalarIx= cursor.getColumnIndex("aciklamalar");
                int tarihIx= cursor.getColumnIndex("date");
                int imageIx = cursor.getColumnIndex("image");
                int alertIx= cursor.getColumnIndex("alarm");

                while (cursor.moveToNext()){
                    editText.setText(cursor.getString(todonameIx));
                    islemText.setText(cursor.getString(aciklamalarIx));
                    tarih.setText(cursor.getString(tarihIx));
                    alarmyazisi.setText(cursor.getString(alertIx));

                    byte[] bytes =cursor.getBlob(imageIx);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imageView.setImageBitmap(bitmap);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void alarm(View view) {
        openPickerDialog(false);
    }

    private void openPickerDialog(boolean tumgunsaat) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, ay, gun) -> {
            yilim = year;
            ayim = ay;
            gunum = gun;
        };
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, saat, dakika) -> {
            saatim = saat;
            dakikam = dakika;
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.YEAR, yilim);
            calSet.set(Calendar.MONTH, ayim);
            calSet.set(Calendar.DAY_OF_MONTH, gunum);
            calSet.set(Calendar.HOUR_OF_DAY, saatim);
            calSet.set(Calendar.MINUTE, dakikam);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        };
        timePickerDialog = new TimePickerDialog(yapilacakislem.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), tumgunsaat);
        timePickerDialog.setTitle("Alarm Ayarla");
        timePickerDialog.show();

        datePickerDialog = new DatePickerDialog(yapilacakislem.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Gun Ayarla");
        datePickerDialog.show();


    }




    private void setAlarm(Calendar alarmCalendar) {
        String alarmsaat = gunum + "." + ayim + "." + yilim + " | " + saatim + ":" + dakikam;
        System.out.println(alarmsaat);
        alarmyazisi.setText("Kurulan alarm: " + alarmsaat);
        System.out.println(gunum);
        Toast.makeText(this, "Alarm ayarlandı!", Toast.LENGTH_SHORT).show();
        Intent intenttt = new Intent(getBaseContext(), com.example.todo.AlarmReceiver2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intenttt, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        intentArray.add(pendingIntent);
        i++;
        System.out.println(i);
        System.out.println(intentArray);
    }

    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }

    public void kapatAlarm(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intenttt = new Intent(getBaseContext(), com.example.todo.AlarmReceiver2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intenttt, 0);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        alarmManager.cancel(intentArray.get(i));
    }

    public void delete(View view){
        System.out.println("Girildi");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Siliniyor");
        alert.setMessage("Oluşturulan zaman planı silinsin mi?");
        alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= getIntent();
                int id = intent.getIntExtra("todoid", 0);
                System.out.println(id);
                SQLiteDatabase database = openOrCreateDatabase("to-do", MODE_PRIVATE, null);
                database.execSQL("DELETE FROM todo WHERE id=" + id);
                Toast.makeText(yapilacakislem.this, "Zaman planı silindi.", Toast.LENGTH_SHORT).show();
                Intent intentt = new Intent(yapilacakislem.this, MainActivity.class);
                startActivity(intentt);
            }
        });
        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(yapilacakislem.this, "Zaman planı silinmedi", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
            else{
                Toast.makeText(this, "Galeri iznini vermediğiniz için galeri açılamıyor.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "İzinler sağlanamadığı için açılamıyor.", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2 && resultCode== RESULT_OK && data!=null){

            Uri imageData = data.getData();

            try {
                if (Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                }
                else{
                    selectedImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void save(View view){
        String todoName=editText.getText().toString();
        String aciklama=islemText.getText().toString();
        String tarihim = tarih.getText().toString();
        String alarmvakti = alarmyazisi.getText().toString();
        Bitmap smallImage=makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG, 50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        try {
            SQLiteDatabase database=this.openOrCreateDatabase("to-do",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS todo (id INTEGER PRIMARY KEY, todoname VARCHAR, aciklamalar VARCHAR, date VARCHAR, alarm, VARCHAR, image BLOB)");
            String sqlString ="INSERT INTO todo (todoname,aciklamalar,date,alarm,image) VALUES(?,?,?,?,?)";

            SQLiteStatement sqLiteStatement= database.compileStatement(sqlString); //Bir stringi SQL komutu olarak çalıştırıyor.
            //Aynı zamanda bindString ile direkt ulaşmamızı sağlayacak.
            sqLiteStatement.bindString(1, todoName);
            sqLiteStatement.bindString(2, aciklama);
            sqLiteStatement.bindString(3,tarihim);
            sqLiteStatement.bindString(4, alarmvakti);
            sqLiteStatement.bindBlob(5, byteArray);
            sqLiteStatement.execute();
            Toast.makeText(this, "Zaman planı oluşturuldu.", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intento = new Intent(yapilacakislem.this, MainActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Daha önceki bütün aktiviteleri kapatır.
        startActivity(intento);
        //finish();

    }


    public void tarihSec(View view2){
        boolean tumgunsaat = false;
        Calendar calendar2 = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener2 = (view, year, ay, gun) -> {
            yilim2 = year;
            ayim2 = ay;
            gunum2 = gun;
        };

        TimePickerDialog.OnTimeSetListener onTimeSetListener2 = (view, saat, dakika) -> {
            saatim2 = saat;
            dakikam2 = dakika;
            String tarihAyar= gunum2+"."+ayim2+"."+yilim2+"   -   "+saatim2+":"+dakikam2;
            tarih.setText(tarihAyar);
        };

        timePickerDialog2 = new TimePickerDialog(yapilacakislem.this, onTimeSetListener2, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), tumgunsaat);
        timePickerDialog2.setTitle("Alarm Ayarla");
        timePickerDialog2.show();

        datePickerDialog2 = new DatePickerDialog(yapilacakislem.this, onDateSetListener2, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.setTitle("Gun Ayarla");
        datePickerDialog2.show();
    }





    public Bitmap makeSmallerImage(Bitmap image, int maximumSize){
        int width=image.getWidth();
        int height=image.getHeight();

        float bitmapRatio=(float) width/ (float) height;

        if (bitmapRatio>1){
            //Resim yataysa
            width=maximumSize;
            height= (int) (width/ bitmapRatio);
        }else{
            //resim dikeyse
            height=maximumSize;
            width=(int) (height*bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}