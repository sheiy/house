package site.ownw.micro.house;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import site.ownw.micro.house.model.request.ComputeRequest;
import site.ownw.micro.house.service.ComputeService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComputeTests {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ComputeService computeService;


    @Test
    public void compute() throws Exception {
        ComputeRequest request = new ComputeRequest();
        request.setTotalAmount(BigDecimal.valueOf(900000));
        request.setPayDay(31);
        request.setFirstRepaymentDate(LocalDate.of(2019, 2, 28));
        request.setDownPaymentDate(LocalDate.of(2019, 2, 15));
        request.setDownPayment(BigDecimal.valueOf(450000));
        request.setTotalPeriods(360);
        request.setOperateRate(BigDecimal.valueOf(0.03));
        request.setLoanRate(BigDecimal.valueOf(0.049 * 1.15));
        request.setSellDate(LocalDate.of(2019 + 30, 2, 15));
        Mono<BigDecimal> compute = computeService.compute(request);
        BigDecimal block = compute.block();
        System.out.println(block);
    }

}
