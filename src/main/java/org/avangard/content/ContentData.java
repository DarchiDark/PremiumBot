package org.avangard.content;

import lombok.Getter;

import java.io.File;

@Getter
public class ContentData {
    private final File file;
    private final ContentManifest manifest;

    public ContentData(File file) {
        this.file = file;
        this.manifest = new ContentManifest(file);
    }
}
