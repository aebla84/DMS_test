package dms.deideas.zas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;


/**
 * Created by dmadmin on 06/07/2016.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> implements View.OnClickListener {

    private List<Order> orders;
    private HistoricalListener listener;
    private Comparator<Order> comparator;


    public HistoricalAdapter(HistoricalListener listener) {
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    public HistoricalAdapter(List<Order> orders, HistoricalListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @Override
    public HistoricalAdapter.HistoricalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_historical, parent, false);
        card.setOnClickListener(this);
        return new HistoricalViewHolder(card);
    }

    @Override
    public void onBindViewHolder(HistoricalAdapter.HistoricalViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.idorder.setText(String.valueOf(orders.get(position).getId()));
        holder.date.setText(convertFormatDate(orders.get(position).getCreated_at()));
        holder.money.setText(orders.get(position).getTotal());
    }

    public String convertFormatDate(String strDate)
    {
        String formattedDate="";
        SimpleDateFormat sourceFormat = new SimpleDateFormat(Constants.DATETIME_BBDD_FORMAT);
        SimpleDateFormat destFormat = new SimpleDateFormat(Constants.DATA_APP_FORMAT);

        Date date = null;
        try {
            date = sourceFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formattedDate = destFormat.format(date);
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setData(List<Order> items) {
        this.orders = items;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (int) v.getTag();
            Order order = orders.get(position);
            listener.onOrderClicked(v, order);
        }
    }

    public class HistoricalViewHolder extends RecyclerView.ViewHolder {
        public TextView idorder, date, state, money;
        public View card;

        public HistoricalViewHolder(View itemView) {
            super(itemView);

            card = itemView;
            idorder = (TextView) itemView.findViewById(R.id.idorder);
            date = (TextView) itemView.findViewById(R.id.date);
            state = (TextView) itemView.findViewById(R.id.state);
            money = (TextView) itemView.findViewById(R.id.money);
        }
    }

    public interface HistoricalListener {
        void onOrderClicked(View card, Order order);
    }

    public void setComparador(Comparator<Order> comparator) {
        this.comparator = comparator;
        reorder();
        notifyDataSetChanged();
    }

    private void reorder() {
        if (comparator != null && !orders.isEmpty()) {
            Collections.sort(orders, comparator);
        }
    }

    public void add(List<Order> newElements) {
        orders.addAll(newElements);
        reorder();
        notifyDataSetChanged();
    }

    public void replace(List<Order> elements) {
        orders.clear();
        add(elements);
    }
}
