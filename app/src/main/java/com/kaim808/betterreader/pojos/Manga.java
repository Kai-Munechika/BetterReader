package com.kaim808.betterreader.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Manga implements MangaInterface{

    /*
    manga's image ("im"),
    title ("t"),
    ID ("i"),
    alias ("a"),
    status ("s"),
    category ("c"),
    last chapter date ("ld"),
    hits ("h")

    Notable: everything except alias

    */

    @SerializedName("a")
    @Expose
    private String a;
    @SerializedName("c")
    @Expose
    private List<String> c = null;
    @SerializedName("h")
    @Expose
    private Integer h;
    @SerializedName("i")
    @Expose
    private String i;
    @SerializedName("im")
    @Expose
    private String im;
    @SerializedName("ld")
    @Expose
    private Double ld;
    @SerializedName("s")
    @Expose
    private Integer s;
    @SerializedName("t")
    @Expose
    private String t;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public List<String> getC() {
        return c;
    }

    public void setC(List<String> c) {
        this.c = c;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public Double getLd() {
        return ld;
    }

    public void setLd(Double ld) {
        this.ld = ld;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    @Override
    public List<String> getCategories() {
        return getC();
    }

    @Override
    public Integer getHits() {
        return getH();
    }

    @Override
    public String getId() {
        return getI();
    }

    @Override
    public String getImageUrl() {
        return getIm();
    }

    @Override
    public Integer getStatus() {
        return getS();
    }

    @Override
    public String getTitle() {
        return getT();
    }
}

interface MangaInterface {

    List<String> getCategories();
    Integer getHits();
    String getId();
    String getImageUrl();
    Integer getStatus();
    String getTitle();

}
