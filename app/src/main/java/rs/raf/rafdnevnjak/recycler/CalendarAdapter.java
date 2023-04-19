package rs.raf.rafdnevnjak.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;
import rs.raf.rafdnevnjak.models.Priority;
import rs.raf.rafdnevnjak.modelviews.RecyclerViewModel;

public class CalendarAdapter extends ListAdapter<Day, CalendarAdapter.ViewHolder> {
    private final ArrayList<Day> daysOfMonth;
    private OnItemListener onItemListener;
    private Context context;
    private Fragment fragment;
    private RecyclerViewModel recyclerViewModel;

    public CalendarAdapter(Context context, @NonNull DiffUtil.ItemCallback<Day> diffCallback,
                           ArrayList<Day> daysOfMonth, OnItemListener onItemListener,
                           Fragment fragment) {
        super(diffCallback);
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Day day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day.getDayOfMonth());
        ConstraintLayout cl = holder.itemView.findViewById(R.id.cellLayout);

        recyclerViewModel = new ViewModelProvider(fragment.requireActivity()).get(RecyclerViewModel.class);
        ArrayList<Obligation> obligations = recyclerViewModel.getObligations()
                .getValue().get(new Day(day.getDate().minusDays(1)));
        Priority priority = null;
        if (obligations == null) return;
        for (Obligation ob : obligations) {
            if (priority == null) {
                priority = ob.getPriority();
            } else {
                if (priority.compareTo(ob.getPriority()) < 0) {
                    priority = ob.getPriority();
                }
            }
        }
        if (priority != null) {
            switch (priority) {
                case LOW -> cl.setBackground(holder.itemView.getContext().getDrawable(R.drawable.low_priority_layout_border));
                case MID -> cl.setBackground(holder.itemView.getContext().getDrawable(R.drawable.mid_priority_layout_border));
                case HIGH -> cl.setBackground(holder.itemView.getContext().getDrawable(R.drawable.high_priority_layout_border));
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView dayOfMonth;
        private final CalendarAdapter.OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getBindingAdapterPosition(), (String) dayOfMonth.getText());
        }
    }
}
