package ir.mtajik.android.rxjavaexample.paging;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.mtajik.android.rxjavaexample.R;

import static android.content.ContentValues.TAG;

public class PagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> items = new ArrayList<>();

    public PagingAdapter() {
    }

    public void addItems(List<String> items) {
        this.items.addAll(items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paging_item, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ItemViewHolder) holder).bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView item;

        public ItemViewHolder(View itemView) {
            super(itemView);

            item = (TextView) itemView.findViewById(R.id.viewHolder_textView);
        }

        void bind(String content) {
            item.setText(content);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: "+item.getText().toString());
                }
            });
        }

    }
}
