package com.goit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class TreeService {
    @Value("${storageDir}")
    private String rootTree;
    private static final String FILE_ICON = "jstree-file";


    public List<Tree> getRootTree() {
        return dirTree(new File(rootTree), new ArrayList<>());
    }


    private static List<Tree> dirTree(File dir, List<Tree> trees) {
        File[] subdirs = dir.listFiles();

        if (subdirs == null) {
            return new ArrayList<>();
        }

        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                Tree directory = new Tree();
                directory.setText(subdir.getName());
                directory.setChildren(dirTree(subdir, new ArrayList<>()));
                trees.add(directory);
            } else {
                Tree file = new Tree();
                file.setText(subdir.getName());
                file.setIcon(FILE_ICON);
                trees.add(file);
            }
        }
        return trees;
    }
}
