package com.goit.service;

import com.goit.model.Node;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class NodeService {
    @Value("${storageDir}")
    private String rootTree;
    private static final String FILE_ICON = "jstree-file";
    private Logger LOG = LoggerFactory.getLogger(NodeService.class);

    public List<Node> getRootTree() {
        return dirTree(new File(rootTree), new ArrayList<>());
    }

    public void deleteNode(String path) {
        File file = new File(rootTree + path);
        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else if (file.isFile()) {
                Files.deleteIfExists(file.toPath());
            }
        } catch (IOException e) {
            LOG.error("Can't delete a file, filename = " + file.getName(), e);
        }
    }

    public Node download(String path) throws IOException {
        File file = new File(rootTree + path);
        if (file.exists() && file.isFile()) {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return new Node(null, file.getName(), null, null, bytes);
        }
        return null;
    }

    @PostConstruct
    private void verifyBackSlash() {
        if (!rootTree.endsWith("/")) {
            rootTree = rootTree + "/";
        }
    }

    private static List<Node> dirTree(File dir, List<Node> nodes) {
        File[] subdirs = dir.listFiles();
        if (subdirs == null) {
            return new ArrayList<>();
        }
        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                Node directory = new Node();
                directory.setText(subdir.getName());
                directory.setChildren(dirTree(subdir, new ArrayList<>()));
                nodes.add(directory);
            } else if (subdir.isFile()) {
                Node file = new Node();
                file.setText(subdir.getName());
                file.setIcon(FILE_ICON);
                nodes.add(file);
            }
        }
        return nodes;
    }

    public void uploadFile(com.goit.model.File file) {
        String path = rootTree + file.getParentPath() + "/" + file.getFileName();
        try (OutputStream out = new FileOutputStream(path)) {
            out.write(Base64.getDecoder().decode(file.getBase64()));
        } catch (IOException e) {
            LOG.error("Cant create file, path = " + path, e);
        }
    }
}
