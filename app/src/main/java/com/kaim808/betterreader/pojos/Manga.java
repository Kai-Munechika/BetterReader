package com.kaim808.betterreader.pojos;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.utils.StringParser;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Manga extends SugarRecord<Manga> implements MangaInterface {

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
    @Ignore public static final String IMAGE_URL = "im";
    @Ignore public static final String TITLE = "t";
    @Ignore public static final String ID = "i";
    @Ignore public static final String ALIAS = "a";
    @Ignore public static final String STATUS = "s";
    @Ignore public static final String LAST_CHAPTER_DATE = "ld";
    @Ignore public static final String HITS = "h";
    @Ignore public static final String CATEGORIES_AS_STRING = "categories_as_string";
    @Ignore public static final String FAVORITED = "favorited";


    @SerializedName("a")
    @Expose
    private String a;

    @Ignore
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

    private String categoriesAsString;
    private boolean favorited = false;

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public Manga() {
    }

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
        return StringParser.htmlStringToString(t);
    }

    public void setT(String t) {
        this.t = t;
    }

    @Override
    public Integer getHits() {
        return getH();
    }

    @Override
    public String getMangaId() {
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
        return StringParser.URLStringToString(StringParser.htmlStringToString(getT()));
    }

    @Override
    public Double getLastChapterDate() {
        return getLd();
    }

    public void setCategoriesAsString(String s) {
        this.categoriesAsString = s;
    }

    @Override
    public String getCategoriesAsString() {
        return categoriesAsString;
    }

    public String categoriesToString() {
        if (getC() != null && getC().size() != 0) {
            return StringUtils.join(getC(), ", ");
        } return categoriesAsString;
    }

    // these 2 methods should be in our pojo
    public String getFormattedStatus(AppCompatActivity appCompatActivity) {
        return this.getStatus() == 1 ? appCompatActivity.getString(R.string.ongoing) : appCompatActivity.getString(R.string.completed);
    }

    public String getFormattedNumViews() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.getHits()) + " views";
    }

    @Override
    public List<String> getCategoriesAsList() {
        // this.c would be loaded this object were instantiated from our api call
        if (this.c != null && this.c.size() != 0) {
            return this.c;
        }
        // categories as string is saved in our sqlite db
        return this.categoriesAsString == null ? null : Arrays.asList(this.categoriesAsString.split(", "));
    }
}

interface MangaInterface {

    String getCategoriesAsString();
    List<String> getCategoriesAsList();
    Integer getHits();
    String getMangaId();
    String getImageUrl();
    Integer getStatus();
    String getTitle();
    Double getLastChapterDate();


}
