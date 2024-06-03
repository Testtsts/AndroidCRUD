package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NrpSearchDialog.SearchDialogListener {




    private EditText nrp,nama,nrpDialog;
    private Button simpan,ambildata, hapus, ubah;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper Opendb;


    @Override
    public void onClickAction(String nrp){
        delete(nrp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = (EditText)findViewById(R.id.nrp);
        nama= (EditText) findViewById(R.id.nama);
        simpan = (Button) findViewById(R.id.Simpan);
        ambildata = (Button) findViewById(R.id.ambildata);
        ubah = (Button) findViewById(R.id.update);
        hapus=(Button) findViewById(R.id.delete);
        simpan.setOnClickListener(operasi);
        ambildata.setOnClickListener(operasi);
        ubah.setOnClickListener(operasi);
        hapus.setOnClickListener(operasi);



        Opendb = new SQLiteOpenHelper(this,"db.sql",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}
            @Override
            public void onUpgrade
                    (SQLiteDatabase db, int oldVersion, int newVersion) {}
        };
        dbku = Opendb.getWritableDatabase();
        dbku.execSQL("create table if not exists mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop(){
        dbku.close();
        Opendb.close();
        super.onStop();

    }
    NrpSearchDialog nrpSearchDialog = new NrpSearchDialog();

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.Simpan){
                simpan();
            } else if (viewId == R.id.delete) {
                nrpSearchDialog.show(getSupportFragmentManager(),"NRP_DIALOG");
            }else if (viewId == R.id.ambildata) {
                ambildata();
            }  else if (viewId == R.id.update) {
                update();
            }
        }
    };



    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //positif -1 negatif -2
            switch (which) {
                case -1:
                    Log.d("test","test1");
                    break;
                case -2:
                    Log.d("test","test2");
                    break;
            }
        }
    };


    private void simpan()
    {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp",nrp.getText().toString());
        dataku.put("nama",nama.getText().toString());
        dbku.insert("mhs",null,dataku);
        Toast.makeText(this,"Data Tersimpan",Toast.LENGTH_LONG).show();
    }

    private void ambildata(){
        Cursor cur = dbku.rawQuery("select * from mhs where nrp='" +
                nrp.getText().toString()+ "'",null);

        if(cur.getCount() >0)
        {
            Toast.makeText(this,"Data Ditemukan Sejumlah " +
                    cur.getCount(),Toast.LENGTH_LONG).show();
            cur.moveToFirst();
            nama.setText(cur.getString(cur.getColumnIndex("nama")));
        }
        else
            Toast.makeText(this,"Data Tidak Ditemukan",Toast.LENGTH_LONG).show();
    }

    private void update()
    {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp",nrp.getText().toString());
        dataku.put("nama",nama.getText().toString());

        dbku.update("mhs",dataku,"nrp='"+nrp.getText().toString()+"'",null);
        Toast.makeText(this,"Data Terupdate",Toast.LENGTH_LONG).show();
    }

    private void delete(String nrp)
    {

        dbku.delete("mhs","nrp='"+nrp+"'",null);
        Toast.makeText(this,"Data Terhapus",Toast.LENGTH_LONG).show();
    }




}



