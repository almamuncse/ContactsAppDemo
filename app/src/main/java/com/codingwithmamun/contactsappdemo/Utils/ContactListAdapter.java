package com.codingwithmamun.contactsappdemo.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codingwithmamun.contactsappdemo.R;
import com.codingwithmamun.contactsappdemo.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends ArrayAdapter<Contact> {
    private static final String TAG = "ContactListAdapter";

    private LayoutInflater mInflater;
    private List<Contact>mContacts=null;
    private ArrayList<Contact>mArrayList;//Used for the search bar
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public ContactListAdapter(@Nullable Context context, @LayoutRes int resource, @Nullable List<Contact>contacts, String append) {
        super(context, resource, contacts);
        Log.d(TAG, "ContactListAdapter: called to ContactListAdapter Constructor.");
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource=resource;
        this.mContext=context;
        mAppend=append;
        this.mContacts=contacts;
        mArrayList=new ArrayList<>();//used to search bar
        this.mArrayList.addAll(mContacts);

    }

    private static class ViewHolder{
        TextView name;
        CircleImageView contactImage;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "getView: started.");

        /*
        *************ViewHolder Build pattern start*************
         */
        final ViewHolder holder;
        if (convertView==null){
            convertView=mInflater.inflate(layoutResource,parent,false);
            holder=new ViewHolder();

            //----------------------Stuff the Change-------------------------------
            holder.name=convertView.findViewById(R.id.contactName);
            holder.contactImage=convertView.findViewById(R.id.profileImage);
            holder.mProgressBar=convertView.findViewById(R.id.contactProgressBar);
            //---------------------------------------------------------------------

            convertView.setTag(holder);
            Log.d(TAG, "getView: create the converted view .");
        }else {
            holder= (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView: defult view created.");
        }

        //----------------------Stuff the Change-------------------------------
        String name_=getItem(position).getName();
        Log.d(TAG, "getView: get positon : and getName: "+getItem(position));
        Log.d(TAG, "getView: getName: "+getItem(position).getName());
        String imagePath=getItem(position).getProfileImage();
        holder.name.setText(name_);

        ImageLoader imageLoader=ImageLoader.getInstance();
        Log.d(TAG, "getView: ImageLoader getCofing.");
        imageLoader.displayImage(mAppend + imagePath, holder.contactImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted: called.");
                holder.mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "onLoadingFailed: called.");
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete: called.");
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "onLoadingCancelled: called.");
                holder.mProgressBar.setVisibility(View.GONE);
            }
        });

        //---------------------------------------------------------------------
        return convertView;
    }


    //Filter Class
    public void filter(String charecterText){
        charecterText=charecterText.toLowerCase(Locale.getDefault());
        mContacts.clear();
        if (charecterText.length()==0){
            mContacts.addAll(mArrayList);
        }else {
            mContacts.clear();
            for (Contact contact: mArrayList){
                if (contact.getName().toLowerCase(Locale.getDefault()).contains(charecterText)){
                    mContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}
