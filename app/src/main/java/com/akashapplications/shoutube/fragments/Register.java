package com.akashapplications.shoutube.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.akashapplications.shoutube.Home;
import com.akashapplications.shoutube.R;
import com.akashapplications.shoutube.utilities.Constants;
import com.akashapplications.shoutube.utilities.LocalPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.model.SavePath;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.kingja.switchbutton.SwitchMultiButton;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {

    public Register() {
        // Required empty public constructor
    }

    ImageView imageView;
    private ArrayList<Image> images = new ArrayList<>();
    String name,email,password,age,gender="Male",image="na";
    MaterialEditText nameET, emailET, passwordET, ageET;
    SwitchMultiButton genderSwitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Home.class));
            }
        });

        imageView = getView().findViewById(R.id.registerProfileImage);

        nameET = getView().findViewById(R.id.registerName);
        emailET = getView().findViewById(R.id.registerEmail);
        passwordET = getView().findViewById(R.id.registerPassword);
        ageET = getView().findViewById(R.id.registerAge);

        genderSwitch = getView().findViewById(R.id.registerGender);

        genderSwitch.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                gender = tabText;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchImagePicker();
            }
        });

        getView().findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                age = ageET.getText().toString();

                if(validate())
                {
                    if(new LocalPreference(getContext()).saveDetals(name,email,password,age,gender,image))
                    {
                        new LocalPreference(getContext()).setLoginStatus(true);
                        Toast.makeText(getContext(),"Welcome "+name,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(),Home.class));
                        getActivity().finish();
                    }
                    
                }
            }
        });

    }


    private void launchImagePicker() {

        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#ffffff")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#AC3B3D")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#AC3B3D")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#AC3B3D")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setMaxSize(1)                     //  Max images can be selected
                .setSavePath("ShouTube")         //  Image capture folder name
                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            image = images.get(0).getPath();
            Glide.with(getContext())
                    .load(image)
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user))
                    .into(imageView);
        }
        else
            image = "na";
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean validate()
    {
        try{

            if(name.equals("") || email.equals("") || password.equals("") || age.equals(""))
            {
                Toast.makeText(getContext(),"All fields are mandatory",Toast.LENGTH_SHORT).show();
                return false;
            }


            if(image.equalsIgnoreCase("na"))
            {
                Toast.makeText(getContext(),"Please select profile image",Toast.LENGTH_SHORT).show();
                return false;
            }


            Pattern pattern;
            Matcher matcher;



            pattern = Pattern.compile(Constants.EMAIL_PATTERN);
            matcher = pattern.matcher(email);
            if(!matcher.matches())
            {
                emailET.setError("Invalid email");
                return false;
            }

            
            if(password.length()<8)
            {
                passwordET.setError("Atleast 8 characters required");
                return false;
            }

            pattern = Pattern.compile(Constants.PASSWORD_PATTER);
            matcher = pattern.matcher(password);
            if(!matcher.matches())
            {
                passwordET.setError("Password should contain uppercase, lowercase, special character and digit");
                return false;
            }

            int a = Integer.parseInt(age);
            if(a<=0 || a>120)
            {
                ageET.setError("Invalid age");
                return false;
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"Some value is not as per our expectation",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
