package rs.raf.rafdnevnjak.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.rafdnevnjak.models.Day;

public class DayDiffItemCallback extends DiffUtil.ItemCallback<Day> {

    @Override
    public boolean areItemsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
        return oldItem.getDay().equals(newItem.getDay());
    }
}
