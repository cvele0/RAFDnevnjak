package rs.raf.rafdnevnjak.activities;

import static rs.raf.rafdnevnjak.fragments.CalendarFragment.SWIPE_THRESHOLD;
import static rs.raf.rafdnevnjak.fragments.CalendarFragment.SWIPE_VELOCITY_THRESHOLD;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.listeners.OnSwipeTouchListener;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;

public class ShowObligationActivity extends AppCompatActivity {
    public static final String OBLIGATION_KEY = "obligationKey";
    public static final String DAY_KEY = "dayKey";
    public static final String SELECTED_OBLIGATION_KEY = "selectedObligationKey";
    public static final String SHOW_OBLIGATION_RETURN_KEY = "showObligationReturnKey";

    private TextView dateText;
    private TextView timeText;
    private TextView nameText;
    private EditText editText;
    private Button editButton;
    private Button deleteButton;
    private LinearLayout linearLayout;
    private ArrayList<Obligation> obligations;
    private ArrayList<Obligation> beforeObligations;
    private Day day;
    private int selectedIndex;
    private int beforeSelectedIndex;
    private LinearLayout editTextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_obligation);
        init();
    }

    private void copyObligation(Obligation ob, int pos) {
        Obligation obligation = obligations.get(pos);
        obligation.setName(ob.getName());
        obligation.setPriority(ob.getPriority());
        obligation.setStartTime(ob.getStartTime());
        obligation.setEndTime(ob.getEndTime());
        obligation.setText(ob.getText());
        setView();
    }

    ActivityResultLauncher<Intent> editObligationActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Obligation obligation = (Obligation) data.getSerializableExtra(EditObligationActivity.EDIT_OBLIGATION_RETURN_KEY);
                    int returnPosition = data.getIntExtra(EditObligationActivity.RETURN_POSITION_RETURN_KEY, -1);
                    if (returnPosition == -1) {

                    } else {
                        copyObligation(obligation, returnPosition);
                    }
                    Toast.makeText(this, R.string.data_saved, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.data_discarded, Toast.LENGTH_LONG).show();
                }
            });

    private void init() {
        dateText = findViewById(R.id.showObligationDateText);
        timeText = findViewById(R.id.obligationTimeText);
        nameText = findViewById(R.id.obligationNameText);
        editText = findViewById(R.id.showObligationEditText);
        editButton = findViewById(R.id.showObligationEdit);
        deleteButton = findViewById(R.id.showObligationDelete);
        linearLayout = findViewById(R.id.showObligationLayout);
        editTextLayout = findViewById(R.id.showObligationEditTextLayout);

        Intent intent = getIntent();
        if (intent != null) {
            Day day = (Day) intent.getSerializableExtra(DAY_KEY);
            ArrayList<Obligation> obligations = (ArrayList<Obligation>) intent.getSerializableExtra(OBLIGATION_KEY);
            int selected = intent.getIntExtra(SELECTED_OBLIGATION_KEY, 0);
            this.day = day;
            this.obligations = obligations;
            this.selectedIndex = selected;
        }

        setView();
        initListeners();
    }

    private void nextIndex() {
        int sz = obligations.size();
        selectedIndex = (selectedIndex + 1) % sz;
        setView();
    }

    private void prevIndex() {
        int sz = obligations.size();
        selectedIndex = (selectedIndex - 1 + sz) % sz;
        setView();
    }

    private void setView() {
        if (obligations == null || obligations.isEmpty()) {
            dateText.setText(R.string.no_more_obligations);
            timeText.setText("");
            nameText.setText("");
            editText.setText("");
            return;
        }

        Obligation selectedObligation = obligations.get(selectedIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd. yyyy.");
        dateText.setText(day.getDate().format(formatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String textForTime = selectedObligation.getStartTime().format(timeFormatter);
        textForTime += " - ";
        textForTime += selectedObligation.getEndTime().format(timeFormatter);
        timeText.setText(textForTime);

        nameText.setText(selectedObligation.getName());

        editText.setText(selectedObligation.getText());
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);// TODO sredidti - text view
    }

    private void deleteItem() {
        beforeSelectedIndex = selectedIndex;
        beforeObligations = new ArrayList<>(obligations);
        obligations.remove(selectedIndex);
        if (obligations.size() >= 1) {
            selectedIndex = (selectedIndex % (int) obligations.size());
        }
        setView();
    }

    private void restoreItem() {
        selectedIndex = beforeSelectedIndex;
        obligations = beforeObligations;
        setView();
    }
    private void initListeners() {
        deleteButton.setOnClickListener(v -> {
            if (obligations.isEmpty()) return;
            Snackbar snackbar = Snackbar.make(linearLayout, "Item deleted", Snackbar.LENGTH_LONG);
            deleteItem();
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    restoreItem();
                    Toast.makeText(getApplicationContext(), "Item restored", Toast.LENGTH_SHORT).show();
                }
            });
            // Show the Snackbar
            snackbar.show();
        });

        editButton.setOnClickListener(e -> {
            if (obligations.isEmpty()) return;
            Intent newIntent = new Intent(this, EditObligationActivity.class);
            newIntent.putExtra(EditObligationActivity.EDIT_OBLIGATION_DAY_KEY, day);
            newIntent.putExtra(EditObligationActivity.EDIT_OBLIGATION_OBLIGATION_KEY, obligations.get(selectedIndex));
            newIntent.putExtra(EditObligationActivity.IS_EDIT_KEY, true);
            newIntent.putExtra(EditObligationActivity.POSITION_KEY, selectedIndex);
            editObligationActivityResultLauncher.launch(newIntent);
        });

        initSwipeListener();
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private void closeActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SHOW_OBLIGATION_RETURN_KEY, obligations);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSwipeListener() {
        GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(@NonNull MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                        try {
                            float diffX = e2.getX() - e1.getX();
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
//                                    swipeUpAction();
                                    Toast.makeText(ShowObligationActivity.this, "left ro right", Toast.LENGTH_LONG).show();
                                } else {
//                                    swipeDownAction();
                                    Toast.makeText(ShowObligationActivity.this, "Right to left", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return false;
                    }
                });

        editText.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(ShowObligationActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(ShowObligationActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
        });

        linearLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                nextIndex();
            }

            @Override
            public void onSwipeRight() {
                prevIndex();
            }
        });
    }
}
