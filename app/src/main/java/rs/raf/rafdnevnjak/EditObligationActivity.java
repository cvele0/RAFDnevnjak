package rs.raf.rafdnevnjak;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;
import rs.raf.rafdnevnjak.models.Priority;

public class EditObligationActivity extends AppCompatActivity {
    public static final String EDIT_OBLIGATION_OBLIGATION_KEY = "editObligationObligationKey";
    public static final String EDIT_OBLIGATION_DAY_KEY = "editObligationDayKey";
    public static final String IS_EDIT_KEY = "isEditKey";
    public static final String POSITION_KEY = "positionKey";
    public static final String EDIT_OBLIGATION_RETURN_KEY = "editObligationReturnKey";
    public static final String RETURN_POSITION_RETURN_KEY = "returnPositionReturnKey";

    private TextView dateText;
    private TextView low;
    private TextView mid;
    private TextView high;
    private EditText titleText;
    private LinearLayout timeLayout;
    private TextView timeText;
    private EditText editText;
    private Button createEditButton;
    private Button cancelButton;
    private Day day;
    private Obligation obligation;
    private boolean isEdit;
    private int returnPosition;
    private int position;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_obligation);
        init();
    }

    private void init() {
        dateText = findViewById(R.id.editObligationDateText);
        low = findViewById(R.id.editObligationLow);
        mid = findViewById(R.id.editObligationMid);
        high = findViewById(R.id.editObligationHigh);
        titleText = findViewById(R.id.editObligationTitleText);
        timeLayout = findViewById(R.id.editObligationTimeLayout);
        timeText = findViewById(R.id.editObligationTimeText);
        editText = findViewById(R.id.editObligationEditText);
        createEditButton = findViewById(R.id.editObligationCreateButton);
        cancelButton = findViewById(R.id.editObligationCancelButton);

        Intent intent = getIntent();
        if (intent != null) {
            Day day = (Day) intent.getSerializableExtra(EDIT_OBLIGATION_DAY_KEY);
            Obligation obligation = (Obligation) intent.getSerializableExtra(EDIT_OBLIGATION_OBLIGATION_KEY);
            boolean is_edit = intent.getBooleanExtra(IS_EDIT_KEY, false);
            this.position = intent.getIntExtra(POSITION_KEY, -1);
            this.day = day;
            if (obligation != null) {
                this.obligation = obligation;
            } else {
                this.obligation = new Obligation();
            }
            this.isEdit = is_edit;
        }

        if (this.isEdit) {
            setEditView();
        } else {
            setNewView();
        }
        returnPosition = position;
        initListeners();
    }

    private void setNewView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd. yyyy.");
        dateText.setText(day.getDate().format(formatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String textForTime = obligation.getStartTime().format(timeFormatter);
        textForTime += " - ";
        textForTime += obligation.getEndTime().format(timeFormatter);
        timeText.setText(textForTime);

        titleText.setText(obligation.getName());

        editText.setText(obligation.getText());
        createEditButton.setText(R.string.create);
    }

    private void setEditView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd. yyyy.");
        dateText.setText(day.getDate().format(formatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String textForTime = obligation.getStartTime().format(timeFormatter);
        textForTime += " - ";
        textForTime += obligation.getEndTime().format(timeFormatter);
        timeText.setText(textForTime);

        titleText.setText(obligation.getName());

        editText.setText(obligation.getText());
        createEditButton.setText(R.string.edit);
    }

    private void setStartTime(int h, int m) {
         this.startTime = LocalTime.of(h, m);
         obligation.setStartTime(startTime);
    }

    private void setEndTime(int h, int m) {
        this.endTime = LocalTime.of(h, m);
        obligation.setEndTime(endTime);
        setTimeText();
    }

    private void setTimeText() {
        String text = "";
        if (startTime != null) {
            text += startTime.getHour() + ":" + startTime.getMinute();
        }
        text += " - ";
        if (endTime != null) {
            text += endTime.getHour() + ":" + endTime.getMinute();
        }
        timeText.setText(text);
    }

    private void initListeners() {
        timeLayout.setOnClickListener(e -> {
            LocalTime timeNow = LocalTime.now();
            int hour = timeNow.getHour();
            int minute = timeNow.getMinute();

            TimePickerDialog endTimePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) ->
                    EditObligationActivity.this.setEndTime(hourOfDay, minute1), hour, minute, true);

            TimePickerDialog startTimePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute12) -> {
                EditObligationActivity.this.setStartTime(hourOfDay, minute12);
                endTimePickerDialog.show();
            }, hour, minute, true);

            // Show the time picker dialog
            startTimePickerDialog.show();
        });

        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                obligation.setName(titleText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                obligation.setText(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        low.setOnClickListener(e -> {
            Toast.makeText(this, R.string.low, Toast.LENGTH_SHORT).show();
            obligation.setPriority(Priority.LOW);
        });

        mid.setOnClickListener(e -> {
            Toast.makeText(this, R.string.mid, Toast.LENGTH_SHORT).show();
            obligation.setPriority(Priority.MID);
        });

        high.setOnClickListener(e -> {
            Toast.makeText(this, R.string.high, Toast.LENGTH_SHORT).show();
            obligation.setPriority(Priority.HIGH);
        });

        createEditButton.setOnClickListener(e -> closeActivity());

        cancelButton.setOnClickListener(e -> finish());
    }

    @Override
    public void onBackPressed() {}

    private void closeActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EDIT_OBLIGATION_RETURN_KEY, obligation);
        returnIntent.putExtra(RETURN_POSITION_RETURN_KEY, returnPosition);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
