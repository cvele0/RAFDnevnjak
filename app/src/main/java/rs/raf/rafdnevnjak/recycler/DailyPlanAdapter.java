package rs.raf.rafdnevnjak.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;

public class DailyPlanAdapter extends ListAdapter<Obligation, DailyPlanAdapter.DailyViewHolder> {
    public final ArrayList<Obligation> obligations;
    private Context context;
    private Day day;

    private ClickListener clickListener;

    public DailyPlanAdapter(Day day, Context context, @NonNull DiffUtil.ItemCallback<Obligation> diffCallback,
                            ArrayList<Obligation> obligations, ClickListener clickListener) {
        super(diffCallback);
        this.day = day;
        this.context = context;
        this.obligations = obligations;
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onObligationClick(int position, Day day);
        void onEditClick(String name, Day day);
        void onDeleteClick(String name, Day day);
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.obligations_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.333333);
        return new DailyViewHolder(day, view, context, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        Obligation obligation = getItem(position);
        switch (obligation.getPriority()) {
            case LOW -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.low_priority));
            case MID -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.mid_priority));
            case HIGH -> holder.showObligation.setBackgroundColor(holder.itemView.getResources().getColor(R.color.high_priority));
        }
        initListeners(holder, position);
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

    private void initListeners(DailyViewHolder viewHolder, int position) {
        viewHolder.editObligation.setOnClickListener(e -> {
            clickListener.onEditClick(viewHolder.nameOfObligation.getText().toString(), day);
        });

        viewHolder.deleteObligation.setOnClickListener(e -> {
            clickListener.onDeleteClick(viewHolder.nameOfObligation.getText().toString(), day);
        });

        viewHolder.obligationsLayout.setOnClickListener(e -> {
            clickListener.onObligationClick(position, day);
        });
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        private Day day;
        private Context context;
        public ImageView showObligation;
        public TextView timeOfObligation;
        public TextView nameOfObligation;
        public ImageView editObligation;
        public ImageView deleteObligation;
        public LinearLayout obligationsLayout;
        private ClickListener clickListener;

        public DailyViewHolder(Day day, @NonNull View itemView, Context context, ClickListener clickListener) {
            super(itemView);
            this.day = day;
            showObligation = itemView.findViewById(R.id.showObligation);
            timeOfObligation = itemView.findViewById(R.id.timeOfObligation);
            nameOfObligation = itemView.findViewById(R.id.nameOfObligation);
            editObligation = itemView.findViewById(R.id.editObligation);
            deleteObligation = itemView.findViewById(R.id.deleteObligation);
            obligationsLayout = itemView.findViewById(R.id.obligationsCellLayout);
            this.context = context;
            this.clickListener = clickListener;
        }
    }
}
