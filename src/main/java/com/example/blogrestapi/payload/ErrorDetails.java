package com.example.blogrestapi.payload;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
    public ErrorDetails(String message,String details,Date timestamp){
        this.message=message;
        this.details=details;
        this.timestamp=timestamp;
    }

}
