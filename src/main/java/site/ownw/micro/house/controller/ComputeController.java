package site.ownw.micro.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.ownw.micro.house.model.request.ComputeRequest;
import site.ownw.micro.house.service.ComputeService;

import javax.validation.Valid;

/**
 * @author sofior
 * @date 2019/3/5 10:21
 */
@RestController
@RequestMapping("/compute")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComputeController {

    private final ComputeService computeService;

    @GetMapping
    public Mono compute(@Valid ComputeRequest request) {
        return computeService.compute(request);
    }

}
