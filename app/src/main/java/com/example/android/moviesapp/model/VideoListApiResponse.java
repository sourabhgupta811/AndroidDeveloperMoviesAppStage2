package com.example.android.moviesapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class VideoListApiResponse {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("results")
        @Expose
        private List<Video> results = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<Video> getResults() {
            return results;
        }

        public void setResults(List<Video> results) {
            this.results = results;
        }
}
