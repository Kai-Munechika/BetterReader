package com.kaim808.betterreader.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KaiM on 8/6/17.
 */
// Decorator/wrapper for the <List<List<String>> chapters we get from retrofit
public class ChapterMetaData {
    private List<List<String>> chapters;

    private static final int CHAPTER_NUMBER_INDEX = 0;
    private static final int CHAPTER_DATE_EPOCH_INDEX = 1;
    private static final int CHAPTER_TITLE_INDEX = 2;
    private static final int CHAPTER_ID_INDEX = 3;

    public ChapterMetaData(List<List<String>> chapters) {
        this.chapters = chapters;
    }

    public String getChapterNumber(int index) {
        return chapters.get(index).get(CHAPTER_NUMBER_INDEX);
    }

    public long getChapterDateInEpoch(int index) {
        return (Math.round(Float.valueOf(chapters.get(index).get(CHAPTER_DATE_EPOCH_INDEX))));
    }

    public String getChapterTitle(int index) {
        String title = chapters.get(index).get(CHAPTER_TITLE_INDEX);
        if (title == null) {
            title = "Chapter " + getChapterNumber(index);
        }
        else if (title.equals(String.valueOf(getChapterNumber(index)))) {
            title = "Chapter " + title;
        }
        return title;
    }

    public String getChapterId(int index) {
        return chapters.get(index).get(CHAPTER_ID_INDEX);
    }

    public int size() {
        return chapters.size();
    }

    public ArrayList<String> getChapterIds() {
        ArrayList<String> chapterIds = new ArrayList<>(chapters.size());

        for (int i = 0; i < chapters.size(); i++) {
            chapterIds.add(getChapterId(i));
        }
        return chapterIds;
    }

    public ArrayList<String> getChapterTitles() {
        ArrayList<String> chaptertitles = new ArrayList<>(chapters.size());

        for (int i = 0; i < chapters.size(); i++) {
            chaptertitles.add(getChapterTitle(i));
        }
        return chaptertitles;
    }


}
