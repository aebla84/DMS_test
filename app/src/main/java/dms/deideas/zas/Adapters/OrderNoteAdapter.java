package dms.deideas.zas.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.R;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderNoteAdapter extends RecyclerView.Adapter<OrderNoteAdapter.OrderNoteViewHolder> {

    private List<OrderNote> notes;

    public OrderNoteAdapter(List<OrderNote> notes) {
        this.notes = notes;
    }

    @Override
    public OrderNoteAdapter.OrderNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_note, parent, false);
        return new OrderNoteViewHolder(card);
    }

    @Override
    public void onBindViewHolder(OrderNoteAdapter.OrderNoteViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.note.setText(notes.get(position).getNote());
        holder.createdAt.setText(convertFormatDate(notes.get(position).getCreatedAt()));
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class OrderNoteViewHolder extends RecyclerView.ViewHolder {

        public TextView note, createdAt;

        public View card;

        public OrderNoteViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            createdAt = (TextView) itemView.findViewById(R.id.note_createdAt);
            note = (TextView) itemView.findViewById(R.id.note_text);
        }
    }

    public String convertFormatDate(String strDate)
    {
        String formattedDate="";
        SimpleDateFormat sourceFormat = new SimpleDateFormat(Constants.DATETIME_BBDD_FORMAT);
        SimpleDateFormat destFormat = new SimpleDateFormat(Constants.DATA_APP_FORMAT);

        if(strDate!=null && strDate!= "")
        {
            Date date = null;
            try {
                date = sourceFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDate = destFormat.format(date);
        }
        return formattedDate;
    }
}
