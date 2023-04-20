package rs.raf.rafdnevnjak.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.rafdnevnjak.models.Obligation;

public class ObligationDiffItemCallback extends DiffUtil.ItemCallback<Obligation> {

    @Override
    public boolean areItemsTheSame(@NonNull Obligation oldItem, @NonNull Obligation newItem) {
        return oldItem.getName().equals(newItem.getName());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Obligation oldItem, @NonNull Obligation newItem) {
        return oldItem.getName().equals(newItem.getName()) &&
                oldItem.getPriority().equals(newItem.getPriority()) &&
                oldItem.getStartTime().equals(newItem.getStartTime()) &&
                oldItem.getEndTime().equals(newItem.getEndTime()) &&
                oldItem.getText().equals(newItem.getText());
    }
}
