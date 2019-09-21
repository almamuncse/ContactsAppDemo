package com.codingwithmamun.contactsappdemo;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codingwithmamun.contactsappdemo.Utils.UniversalImageLoader;
import com.codingwithmamun.contactsappdemo.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements
        ViewContactsFragment.onContactSelectedListener,
        ContactFragment.onEditContactListener,
        ViewContactsFragment.onAddContactListener{

    private static final String TAG = "MainActivity";
    private static final int REQUES_CODE=1;


    @Override
    public void onEditContactSelecd(Contact contact) {
        Log.d(TAG, "onEditContactSelecd: contact selected from"+" "+
                getString(R.string.edit_contact_fragment)+" "+contact.getName());
        EditContactFragment fragment=new EditContactFragment();
        Bundle arg=new Bundle();
        Log.d(TAG, "onContactSelectedListener: contact for: "+contact);
        arg.putParcelable(getString(R.string.contact),contact);
        fragment.setArguments(arg);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString(R.string.view_contact_fragment));
        transaction.commit();
    }

    @Override
    public void onContactSelectedListener(Contact contact) {
        Log.d(TAG, "onContactSelectedListener: contact selected from"+" "+
                getString(R.string.view_contact_fragment)+" "+contact.getName());
        ContactFragment fragment=new ContactFragment();
        Bundle arg=new Bundle();
        Log.d(TAG, "onContactSelectedListener: contact for: "+contact);
        arg.putParcelable(getString(R.string.contact),contact);
        fragment.setArguments(arg);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString(R.string.view_contact_fragment));
        transaction.commit();

    }


    @Override
    public void onAddContact() {
        Log.d(TAG, "onAddContact: navigating to : "+getString(R.string.add_contact_fragment));
        AddContactFragment fragment=new AddContactFragment();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString(R.string.edit_contact_fragment));
        transaction.commit();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");


    init();
    initImageLoader();

}

    /**
     * initialize the first fragment (ViewContactFragment)
     */
    private void init(){
        ViewContactsFragment fragment=new ViewContactsFragment();
        Log.d(TAG, "init:crate to viewContactFragment object"+fragment);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        Log.d(TAG, "init: getSupportFragmentManager to trasaction");
        //replace whatever is in the fragment container view with the fragment
        //and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container,fragment);
        Log.d(TAG, "init: transaction to replace .");
        transaction.addToBackStack(null);
        Log.d(TAG, "init: called to add to backStack.");
        transaction.commit();
        Log.d(TAG, "init: viewContactFragment transaction commit.");
    }

    private void initImageLoader(){
        Log.d(TAG, "initImageLoader: universalImageLoader configration");
        UniversalImageLoader universalImageLoader=new UniversalImageLoader(MainActivity.this);
        Log.d(TAG, "initImageLoader: called to universal image loader");
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
        Log.d(TAG, "initImageLoader: "+universalImageLoader.getConfig());

    }

    /**
     * bitmap the compress by the @param 'quality'
     * Quality can be anywhere from: 1-100, 100 being the highest quality
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap,int quality){
        Log.d(TAG, "compressBitmap: started.");
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return bitmap;
    }

    /**
     * Generalize method for asking user.Can pass any array of permission
     * @param permission
     */
    public void verifyPermission(String[]permission){
        Log.d(TAG, "verifyPermission: asking user");
        ActivityCompat.requestPermissions(MainActivity.this,
                permission,REQUES_CODE);
    }

    /**
     * Checks to see if permissions was granted for the pass parameters
     * ONLY ONE PERMISSION MEY BE CHECKED AT A TIME
     * @param permission
     * @return
     */
    public boolean checkPermission(String[]permission){
        Log.d(TAG, "checkPermission: checking permission for: "+permission[0]);
        int requestPermission=ActivityCompat.checkSelfPermission(MainActivity.this,
                permission[0]);
        if (requestPermission!= PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: /nPermission was not granted for: "+permission[0]);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: "+requestCode);
        
        switch (requestCode){
            case REQUES_CODE:
                for (int i=0;i<permissions.length;i++){
                    if (grantResults[i]==PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: user has allowed permission for access."+permissions[i]);
                    }else {
                        break;
                    }
                }
                break;
        }
    }


}
