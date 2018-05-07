package com.mpetroiu.uniapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "PasswordFragment";

    private FirebaseUser mFirebaseUser;

    private EditText newPassword, verifiedPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_password, container, false);

        newPassword = (EditText) view.findViewById(R.id.changePassword);
        verifiedPassword = (EditText) view.findViewById(R.id.verifyChangePassword);
        view.findViewById(R.id.change).setOnClickListener(this);

        return view;
    }

    private void updatePassword(String newPassword){

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (!validateForm()) {
            return;
        }

            if(mFirebaseUser != null){
                    Log.e(TAG, "User : " +mFirebaseUser);

                mFirebaseUser.updatePassword(newPassword).addOnCompleteListener(getActivity(),
                        new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "isSuccessfull");
                            Toast.makeText(getContext(), "Password updated!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getActivity(), LoginOptionsActivity.class));
                        } else {
                            Log.d(TAG, "isNotSuccessfull");
                            Toast.makeText(getContext(), "Failed to update password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    private boolean validateForm() {
        boolean valid = true;

        String newPass = newPassword.getText().toString();
        String verifiedPass = verifiedPassword.getText().toString();

        if (TextUtils.isEmpty(newPass)) {
            newPassword.setError("Required.");
            valid = false;
        } else {
            newPassword.setError(null);
        }

        if (TextUtils.isEmpty(verifiedPass)) {
            verifiedPassword.setError("Required.");
            valid = false;
        } else {
            verifiedPassword.setError(null);
        }

        if(!newPass.equals(verifiedPass)){
            Toast.makeText(getActivity(),"Passwords do not match", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.change) {
            updatePassword(newPassword.getText().toString());
        }
    }
}
