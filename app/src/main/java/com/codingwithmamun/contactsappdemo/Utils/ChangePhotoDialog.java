package com.codingwithmamun.contactsappdemo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingwithmamun.contactsappdemo.R;

import org.w3c.dom.Text;

import java.io.File;

public class ChangePhotoDialog extends DialogFragment {
    private static final String TAG = "ChangePhotoDialog";

    public interface onReceivedListener{
        void getBitmapImage(Bitmap bitmap);
        void getImagePath(String imagePath);
    }
    onReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_changephoto,container,false);

        //initialize the textview for starting the camera
        TextView takePhoto=view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera.");

                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,Init.CAMERA_REQUEST_CODE);

            }
        });

        //initialize the textview for choosing an image from memory
        TextView selectPhoto=view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessing the memory. ");
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Init.PICKFILE_REQUEST_CODE);
            }
        });

        //cancel button for the closing data
        TextView cancelDialog=view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog. ");
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPhotoReceived= (onReceivedListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException: "+e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result when taking a new image Camera
        if (requestCode==Init.CAMERA_REQUEST_CODE && requestCode== Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture.");

            //get the new image bitmap
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            Log.d(TAG, "onActivityResult: receieved bitmap"+bitmap);

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }

        if (requestCode==Init.PICKFILE_REQUEST_CODE&&resultCode==Activity.RESULT_OK){
            Uri selectedPath=data.getData();
            File file=new File(selectedPath.toString());
            Log.d(TAG, "onActivityResult: images."+file.getPath());

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getImagePath(file.getPath());
            getDialog().dismiss();
        }
    }
}
