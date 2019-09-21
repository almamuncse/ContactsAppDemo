package com.codingwithmamun.contactsappdemo.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithmamun.contactsappdemo.MainActivity;
import com.codingwithmamun.contactsappdemo.R;
import com.codingwithmamun.contactsappdemo.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactPropertyListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "ContactPropertyListAdap";

    private LayoutInflater mInflater;
    private List<String> mProperties=null;
    private int layoutResource;
    private Context mContext;

    public ContactPropertyListAdapter(@Nullable Context context, @LayoutRes int resource, @Nullable List<String>properties) {
        super(context, resource, properties);
        Log.d(TAG, "ContactListAdapter: called to ContactListAdapter Constructor.");
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource=resource;
        this.mContext=context;
        this.mProperties=properties;

    }

    //----------------------Stuff the Change-------------------------------
    private static class ViewHolder{
        TextView property;
        ImageView rightIcon;
        ImageView leftIcon;
    }
    //----------------------------------------------------------------------

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

            holder.property=convertView.findViewById(R.id.tvMiddleCardView);
            holder.rightIcon=convertView.findViewById(R.id.iconRightCardView);
            holder.leftIcon=convertView.findViewById(R.id.iconLeftCardView);

            //---------------------------------------------------------------------

            convertView.setTag(holder);
            Log.d(TAG, "getView: create the converted view .");
        }else {
            holder= (ViewHolder) convertView.getTag();
            Log.d(TAG, "getView: defult view created.");
        }

        //----------------------Stuff the Change-------------------------------

        final String property=getItem(position);
        Log.d(TAG, "getView: Al mamun View the Property..............:"+property);
        holder.property.setText(property);

        //check if it's an email or a phone number
        if (property.contains("@")){
            Log.d(TAG, "getView: set up email icon.");
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_email",null,mContext.getPackageName()));

            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "onClick: sending email.");
//                    Log.i("Send email", "");
//                    String[] TO = {""};
//                    String[] CC = {""};
//                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//                    emailIntent.setData(Uri.parse("mailto:"));
//                    emailIntent.setType("text/plain");
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//
//                    try {
//                        mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                        Log.i("Finish sending email...", "");
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(mContext, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//                    }

                    Intent emailIntent=new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{property});
                    //optional setting for email....
//                    String email=property;
//                    String subject="subject";
//                    String body="body.........";
//                    String uriText="mailto:" +Uri.encode(email)+"?subject="+ Uri.encode(subject)+"&body="+
//                            Uri.encode(body);
//                    Uri uri=Uri.parse(uriText);
//                    emailIntent.setData(uri);
                    mContext.startActivity(emailIntent);
                }
            });

        }else if (property.length()!=0){
            Log.d(TAG, "getView: set up phone and message icon.");
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_phone",null,mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((MainActivity)mContext).checkPermission(Init.PHONE_PERMISSION)){
                        Log.d(TAG, "onClick: Initialize phone call....");
                        Intent callIntennt=new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",property,null));
                        mContext.startActivity(callIntennt);
                    }else {
                        ((MainActivity)mContext).verifyPermission(Init.PHONE_PERMISSION);
                    }
                }
            });

            //setup the sending message icon
            holder.rightIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_message",null,mContext.getPackageName()));
            holder.rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: initialize text message.....");
                    Intent messageIntent=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",property,null));
                    mContext.startActivity(messageIntent);


                }
            });
        }

        //---------------------------------------------------------------------
        return convertView;
    }
}
