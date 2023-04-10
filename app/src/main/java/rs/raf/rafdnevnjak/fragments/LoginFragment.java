package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

import rs.raf.rafdnevnjak.R;
import timber.log.Timber;

public class LoginFragment extends Fragment {
    private EditText username;
    private EditText password;
    private EditText email;
    private Button loginBtn;
    private TextView usernameText;
    private TextView passwordText;
    private TextView emailText;
    private String validPassword = null;

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
        readPasswordFromFile();
        initListeners();
    }

    private void readPasswordFromFile() {
        String fileName = "filename.txt";
        String contents = "";

        try {
            InputStream stream = getActivity().getAssets().open(fileName);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            contents = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        loginBtn = view.findViewById(R.id.loginButton);
        usernameText = view.findViewById(R.id.usernameText);
        passwordText = view.findViewById(R.id.passwordText);
        emailText = view.findViewById(R.id.emailText);

        usernameText.setVisibility(View.INVISIBLE);
        passwordText.setVisibility(View.INVISIBLE);
        emailText.setVisibility(View.INVISIBLE);
    }

    private void initListeners() {
        loginBtn.setOnClickListener(e -> {
            Timber.e("Password is %s", validPassword);
        });
    }
}
