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
        request.setTotalAmount(BigDecimal.valueOf(450000));
        request.setPayDay(1);
        request.setFirstRepaymentDate(LocalDate.of(2018, 3, 28));
        request.setDownPaymentDate(LocalDate.of(2018, 3, 1));
        request.setDownPayment(BigDecimal.valueOf(1000000));
        request.setTotalPeriods(240);
        request.setOperateRate(BigDecimal.valueOf(0.031));
        request.setLoanRate(BigDecimal.valueOf(0.049 * 1.15));
        request.setSellDate(LocalDate.of(2019 + 20, 2, 15));
        Mono<BigDecimal> compute = computeService.compute(request);
        BigDecimal block = compute.block();
        System.out.println(block);

//        EntityExchangeResult<String> result = webClient.get()
//                .uri("/compute?" +
//                        "totalAmount=900000&" +
//                        "payDay=31&" +
//                        "firstRepaymentDate=2019-02-28&" +
//                        "downPaymentDate=2019-02-15&" +
//                        "downPayment=400000&" +
//                        "totalPeriods=360&" +
//                        "operateRate=0.031&" +
//                        "loanRate=0.052")
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class)
//                .returnResult();
//        System.out.println(result.getResponseBody());
    }

}
