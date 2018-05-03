package uk.ac.tees.com2060.oreo;

public class Contract {
    String item_description, courier_alias, sender_alias, bid_message;
    double bid_amount;

    public Contract(String item_description, String courier_alias, String sender_alias,
                    String bid_message, double bid_amount) {
        this.item_description = item_description;
        this.courier_alias = courier_alias;
        this.sender_alias = sender_alias;
        this.bid_message = bid_message;
        this.bid_amount = bid_amount;
    }

    public String getItem_description() {
        return this.item_description;
    }

    public String getSender_alias() {
        return sender_alias;
    }

    public String getBid_message() {
        return bid_message;
    }

    public double getBid_amount() {
        return bid_amount;
    }

    public String getCourier_alias() {
        return courier_alias;
    }
}