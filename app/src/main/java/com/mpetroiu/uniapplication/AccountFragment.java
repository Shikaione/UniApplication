package com.mpetroiu.uniapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = " AccountFragment";

    private FirebaseUser mFirebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        view.findViewById(R.id.deleteAccount).setOnClickListener(this);

        return view;
    }

    private void deleteAccount(){

       mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

       if(mFirebaseUser != null){

            mFirebaseUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "isSuccessfull");
                                Toast.makeText(getContext(), "Account Deleted!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginOptionsActivity.class));
                            }
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.deleteAccount) {
            deleteAccount();
        }
    }
}
