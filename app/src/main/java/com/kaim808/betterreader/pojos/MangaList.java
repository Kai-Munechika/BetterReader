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
    private List<Manga> mangas = null;
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

    public List<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(List<Manga> mangas) {
        this.mangas = mangas;
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

