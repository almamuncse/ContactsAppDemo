package com.codingwithmamun.contactsappdemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.codingwithmamun.contactsappdemo.Utils.ContactListAdapter;
import com.codingwithmamun.contactsappdemo.Utils.DatabaseHelper;
import com.codingwithmamun.contactsappdemo.models.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewContactsFragment extends Fragment {
    private static final String TAG = "ViewContactsFragment";
    private String testProfileURL="cdn.vox-cdn.com/thumbor/eVoUeqwkKQ7MFjDCgrPrqJP5ztc=/0x0:2040x1360/1200x800/filters:focal(860x1034:1186x1360)/cdn.vox-cdn.com/uploads/chorus_image/image/59377089/wjoel_180413_1777_android_001.1523625143.jpg";


    //interface
    public interface onContactSelectedListener{
        public void onContactSelectedListener(Contact con);
    }
    onContactSelectedListener mContactListener;

    public interface onAddContactListener{
        public void onAddContact();
    }
    onAddContactListener mOnAddContact;


    //Variable and widgets
    private static final int STANDARD_APPBAR=0;
    private static final int SEARCH_APPBAR=1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar,searchBar;
    private ContactListAdapter adapter;
    private ListView contactList;
    private EditText mSearchContact;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_viewcontacts,container,false);
        viewContactsBar=view.findViewById(R.id.viewContactsToolbar);
        searchBar=view.findViewById(R.id.searchContactsToolbar);
        contactList=view.findViewById(R.id.contactsList);
        mSearchContact=view.findViewById(R.id.etSearchContacts);
        Log.d(TAG, "onCreateView: started.");


        //Set up Contact List
        setUpContactList();
        Log.d(TAG, "onCreateView: called up to setupContactList.finish");

        FloatingActionButton fab=view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked fab.");
                mOnAddContact.onAddContact();

            }
        });

        ImageView ivSearchContact=view.findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked search icon.");
                toggleToolbarState();
            }
        });
        
        ImageView ivBackArrow=view.findViewById(R.id.backarrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow icon.");
                toggleToolbarState();
            }
        });
        
        
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContactListener= (onContactSelectedListener) getActivity();
            Log.d(TAG, "onAttach: called ."+getActivity());
            mOnAddContact= (onAddContactListener) getActivity();
            Log.d(TAG, "onAttach: finish Attach Method.");
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: classCastException "+e.getMessage());
        }
    }

    //https://
    private void setUpContactList(){
        final ArrayList<Contact>contacts=new ArrayList<>();
        Log.d(TAG, "setUpContactList: "+getActivity());
//        contacts.add(new Contact("Al Mamun","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        Log.d(TAG, "setUpContactList: what is your details.");
//        contacts.add(new Contact("Md Nawsher Ali","01737157517","Mobile","almamungmail.com",testProfileURL));
//        contacts.add(new Contact("Abu Bokkor Siddik","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        contacts.add(new Contact("Sobuj ","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        contacts.add(new Contact("Alamin","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        contacts.add(new Contact("Riad","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        contacts.add(new Contact("Akhi","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        contacts.add(new Contact("Al Mamun","01737157517","Mobile","almamun@gmail.com",testProfileURL));
//        Log.d(TAG, "setUpContactList: add the catacts array list of Catact."+getActivity());


        DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        Cursor cursor=databaseHelper.getAllContact();

        //iterate through all the row contained in the database
        if (!cursor.moveToNext()){
            Toast.makeText(getActivity(),"There are no contact show",Toast.LENGTH_SHORT).show();
        }

        while (cursor.moveToNext()){
            contacts.add(new Contact(
                    cursor.getString(1),//name
                    cursor.getString(2),//phone number
                    cursor.getString(3),//device
                    cursor.getString(4),//email
                    cursor.getString(5)//profile image uri
            ));
        }

        Log.d(TAG, "setUpContactList: "+contacts.get(0).getProfileImage());

        //sort the arrayList on the based on the contact name
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        adapter=new ContactListAdapter(getActivity(),R.layout.layout_contactlistitem,contacts,"");

        mSearchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text=mSearchContact.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Log.d(TAG, "setUpContactList: adapter define and set list view");

        contactList.setAdapter(adapter);

        Log.d(TAG, "setUpContactList: set list view item to contact ");

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: navigating to "+getString(R.string.contact_fragment));

                //pass the interface to the contact and send to it MainActivity
                mContactListener.onContactSelectedListener(contacts.get(position));
                Log.d(TAG, "onItemClick: called to finish interface and contact position "+contacts.get(position));
            }
        });


    }

    /**
     * initiates the appbar state toggle
     */

    private void toggleToolbarState() {
        Log.d(TAG, "toggleToolbarState: toggling AppbarState.");
        if (mAppBarState==STANDARD_APPBAR){
            setAppbarState(SEARCH_APPBAR);
        }else {
            setAppbarState(STANDARD_APPBAR);
        }

    }

    /**
     * sets the appbar state the either the search 'mode' or the standard mode
     * @param state
     */
    private void setAppbarState(int state) {
        Log.d(TAG, "setAppbarState: changing appbar state to: "+state);

        mAppBarState=state;
        if (mAppBarState==STANDARD_APPBAR){
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);

            // hide the keyboard
            View view=getView();
            Log.d(TAG, "setAppbarState: hide the keyboard of view"+view);
            InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                Log.d(TAG, "setAppbarState: hide keyboar of window token is: "+view.getWindowToken());
            } catch (Exception e) {
                Log.d(TAG, "setAppbarState: NullPointer Exception."+e.getMessage());
            }

        }else if (mAppBarState==SEARCH_APPBAR){
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            // open the keyboard
            InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setAppbarState(STANDARD_APPBAR);
        Log.d(TAG, "onResume: called to on Resume Method.");

    }

}
