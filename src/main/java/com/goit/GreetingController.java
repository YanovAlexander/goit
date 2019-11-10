package com.goit;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
public class GreetingController {

    private final TreeService treeService;

    public GreetingController(TreeService treeService) {
        this.treeService = treeService;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/root-tree")
    @SendTo("/topic/retrieveTree")
    public List<Tree> rootTree() {
        return treeService.getRootTree();
    }
}
