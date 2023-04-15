package rs.raf.rafdnevnjak.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;

public class CalendarAdapter extends ListAdapter<Day, CalendarAdapter.ViewHolder> {

//    private final Consumer<Day> onDayClicked;
    private final ArrayList<String> daysOfMonth;
    private OnItemListener onItemListener;
    private Context context;

    public CalendarAdapter(Context context, @NonNull DiffUtil.ItemCallback<Day> diffCallback,
                           ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        super(diffCallback);
        this.context = context;
//        this.onDayClicked = onDayClicked;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);

        Random random = new Random();
        int rnd = random.nextInt(3);
        switch (rnd) {
            case 0 -> holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.babyBlue));
            case 1 -> holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.silverBlue));
            case 2 -> holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.loginText));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView dayOfMonth;
        private final CalendarAdapter.OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
//            itemView.setOnClickListener(v -> {
//                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
//                    onItemClicked.accept(getBindingAdapterPosition());
//                }
//            });
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getBindingAdapterPosition(), (String) dayOfMonth.getText());
        }

//        public void bind(String day) {
//            ((TextView) itemView.findViewById(R.id.cellDayText)).setText(day);
//        }
    }
}
