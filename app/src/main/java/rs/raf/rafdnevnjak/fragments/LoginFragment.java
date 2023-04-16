package rs.raf.rafdnevnjak.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rs.raf.rafdnevnjak.MainActivity;
import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.User;

public class LoginFragment extends Fragment {
    private EditText username;
    private EditText password;
    private EditText email;
    private Button loginBtn;
    private TextView usernameText;
    private TextView passwordText;
    private TextView emailText;

    public LoginFragment() {
        super(R.layout.fragment_login);
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
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        loginBtn = view.findViewById(R.id.loginButton);
        usernameText = view.findViewById(R.id.usernameText);
        passwordText = view.findViewById(R.id.passwordText);
        emailText = view.findViewById(R.id.emailText);

        User loggedUser = ((MainActivity) requireActivity()).loadUser();
        if (loggedUser != null) {
            email.setText(loggedUser.getEmail());
        }

        usernameText.setVisibility(View.INVISIBLE);
        passwordText.setVisibility(View.INVISIBLE);
        emailText.setVisibility(View.INVISIBLE);
    }

    private boolean fieldsEmpty() {
        boolean result = false;
        if (username == null || username.getText().toString().equals("")) {
            usernameText.setVisibility(View.VISIBLE);
            result = true;
        } else {
            usernameText.setVisibility(View.INVISIBLE);
        }
        if (password == null || password.getText().toString().equals("")) {
            passwordText.setVisibility(View.VISIBLE);
            result = true;
        } else {
            passwordText.setVisibility(View.INVISIBLE);
        }
        if (email == null || email.getText().toString().equals("")) {
            emailText.setVisibility(View.VISIBLE);
            result = true;
        } else {
            emailText.setVisibility(View.INVISIBLE);
        }
        return result;
    }

    private void initListeners() {
        loginBtn.setOnClickListener(e -> {
            User loggedUser = ((MainActivity) requireActivity()).getLoggedUser();
            if (fieldsEmpty()) return;
            User user = new User(username.getText().toString(), password.getText().toString(), email.getText().toString());
            if (!user.isValidEmail()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.email_requirement), Toast.LENGTH_LONG).show();
                return;
            }
            if (!user.isValidPassword()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.password_requirement), Toast.LENGTH_LONG).show();
                return;
            }

            if (loggedUser == null) { // first log in
                ((MainActivity) requireActivity()).saveUser(user);
            } else {
                if (!loggedUser.getPassword().equals(user.getPassword())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.incorrect_password), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!loggedUser.getEmail().equals(user.getEmail())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.incorrect_email), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!loggedUser.getUsername().equals(user.getUsername())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.incorrect_username), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // logging
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putString(MainActivity.PREF_LOGIN_KEY, "true")
                    .apply();
            ((MainActivity) requireActivity()).loadMainView();
        });
    }
}
