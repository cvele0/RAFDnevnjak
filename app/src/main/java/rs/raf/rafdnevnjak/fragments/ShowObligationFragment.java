package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;

public class ShowObligationFragment extends Fragment {
    private TextView dateText;
    private TextView timeText;
    private TextView nameText;
    private EditText editText;
    private Obligation obligation;
    private Day day;


    public ShowObligationFragment(Day day, Obligation obligation) {
        super(R.layout.fragment_show_obligation);
        this.day = day;
        this.obligation = obligation;
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
        dateText = view.findViewById(R.id.showObligationDateText);
        timeText = view.findViewById(R.id.obligationTimeText);
        nameText = view.findViewById(R.id.obligationNameText);
        editText = view.findViewById(R.id.showObligationEditText);
        setView();
    }

    private void setView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd. yyyy.");
        dateText.setText(day.getDate().format(formatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String textForTime = obligation.getStartTime().format(timeFormatter);
        textForTime += " - ";
        textForTime += obligation.getEndTime().format(timeFormatter);
        timeText.setText(textForTime);

        nameText.setText(obligation.getName());

        editText.setText(obligation.getText());
    }
    private void initListeners() {

    }
}
