package site.ownw.micro.house.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class IndexController {

    @RequestMapping
    public Mono index() {
        return Mono.just("OK");
    }

}
