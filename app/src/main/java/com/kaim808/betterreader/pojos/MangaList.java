package com.kaim808.betterreader.pojos;

/**
 * Created by KaiM on 7/19/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MangaList {

    @SerializedName("end")
    @Expose
    private Integer end;
    @SerializedName("manga")
    @Expose
    private List<Manga> manga = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("start")
    @Expose
    private Integer start;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public List<Manga> getManga() {
        return manga;
    }

    public void setManga(List<Manga> manga) {
        this.manga = manga;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}

class Manga {

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

}
