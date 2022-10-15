package com.example.gsy.entity;

public class SimpleResponse {
    public int statusCode;
    public String statusMessage;

    public SimpleResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
