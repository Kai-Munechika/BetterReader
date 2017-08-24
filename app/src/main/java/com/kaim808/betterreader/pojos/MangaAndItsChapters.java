package com.kaim808.betterreader.pojos;

/**
 * Created by KaiM on 7/19/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaim808.betterreader.utils.StringParser;

import java.util.List;

public class MangaAndItsChapters {

    /* Notable values
    Artist
    Author
    Categories
    chapters_len = total number of chapters in here
    hits
    image
    statusw

     */

    @SerializedName("aka")
    @Expose
    private List<String> aka = null;
    @SerializedName("aka-alias")
    @Expose
    private List<String> akaAlias = null;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("artist_kw")
    @Expose
    private List<String> artistKw = null;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("author_kw")
    @Expose
    private List<String> authorKw = null;
    @SerializedName("autoManga")
    @Expose
    private Boolean autoManga;
    @SerializedName("baka")
    @Expose
    private Boolean baka;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("chapters")
    @Expose
    private List<List<String>> chapters = null;
    @SerializedName("chapters_len")
    @Expose
    private Integer chaptersLen;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("hits")
    @Expose
    private Integer hits;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("last_chapter_date")
    @Expose
    private Integer lastChapterDate;
    @SerializedName("released")
    @Expose
    private Integer released;
    @SerializedName("startsWith")
    @Expose
    private String startsWith;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_kw")
    @Expose
    private List<String> titleKw = null;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("updatedKeywords")
    @Expose
    private Boolean updatedKeywords;
    @SerializedName("url")
    @Expose
    private String url;

    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public List<String> getAkaAlias() {
        return akaAlias;
    }

    public void setAkaAlias(List<String> akaAlias) {
        this.akaAlias = akaAlias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getArtistKw() {
        return artistKw;
    }

    public void setArtistKw(List<String> artistKw) {
        this.artistKw = artistKw;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getAuthorKw() {
        return authorKw;
    }

    public void setAuthorKw(List<String> authorKw) {
        this.authorKw = authorKw;
    }

    public Boolean getAutoManga() {
        return autoManga;
    }

    public void setAutoManga(Boolean autoManga) {
        this.autoManga = autoManga;
    }

    public Boolean getBaka() {
        return baka;
    }

    public void setBaka(Boolean baka) {
        this.baka = baka;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<List<String>> getChapters() {
        return chapters;
    }

    public void setChapters(List<List<String>> chapters) {
        this.chapters = chapters;
    }

    public Integer getChaptersLen() {
        return chaptersLen;
    }

    public void setChaptersLen(Integer chaptersLen) {
        this.chaptersLen = chaptersLen;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getDescription() {
        return StringParser.htmlStringToString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getLastChapterDate() {
        return lastChapterDate;
    }

    public void setLastChapterDate(Integer lastChapterDate) {
        this.lastChapterDate = lastChapterDate;
    }

    public Integer getReleased() {
        return released;
    }

    public void setReleased(Integer released) {
        this.released = released;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(String startsWith) {
        this.startsWith = startsWith;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTitleKw() {
        return titleKw;
    }

    public void setTitleKw(List<String> titleKw) {
        this.titleKw = titleKw;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getUpdatedKeywords() {
        return updatedKeywords;
    }

    public void setUpdatedKeywords(Boolean updatedKeywords) {
        this.updatedKeywords = updatedKeywords;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}