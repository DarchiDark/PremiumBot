package org.avangard.content;

import org.avangard.profile.Group;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContentManager {
    private ContentManager() {}

    private static ContentManager instance;

    public static ContentManager getInstance() {
        if(instance != null) return instance;
        return instance = new ContentManager();
    }

    public List<ContentData> getContentForGroup(Group group) {
        List<ContentData> forGroup = new ArrayList<>();

        List<ContentData> allData = getAllContent();
        for(ContentData contentData : allData) {
            if(contentData.getManifest().getGroups().contains(group)) {
                forGroup.add(contentData);
            }
        }
        return forGroup;
    }

    public List<ContentData> getAllContent() {
        List<ContentData> contentDataList = new ArrayList<>();

        File content_dir = new File("content");

        if(!content_dir.exists() || !content_dir.isDirectory()) return new ArrayList<>();
        File[] files = content_dir.listFiles();
        if(files == null || files.length < 1) return new ArrayList<>();
        for(File file : files) {
            if(file.getName().endsWith(".manifest")) continue;
            ContentData contentData = new ContentData(file);
            contentDataList.add(contentData);
        }
        return contentDataList;
    }



}
