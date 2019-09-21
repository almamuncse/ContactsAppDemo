package com.codingwithmamun.contactsappdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codingwithmamun.contactsappdemo.Utils.ContactPropertyListAdapter;
import com.codingwithmamun.contactsappdemo.Utils.UniversalImageLoader;
import com.codingwithmamun.contactsappdemo.models.Contact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactFragment extends Fragment {

    public interface onEditContactListener{
        void onEditContactSelecd(Contact contact);
    }
    onEditContactListener mOnEditContactListener;

    private static final String TAG = "ContactFragment";
    private Toolbar toolbar;
    private Contact mContact;
    private TextView mContactName;
    private CircleImageView mContactImage;
    private ListView mListView;

    //This will evade the nullPointer exception when adding to a new bundle from MainActivity
    public ContactFragment(){
        super();
        Log.d(TAG, "ContactFragment: setArguments in Bundle.Because null pointer exception.");
        setArguments(new Bundle());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact,container,false);
        toolbar=view.findViewById(R.id.contactToolbar);
        mContactName=view.findViewById(R.id.tvName);
        mContactImage=view.findViewById(R.id.contactImage);
        mListView=view.findViewById(R.id.lvContactProperties);
        Log.d(TAG, "onCreateView: started.");
        mContact=getContactFromBundle();



        //required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        init();

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


        //navigate to the edit contact fragment to edit the contact selected.
        ImageView ivEdit=view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked ivEdit icon. ");
                mOnEditContactListener.onEditContactSelecd(mContact);
            }
        });

        return view;
    }

    private void init(){
        Log.d(TAG, "init: initialize image and name of contactFragment.");
        mContactName.setText(mContact.getName());
        UniversalImageLoader.setImage(mContact.getProfileImage(),mContactImage,null,"https://");

        ArrayList<String>properties=new ArrayList<>();
        properties.add(mContact.getPhonenumber());
        properties.add(mContact.getEmail());

        ContactPropertyListAdapter adapter=new ContactPropertyListAdapter(getActivity(),R.layout.layout_cardview,properties);
        mListView.setAdapter(adapter);

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
     * Retrieves the selected contact from bundle (coming from MainActivity)
     * @return
     */
    private Contact getContactFromBundle(){
        Log.d(TAG, "getContactFromBundle: arguments: "+getArguments());

        Bundle bundle=this.getArguments();
        if (bundle!=null){
            return bundle.getParcelable(getString(R.string.contact));
        }else {
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnEditContactListener= (onEditContactListener) getActivity();
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException: "+e.getMessage());
        }
    }
}
