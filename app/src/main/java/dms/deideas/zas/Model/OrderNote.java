package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderNote implements Serializable{


    private String id;
    private String created_at;
    private String note;
    private boolean customer_note;

    public OrderNote() {
    }
    /**
     *
     * @param id
     * @param customer_note
     * @param created_at
     * @param note
     */
    public OrderNote(String id, String created_at, String note, boolean customer_note) {
        this.id = id;
        this.created_at = created_at;
        this.note = note;
        this.customer_note = customer_note;
    }




    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return created_at;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    /**
     *
     * @return
     * The note
     */
    public String getNote() {
        return note;
    }

    /**
     *
     * @param note
     * The note
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     *
     * @return
     * The customerNote
     */
    public boolean isCustomerNote() {
        return customer_note;
    }

    /**
     *
     * @param customerNote
     * The customer_note
     */
    public void setCustomerNote(boolean customerNote) {
        this.customer_note = customerNote;
    }




}



