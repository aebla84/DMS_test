package dms.deideas.zas.Services;

import java.util.List;

import dms.deideas.zas.Model.OrderNote;

/**
 * Created by bnavarro on 18/07/2016.
 */
public class OrderNoteGet {
    List<OrderNote> order_notes;


    public List<OrderNote> getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(List<OrderNote> order_notes) {
        this.order_notes = order_notes;
    }
}
