package com.program.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button kaydet, getir, sil, guncelle;
    private TextView cikti;
    private EditText id, ad, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kaydet = findViewById(R.id.kaydetButon);
        getir = findViewById(R.id.getirButon);
        sil = findViewById(R.id.silButon);
        guncelle = findViewById(R.id.guncelleButon);
        cikti = findViewById(R.id.cikti);
        id = findViewById(R.id.idText);
        ad = findViewById(R.id.nameText);
        tel = findViewById(R.id.telText);

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                values.put("ad",ad.getText().toString());
                values.put("tel",tel.getText().toString());
                Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI,values);
                Toast.makeText(getApplicationContext(),"Rehber: " + uri.toString()+ " kayıt edildi.",Toast.LENGTH_SHORT).show();
            }
        });

        getir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor c = getContentResolver().query(MyProvider.CONTENT_URI,null,null,null,null);

                String result = "Rehber Sonuç: ";

                while (c.moveToNext()){
                    result = result + "\n" + c.getInt(c.getColumnIndex("id"))+"--"+c.getString(c.getColumnIndex("ad"))+"--"+c.getString(c.getColumnIndex("tel"));
                }

                cikti.setText(result);

            }
        });

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}