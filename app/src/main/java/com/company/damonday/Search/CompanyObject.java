package com.company.damonday.Search;

/**
 * Created by tom on 25/10/15.
 */
public class CompanyObject {

        private String title;
        private String thumbnailUrl;
        private String username;
        private String user_id;
        private String comment;
        private String day_before;
        private String aveage_scrore;




        public CompanyObject() {
        }



        public CompanyObject(String user_id, String thumbnailUrl, String day_before, String aveage_scrore,String username,
                             String comment,String title
        ) {
            this.title = title;
            this.user_id=user_id;
            this.thumbnailUrl = thumbnailUrl;
            this.day_before = day_before;
            this.aveage_scrore = aveage_scrore;
            this.username =username;
            this.comment = comment;
            this.title = title;
        }



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDay_before() {
            return day_before;
        }

        public void setDay_before(String day_before) {
            this.day_before = day_before;
        }

        public String getAveage_scrore() {
            return aveage_scrore;
        }

        public void setAveage_scrore(String aveage_scrore) {
            this.aveage_scrore = aveage_scrore;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }


    }

