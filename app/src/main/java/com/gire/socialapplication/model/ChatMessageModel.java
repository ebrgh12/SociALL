package com.gire.socialapplication.model;

/**
 * Created by girish on 7/22/2017.
 */

public class ChatMessageModel {
    Integer flagType;
    String message;

    public ChatMessageModel(Integer flagType, String message) {
        this.flagType = flagType;
        this.message = message;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
