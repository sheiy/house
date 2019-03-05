package site.ownw.micro.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import site.ownw.micro.house.service.ComputeService;

import java.time.LocalDate;

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
    public Flux compute(@RequestParam LocalDate buyTime,@RequestParam  Integer downPayment,@RequestParam  Integer monthlyPayment,@RequestParam  Integer payDay,@RequestParam  Float operateRate,@RequestParam  Float loanRate) {
        return computeService.compute(buyTime, downPayment, monthlyPayment, payDay, operateRate, loanRate);
    }

}
