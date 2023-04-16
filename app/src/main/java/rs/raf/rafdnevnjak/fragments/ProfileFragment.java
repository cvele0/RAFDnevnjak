package rs.raf.rafdnevnjak.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

    private RelativeLayout changePasswordLayout;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button confirmButton;
    private Button backButton;
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

    private void loadProfileView() {
        changePasswordLayout.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);
        buttonChangePassword.setVisibility(View.VISIBLE);
        buttonLogOut.setVisibility(View.VISIBLE);
    }

    private void loadChangePasswordView() {
        changePasswordLayout.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        buttonChangePassword.setVisibility(View.INVISIBLE);
        buttonLogOut.setVisibility(View.INVISIBLE);
    }

    private void initView(View view) {
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        buttonChangePassword = view.findViewById(R.id.profileButtonChangePassword);
        buttonLogOut = view.findViewById(R.id.profileButtonLogOut);

        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        newPassword = view.findViewById(R.id.inputNewPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        confirmButton = view.findViewById(R.id.confirmButton);
        backButton = view.findViewById(R.id.backButton);

        loadProfileView();

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
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putString(MainActivity.PREF_LOGIN_KEY, "false")
                    .apply();
            ((MainActivity) requireActivity()).loadLoginView();
        });

        buttonChangePassword.setOnClickListener(e -> {
            loadChangePasswordView();
        });

        confirmButton.setOnClickListener(e -> {
            String newPass = newPassword.getText().toString();
            String confPass = confirmPassword.getText().toString();
            if (!newPass.equals(confPass)) {
                Toast.makeText(getContext(), getResources().getString(R.string.passwords_not_match), Toast.LENGTH_SHORT).show();
                return;
            }
            User loggedUser = ((MainActivity) requireActivity()).loadUser();
            if (loggedUser.getPassword().equals(newPass)) {
                Toast.makeText(getContext(), getResources().getString(R.string.same_password), Toast.LENGTH_SHORT).show();
                return;
            }
            User newUser = new User(loggedUser);
            newUser.setPassword(newPass);
            if (!newUser.isValidPassword()) {
                Toast.makeText(getContext(), getResources().getString(R.string.password_requirement), Toast.LENGTH_SHORT).show();
                return;
            }
            ((MainActivity) requireActivity()).saveUser(newUser);
            Toast.makeText(getContext(), getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
            loadProfileView();
        });

        backButton.setOnClickListener(e -> {
            loadProfileView();
        });
    }
}
