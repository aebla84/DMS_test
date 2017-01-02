package dms.deideas.zas.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements View.OnClickListener {

    private Integer countOrders;
    private List<Order> orders;
    private OrderListener listener;
    private String timeMax = "";

    private Comparator<Order> comparator;

    public OrderAdapter(OrderListener listener) {
        this.orders = new ArrayList<>();
        this.listener = listener;
    }
    public OrderAdapter(OrderListener listener,String timeMax) {
        this.orders = new ArrayList<>();
        this.listener = listener;
        this.timeMax = timeMax;
    }
    public OrderAdapter(List<Order> orders, OrderListener listener, Integer countOrders) {
        this.orders = orders;
        this.listener = listener;
        this.countOrders = countOrders;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        card.setOnClickListener(this);
        return new OrderViewHolder(card);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.restaurant_name.setText(orders.get(position).getRestaurant().getName());
        holder.direction_client.setText(orders.get(position).getShipping_address().getAddress_1());

        String strOrderStatus = String.valueOf(orders.get(position).getOrderstatus());

        holder.state.setText(getOrderStatusName(holder.state.getContext(), orders.get(position).getOrderstatus()));
        holder.idorder.setText(String.valueOf(orders.get(position).getId()));

        if(orders.get(position).getOrder_big()){
            holder.imgbigorder.setVisibility(View.VISIBLE);
        } else {
            holder.imgbigorder.setVisibility(View.INVISIBLE);
        }

        //region The state of payment change depending of payment details of order.
        if(!strOrderStatus.equals(Constants.ORDER_STATUS_rest_has_accepted) && !strOrderStatus.equals(Constants.ORDER_STATUS_problem) && !strOrderStatus.equals(Constants.ORDER_STATUS_driver_has_accepted) ){
            if (orders.get(position).getPayment_details().getMethod_id().contentEquals("cod") && orders.get(position).getPayment_details().getPaid() == false) {
                //holder.state_of_payment.setText(String.valueOf((orders.get(position).getTotal()) + " " + R.string.euro_Symbol));
                holder.state_of_payment.setText(String.valueOf((orders.get(position).getTotal()) + " €"));
                holder.state_of_payment.setVisibility(View.VISIBLE);
            } else {
                holder.state_of_payment.setText(R.string.paid_online);
                holder.state_of_payment.setVisibility(View.VISIBLE);
            }
        }
        //endregion

        //region The color of RelativeLayout Background change depending of status of order , the type of problem (if Orderstatus = problem), the time of kitchen or the priority of restaurant.
        switch (strOrderStatus)
        {
            case Constants.ORDER_STATUS_problem:
                // On function of type of incidence change relativelayout background color
                if (orders.get(position).getLista_incidencias() != null && orders.get(position).getLista_incidencias().size() > 0) {
                    switch (orders.get(position).getLista_incidencias().get(0).getTypeofincidencia()) {
                        case Constants.PROBLEM_drop_food_str:
                        case Constants.PROBLEM_wrong_plate_str:
                        case Constants.PROBLEM_wrong_order_str:
                        case Constants.PROBLEM_forget_plate_str:
                            holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                            holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                            holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                            break;
                        case Constants.PROBLEM_drop_drink_str:
                        case Constants.PROBLEM_wrong_drink_str:
                        case Constants.PROBLEM_forget_drink_str:
                            holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange));
                            break;
                        default:
                            holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                            holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                            holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                            break;
                    }
                }
                holder.hourKitchen.setText(orders.get(position).getMinutesOrderWithProblem() + " min");

                break;
            case Constants.ORDER_STATUS_rest_has_accepted:
            case Constants.ORDER_STATUS_driver_has_accepted:
                if (Integer.parseInt(orders.get(position).getTimeKitchen()) < Constants.ORDER_TIMEKITCHEN_0) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                } else if ((Integer.parseInt(orders.get(position).getTimeKitchen()) < Constants.ORDER_TIMEKITCHEN_5) && (Integer.parseInt(orders.get(position).getTimeKitchen()) >= Constants.ORDER_TIMEKITCHEN_0)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                } else if ((Integer.parseInt(orders.get(position).getTimeKitchen()) < Constants.ORDER_TIMEKITCHEN_12) && (Integer.parseInt(orders.get(position).getTimeKitchen()) >= Constants.ORDER_TIMEKITCHEN_5)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                } else {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                }
                if (orders.get(position).getTimeKitchen() != null) {
                    holder.hourKitchen.setText(orders.get(position).getTimeKitchen() + " min");
                }
                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_HIGHT) ||((orders.get(position).getMinutesDriverOnRoad()) >= Integer.valueOf(timeMax)) ) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                } else if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_MEDIUM)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                } else if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_LOW)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                } else {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                }

                holder.hourKitchen.setText(orders.get(position).getMinutesDriverInRestaurant() + " min");

                break;
            case Constants.ORDER_STATUS_driver_on_road:
                if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_HIGHT) ||((orders.get(position).getMinutesDriverOnRoad()) >= Integer.valueOf(timeMax)) ) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                } else if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_MEDIUM)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                } else if (orders.get(position).getRestaurant_foodpriority().equals(Constants.ORDER_FOODPRIORITY_LOW)) {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                } else {
                    holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                    holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                    holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                }
                if (orders.get(position).getTimeKitchen() != null) {
                    holder.hourKitchen.setText(orders.get(position).getMinutesDriverOnRoad() + " min");
                }
                break;
            default:
                holder.block1.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.txtOrder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                holder.idorder.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent));
                holder.hourKitchen.setText(" -- ");
                break;

        }
        //endregion
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setData(List<Order> items) {
        this.orders = items;
        notifyDataSetChanged();
    }


    public int getOrdersCount() {
        if(countOrders!= null)
        return countOrders;
        else{return orders.size();}
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (int) v.getTag();
            Order order = orders.get(position);
            listener.onOrderClicked(v, order);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView idorder, restaurant, restaurant_name, direction_client, hourOrder, hourOrderRestAccepted, hourKitchen, state, numProblems, numOrders, state_of_payment,txtOrder;
        public ImageView imgbigorder, priority, imgClock;
        public View card;
        public RelativeLayout block1;

        public OrderViewHolder(View itemView) {
            super(itemView);

            card = itemView;
            state = (TextView) itemView.findViewById(R.id.state);
            txtOrder =  (TextView) itemView.findViewById(R.id.txtOrder);
            idorder = (TextView) itemView.findViewById(R.id.idorder);
            restaurant_name = (TextView) itemView.findViewById(R.id.restaurant_name);
            direction_client = (TextView) itemView.findViewById(R.id.direction_client);
            hourKitchen = (TextView) itemView.findViewById(R.id.hourLeft);
            card.getContext().getResources().getString(R.string.problem_status);
            imgbigorder = (ImageView) itemView.findViewById(R.id.imgBigOrder);
            imgClock = (ImageView) itemView.findViewById(R.id.imgClock);
            state_of_payment = (TextView) itemView.findViewById(R.id.state_of_payment);
            block1 = (RelativeLayout) itemView.findViewById(R.id.block1);

        }
    }

    public interface OrderListener {
        void onOrderClicked(View card, Order order);
    }

    public String getOrderStatusName(Context context, String status) {
        String ret_status = status;
        switch (ret_status) {
            case Constants.ORDER_STATUS_problem:
                ret_status = context.getResources().getString(R.string.problem_status);
                break;
            case Constants.ORDER_STATUS_driver_has_accepted:
                ret_status = context.getResources().getString(R.string.motodriver_accept_status);
                break;
            case Constants.ORDER_STATUS_rest_has_accepted:
                ret_status = context.getResources().getString(R.string.rest_has_accepted_status);
                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                ret_status = context.getResources().getString(R.string.driver_in_rest_status);
                break;
            case Constants.ORDER_STATUS_driver_on_road:
                ret_status = context.getResources().getString(R.string.driver_on_road_status);
                break;
            case Constants.ORDER_STATUS_order_delivered:
                ret_status = context.getResources().getString(R.string.order_delivered_status);
                break;
            default:
                ret_status = status;
                break;
        }

        return ret_status;

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
        notifyDataSetChanged();
    }
}