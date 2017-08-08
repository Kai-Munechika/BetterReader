package com.kaim808.betterreader.pojos;

import java.util.List;

/**
 * Created by KaiM on 8/6/17.
 */
// TODO: 8/6/17 save and read progress for every chapter. dank.
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
        if (title.equals(String.valueOf(getChapterNumber(index)))) {
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


}