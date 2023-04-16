package rs.raf.rafdnevnjak.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rs.raf.rafdnevnjak.MainActivity;
import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.User;

public class ProfileFragment extends Fragment {
    private TextView profileName;
    private TextView profileEmail;
    private Button buttonChangePassword;
    private Button buttonLogOut;
    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initListeners();
    }

    private void initView(View view) {
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        buttonChangePassword = view.findViewById(R.id.profileButtonChangePassword);
        buttonLogOut = view.findViewById(R.id.profileButtonLogOut);

        User loggedUser = ((MainActivity) requireActivity()).loadUser();
        if (loggedUser != null) {
            profileName.setText(loggedUser.getUsername());
            profileEmail.setText(loggedUser.getEmail());
        } else {
            profileName.setText(getResources().getString(R.string.not_available));
            profileEmail.setText(getResources().getString(R.string.not_available));
        }
    }
    private void initListeners() {
        buttonLogOut.setOnClickListener(e -> {
            User user = ((MainActivity) requireActivity()).loadUser();
            // log out
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putString(MainActivity.PREF_LOGIN_KEY, "false")
                    .apply();
            ((MainActivity) requireActivity()).loadLoginView();
        });
    }
}
