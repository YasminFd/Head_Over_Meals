package com.example.gui.models;

public class RecipeGrid {
    private String imageSrc;
    private String title;
    private String profileSrc;
    private String category;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private String star5;
    private int nbRatings;
    private boolean fav = false;

    private int rec_id;

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfileSrc() {
        return profileSrc;
    }

    public void setProfileSrc(String profileSrc) {
        this.profileSrc = profileSrc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStar1() {
        return star1;
    }

    public void setStar1(String star1) {
        this.star1 = star1;
    }

    public String getStar2() {
        return star2;
    }

    public void setStar2(String star2) {
        this.star2 = star2;
    }

    public String getStar3() {
        return star3;
    }

    public void setStar3(String star3) {
        this.star3 = star3;
    }

    public String getStar4() {
        return star4;
    }

    public void setStar4(String star4) {
        this.star4 = star4;
    }

    public String getStar5() {
        return star5;
    }

    public void setStar5(String star5) {
        this.star5 = star5;
    }

    public int getNbRatings() {
        return nbRatings;
    }

    public void setNbRatings(int nbRatings) {
        this.nbRatings = nbRatings;
    }

    public String isFav() {

        if(fav == true)
        {
            return "/images/full-heart.png";
        }
        return "/images/heart.png";
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
