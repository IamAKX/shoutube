package com.akashapplications.shoutube.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akashapplications.shoutube.Home;
import com.akashapplications.shoutube.R;
import com.akashapplications.shoutube.utilities.LocalPreference;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    MaterialEditText email,password;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        email = getView().findViewById(R.id.loginEmail);
        password = getView().findViewById(R.id.loginPassword);

        getView().findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authenticate())
                {
                    startActivity(new Intent(getContext(),Home.class));
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getContext(),"Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticate() {
        LocalPreference p = new LocalPreference(getContext());

        if(p.getEmail().equals(email.getText().toString()) && p.getPassword().equals(password.getText().toString()))
        {
            p.setLoginStatus(true);
            return true;
        }

        return false;
    }
}
