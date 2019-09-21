package com.codingwithmamun.contactsappdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithmamun.contactsappdemo.Utils.ChangePhotoDialog;
import com.codingwithmamun.contactsappdemo.Utils.DatabaseHelper;
import com.codingwithmamun.contactsappdemo.Utils.Init;
import com.codingwithmamun.contactsappdemo.Utils.UniversalImageLoader;
import com.codingwithmamun.contactsappdemo.models.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactFragment extends Fragment implements ChangePhotoDialog.onReceivedListener{
    private static final String TAG = "AddContactFragment";

//  private Contact mContact;
    private EditText mName,mEmail,mPhone;
    private Toolbar mToolbar;
    private CircleImageView mImageView;
    private Spinner mSelectDivice;
    private String mSelectedPath;
    private int mPreviousKeyStoke;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_addcontact,container,false);
        mName=view.findViewById(R.id.etContactPerson);
        mEmail=view.findViewById(R.id.etContactEmail);
        mPhone=view.findViewById(R.id.etContactPhone);
        mToolbar=view.findViewById(R.id.editcontactToolbar);
        mImageView=view.findViewById(R.id.contactImage);
        mSelectDivice=view.findViewById(R.id.select_divice);
        mSelectedPath=null;
        Log.d(TAG, "onCreateView: started.");

        //Load the default image by causing an error
        UniversalImageLoader.setImage(null,mImageView,null,"");

        //set the heading the for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.edit_contact));

        //required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        Log.d(TAG, "onCreateView: set toolbar to supportyActionbar.");
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreateView: setHasOptionsMenu.");


        //navigate to the back arrow
        ImageView ivBackArrow=view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow icon.");
                //remove previous fragment from the back stack(therefore navigating back.)
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        //save changes to the contact
        ImageView ivCheckMark=view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving the  contact ");
                //execute the save method of the database
                if (checkStringIfNull(mName.getText().toString())){
                    Log.d(TAG, "onClick: saving new contact.");

                    DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
                    Contact contact=new Contact(mName.getText().toString(),
                            mPhone.getText().toString(),
                            mSelectDivice.getSelectedItem().toString(),
                            mEmail.getText().toString(),
                            mSelectedPath);

                    if (databaseHelper.addContacts(contact)){
                        Toast.makeText(getActivity(),"contact saved",Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else {
                        Toast.makeText(getActivity(),"Error saving",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //Initialize the dialog box for  choose an photo
        ImageView ivCamera=view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening the 'image selection dialog box'.");
                /**
                 * Make sure all permission have been verified before opening the dialog.
                 */
                for (int i = 0; i< Init.PERMISSION.length; i++){
                    String []permission={Init.PERMISSION[i]};
                    if (((MainActivity)getActivity()).checkPermission(permission)){
                        ChangePhotoDialog dialog=new ChangePhotoDialog();
                        Log.d(TAG, "onClick: ChangePhotoDialoge Show.");
                        dialog.show(getFragmentManager(),getString(R.string.change_dialog_photo));
                        Log.d(TAG, "onClick: set TargetFragment EditContact Fragment.");
                        dialog.setTargetFragment(AddContactFragment.this,0);
                    }else {
                        Log.d(TAG, "onClick: called to verify Permission.");
                        ((MainActivity)getActivity()).verifyPermission(permission);
                    }
                }

            }
        });
        initOnTextChangeListener();

        return view;
    }

    public boolean checkStringIfNull(String string){
        if (string.equals("")){
            return false;
        }else {
            return true;
        }
    }

    /**
     * initialize the onTextChangedListener for formmiting the phone number
     */
    private void initOnTextChangeListener(){
        mPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                mPreviousKeyStoke=keyCode;
                return false;
            }
        });
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number=s.toString();
                if (number.length()==3 && mPreviousKeyStoke!=KeyEvent.KEYCODE_DEL
                        && !number.contains("(")){
                    number=String.format("(%s",s.toString().substring(0,3));
                    mPhone.setText(number);
                    mPhone.setSelection(number.length());
                }else if (number.length()==5 && mPreviousKeyStoke!=KeyEvent.KEYCODE_DEL
                                && !number.contains(")")){
                    number=String.format("(%s) %s",s.toString().substring(1,4),
                            s.toString().substring(4,5));
                    mPhone.setText(number);
                    mPhone.setSelection(number.length());
                }else if (number.length()==10 && mPreviousKeyStoke!=KeyEvent.KEYCODE_DEL
                        && !number.contains("-")){
                    number=String.format("(%s) %s-%s",s.toString().substring(1,4),
                                                s.toString().substring(6,9),
                                                s.toString().substring(9,10));

                    mPhone.setText(number);
                    mPhone.setSelection(number.length());
                }


            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: started.");
        inflater.inflate(R.menu.contact_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: started.");
        switch (item.getItemId()){
            case R.id.menu_delete:
                Log.d(TAG, "onOptionsItemSelected: contact deleting.");
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Retrieves the selected image from bundle(coming from ChangePhotoDialog)
     * @param bitmap
     */
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: get the bitmap: "+bitmap);
        //get the bitmap from 'ChangPhotoDialog'
        if (bitmap!=null){
            //compress the image(if you like)
            ((MainActivity)getActivity()).compressBitmap(bitmap,70);
            Log.d(TAG, "getBitmapImage: set bitmap mImageview.");
            mImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void getImagePath(String imagePath) {
        Log.d(TAG, "getImagePath: get the image path: "+imagePath);

        if (!imagePath.equals("")){
            mSelectedPath=imagePath;
            UniversalImageLoader.setImage(imagePath,mImageView,null,"");
        }
    }
}
