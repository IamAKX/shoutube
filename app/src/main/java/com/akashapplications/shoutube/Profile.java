package com.akashapplications.shoutube;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akashapplications.shoutube.R;
import com.akashapplications.shoutube.utilities.Constants;
import com.akashapplications.shoutube.utilities.LocalPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.kingja.switchbutton.SwitchMultiButton;

public class Profile extends AppCompatActivity {

    ImageView imageView;
    MaterialEditText nameET,emailET,passwordET,ageET,channelIDET;
    SwitchMultiButton genderSwitch;
    String name,email,password,age,channelID,gender="Male",image="na";
    private ArrayList<Image> images = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameET = findViewById(R.id.profileName);
        emailET = findViewById(R.id.profileEmail);
        passwordET = findViewById(R.id.profilePassword);
        ageET = findViewById(R.id.profileAge);
        channelIDET = findViewById(R.id.profileChannelID);
        genderSwitch = findViewById(R.id.profileGender);
        imageView = findViewById(R.id.profileProfileImage);

        getSupportActionBar().setTitle("Profile");

        LocalPreference localPreference = new LocalPreference(getBaseContext());
        nameET.setText(localPreference.getName());
        emailET.setText(localPreference.getEmail());
        passwordET.setText(localPreference.getPassword());
        ageET.setText(localPreference.getAge());
        channelIDET.setText(localPreference.getChannelID());
        gender = localPreference.getGender();
        image = localPreference.getImgPath();


        genderSwitch.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                gender = tabText;
            }
        });
    
        nameET.setEnabled(false);
        emailET.setEnabled(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchImagePicker();
            }
        });

        Glide.with(getBaseContext())
                .load(image)
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user))
                .into(imageView);
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
            Glide.with(getBaseContext())
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save)
        {
            name = nameET.getText().toString();
            email = emailET.getText().toString();
            password = passwordET.getText().toString();
            age = ageET.getText().toString();
            channelID = channelIDET.getText().toString();
            
            if(validate())
            {
                new LocalPreference(getBaseContext()).saveDetals(name,email,password,age,gender,image);
                new LocalPreference(getBaseContext()).setChannelId(channelID);
                startActivity(new Intent(getBaseContext(),Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        }
        return false;
    }

    private boolean validate() {

        if(image.equalsIgnoreCase("na"))
        {
            Toast.makeText(getBaseContext(),"Please select profile image",Toast.LENGTH_SHORT).show();
            return false;
        }


        Pattern pattern;
        Matcher matcher;



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
        return true;
    }
}
