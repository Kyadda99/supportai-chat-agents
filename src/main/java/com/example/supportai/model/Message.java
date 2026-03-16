package com.example.supportai.model;

public class Message {
    public String role;
    public String content;

    public Message(String content, String role) {
        this.content = content;
        this.role = role;
    }


}
