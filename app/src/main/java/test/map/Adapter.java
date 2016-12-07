package test.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final LayoutInflater inflater;
    private final List<Item> dataset;

    public Adapter(Context context, List<Item> list) {
        dataset = list;
        inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = dataset.get(position);
        holder.textTitle.setText(item.title);
        holder.textDesc.setText(item.desc);
        holder.textSub.setText(item.sub);
        holder.textTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResUtils.getIcon(item.type), 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public long getItemId(int position) {
        return dataset.get(position).hashCode();
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView textTitle;
    TextView textDesc;
    TextView textSub;

    public ItemViewHolder(View itemView) {
        super(itemView);
        textTitle = (TextView) itemView.findViewById(R.id.text_title);
        textDesc = (TextView) itemView.findViewById(R.id.text_desc);
        textSub = (TextView) itemView.findViewById(R.id.text_sub);
    }
}
