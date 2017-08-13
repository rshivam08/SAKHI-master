package com.danielkim.soundrecorder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.danielkim.soundrecorder.activities.MainActivity;

public class Add extends ActionBarActivity {
    Button btn;
    SharedPreferences shared;
    private static final int REQUEST_READ_CONTACT=1;
    TextView mName,mPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btn=(Button)findViewById(R.id.btn);
        mName=(TextView)findViewById(R.id.name);
        mPhone=(TextView)findViewById(R.id.number);
        SharedPreferences share=getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String no=(share.getString("No",""));
        String name=(share.getString("Name",""));
        mName.setText(name);
        mPhone.setText(no);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 2005);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            String name=null;
            if (requestCode==2005){
                Uri uri=data.getData();
                //  Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                Cursor c=managedQuery(uri,null,null,null,null);
                if (c.moveToFirst()){
                    name=c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Toast.makeText(Add.this, name, Toast.LENGTH_SHORT).show();

                }
                Cursor contact=getContentResolver().query(uri,new String[]{ContactsContract.Contacts._ID},null,null,null);
                String id=null;
                if (contact.moveToFirst()){
                    id=contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
                }
                contact.close();
                String no=null;
                Cursor phoneCursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER},ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=? ",
                        new String[]{id},null);
                if (phoneCursor.moveToFirst()){
                    no=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    Toast.makeText(Add.this, no, Toast.LENGTH_SHORT).show();
                }
                shared=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                editor.putString("Name",name);
                editor.putString("No",no);
                editor.commit();
   mName.setText(name);
                mPhone.setText(no);
            }
        }
    }


}
