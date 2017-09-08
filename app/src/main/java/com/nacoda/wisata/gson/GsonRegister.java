package com.nacoda.wisata.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ghifari on 9/6/17.
 */

public class GsonRegister {
    @SerializedName("status")
    private boolean status;
    @SerializedName("status_code")
    private int status_code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<GsonRegister.Data> data;

    public boolean isStatus() {
        return status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public List<GsonRegister.Data> getData() {
        return data;
    }


    public class Data {
        @SerializedName("id_user")
        private String id_user;
        @SerializedName("first_name")
        private String first_name;
        @SerializedName("last_name")
        private String last_name;
        @SerializedName("username")
        private String username;
        @SerializedName("password")
        private String password;
        @SerializedName("bdate")
        private String bdate;
        @SerializedName("gender")
        private String gender;
        @SerializedName("phone")
        private String phone;


        public String getId_user() {
            return id_user;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getBdate() {
            return bdate;
        }

        public String getGender() {
            return gender;
        }

        public String getPhone() {
            return phone;
        }
    }

}
