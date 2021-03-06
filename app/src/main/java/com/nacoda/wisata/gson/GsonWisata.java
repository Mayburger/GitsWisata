package com.nacoda.wisata.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ghifari on 9/6/17.
 */

public class GsonWisata {

    @SerializedName("status")
    private boolean status;
    @SerializedName("status_code")
    private int status_code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Data> data;

    public boolean isStatus() {
        return status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public List<Data> getData() {
        return data;
    }


    public class Data {
        @SerializedName("id_data")
        private int id_data;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("url_image")
        private String url_image;
        @SerializedName("rate")
        private float rate;
        @SerializedName("created_at")
        private String created_at;

        public int getId_data() {
            return id_data;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl_image() {
            return url_image;
        }

        public float getRate() {
            return rate;
        }

        public String getCreated_at() {
            return created_at;
        }

    }
}
