package com.company.damonday.Search;

/**
 * Created by tom on 25/10/15.
 */
public class CompanyObject {

        private String title;
        private String thumbnailUrl;
        private String user_id;
        private String category;
        private String price_range;
        private String aveage_scrore;
        private String fair;
        private String dislike;
        private String like;



        public CompanyObject() {
        }



        public CompanyObject(String user_id, String thumbnailUrl, String category, String aveage_scrore,String price_range,
                             String like,String dislike,String fair,String title
        ) {
            this.title = title;
            this.user_id=user_id;
            this.thumbnailUrl = thumbnailUrl;
            this.category = category;
            this.aveage_scrore = aveage_scrore;
            this.price_range =price_range;
            this.like = like;
            this.dislike = dislike;
            this.fair =fair;
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

        public String getPrice_range() {
            return price_range;
        }

        public void setPrice_range(String price_range) {
            this.price_range = price_range;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public String getDislike() {
        return dislike;
    }

        public void setDislike(String dislike) {
        this.dislike = dislike;
    }

        public String getFair() {
        return fair;
    }

         public void setFair(String fair) {
        this.fair = fair;
    }

    }

