package org.Stasy.PublicPrivacyAppBackendHeroku.FixedEndPoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {
    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @GetMapping("/")
    public String hello() {
        return "YuHOO1";
    }


}
