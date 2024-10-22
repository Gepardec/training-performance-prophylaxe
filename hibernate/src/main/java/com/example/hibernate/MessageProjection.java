package com.example.hibernate;

public class MessageProjection {

    private final String text;
    private final String recipient;

    public MessageProjection(String text, String recipient) {
        this.text = text;
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public String getRecipient() {
        return recipient;
    }

}
