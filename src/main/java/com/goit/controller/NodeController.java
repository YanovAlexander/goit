package com.goit.controller;

import com.goit.model.File;
import com.goit.model.Node;
import com.goit.service.NodeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @MessageMapping("/root-tree")
    @SendTo("/topic/retrieveTree")
    public List<Node> rootTree() {
        return nodeService.getRootTree();
    }

    @MessageMapping("/delete-node")
    public void deleteNode(String path) {
        nodeService.deleteNode(path);
    }

    @MessageMapping("/download-node")
    @SendTo("/topic/retrieveFile")
    public Node downloadFile(String path) throws IOException {
        return nodeService.download(path);
    }

    @MessageMapping("/upload-file")
    @SendTo("/topic/retrieveTree")
    public List<Node> uploadFile(File file) {
        nodeService.uploadFile(file);
        return nodeService.getRootTree();
    }
}
