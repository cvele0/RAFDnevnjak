package rs.raf.rafdnevnjak.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;

public class DailyPlanAdapter extends ListAdapter<Obligation, DailyPlanAdapter.DailyViewHolder> {
    public final ArrayList<Obligation> obligations;
    private Context context;
    private Day day;

    public DailyPlanAdapter(Day day, Context context, @NonNull DiffUtil.ItemCallback<Obligation> diffCallback,
                            ArrayList<Obligation> obligations) {
        super(diffCallback);
        this.day = day;
        this.context = context;
        this.obligations = obligations;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.obligations_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.333333);
        return new DailyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        Obligation obligation = getItem(position);
        switch (obligation.getPriority()) {
            case LOW -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.low_priority));
            case MID -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.mid_priority));
            case HIGH -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.high_priority));
        }
        holder.nameOfObligation.setText(obligation.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // create a formatter
        String timeToPrint = obligation.getStartTime().format(formatter) + " - ";
        timeToPrint += obligation.getEndTime().format(formatter);
        holder.timeOfObligation.setText(timeToPrint);
        holder.showObligation.setImageResource(R.drawable.show_obligation);
        holder.editObligation.setImageResource(R.drawable.edit_obligation);
        holder.deleteObligation.setImageResource(R.drawable.delete_obligation);
        LocalTime now = LocalTime.now();
        LocalTime obg = obligation.getStartTime();
        LocalTime timeNow = LocalTime.of(now.getHour(), now.getMinute());
        LocalTime timeObg = LocalTime.of(obg.getHour(), obg.getMinute());
        if (timeObg.isBefore(timeNow)) {
            holder.obligationsLayout.setBackgroundColor(
                    holder.itemView.getResources().getColor(R.color.gray)
            );
        } else {
            holder.obligationsLayout.setBackgroundColor(
                    holder.itemView.getResources().getColor(R.color.white)
            );
        }
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        public ImageView showObligation;
        public TextView timeOfObligation;
        public TextView nameOfObligation;
        public ImageView editObligation;
        public ImageView deleteObligation;
        public LinearLayout obligationsLayout;

        public DailyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            showObligation = itemView.findViewById(R.id.showObligation);
            timeOfObligation = itemView.findViewById(R.id.timeOfObligation);
            nameOfObligation = itemView.findViewById(R.id.nameOfObligation);
            editObligation = itemView.findViewById(R.id.editObligation);
            deleteObligation = itemView.findViewById(R.id.deleteObligation);
            obligationsLayout = itemView.findViewById(R.id.obligationsCellLayout);
            this.context = context;
            initListeners();
        }

        private void initListeners() {
            editObligation.setOnClickListener(e -> {
                Toast.makeText(context, "Edit clicked", Toast.LENGTH_LONG).show();
            });

            deleteObligation.setOnClickListener(e -> {
                Toast.makeText(context, "delete clicked", Toast.LENGTH_LONG).show();
            });
        }
    }
}