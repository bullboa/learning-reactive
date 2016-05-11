package com.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApiController {

    @RequestMapping("/searchDuckDuckGo")
    public Response searchDuckDuckGo(@RequestParam(name = "input", defaultValue = "omega") String input) throws IOException {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage("http://duckduckgo.com/?q=" + input);
        DomElement element = page.getElementById("r1-0");
        return new Response(element.getTextContent());
    }
}
