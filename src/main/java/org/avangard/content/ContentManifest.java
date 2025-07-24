package org.avangard.content;

import lombok.Getter;
import lombok.SneakyThrows;
import org.avangard.profile.Group;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ContentManifest {
    private final File target;
    private final List<Group> groups;

    public ContentManifest(File target) {
        this.target = target;
        this.groups = getGroupsFromManifestFile();
    }

    @SneakyThrows
    private List<Group> getGroupsFromManifestFile() {
        List<Group> groups = new ArrayList<>();

        String filename = target.getName();
        File file = new File(filename + ".manifest");

        if(!file.exists()) throw new RuntimeException("Manifest file not exists for " + filename);

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

        for(String line : lines) {
            if(line.startsWith("Groups: ")) {
                String groups_str = line.replace("Groups: ", "");
                List<String> groups_list_as_string = Arrays.stream(groups_str.split(",")).toList();
                for(String group_str : groups_list_as_string) {
                    Group group = Group.valueOf(group_str);
                    groups.add(group);
                }
            }
        }
        return groups;
    }


}
