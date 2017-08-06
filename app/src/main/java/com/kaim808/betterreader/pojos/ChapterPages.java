package com.kaim808.betterreader.pojos;

/**
 * Created by KaiM on 7/19/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChapterPages {

    @SerializedName("images")
    @Expose
    private List<List<String>> images = null;

    public List<List<String>> getImages() {
        return images;
    }

    public void setImages(List<List<String>> images) {
        this.images = images;
    }

}