package com.example.ecormmerce;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterResponse {
    String status,success;

    List<messageData> message;

    public List<messageData> getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setMessage(List<messageData> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

class messageData{
    @SerializedName("non_field_errors")
    String non_field_errors;

    public String getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}


