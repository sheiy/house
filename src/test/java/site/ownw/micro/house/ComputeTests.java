package site.ownw.micro.house;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComputeTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void compute() throws Exception {
        webClient.get()
                .uri("/compute?payDay=31&firstRepaymentDate=2019-02-28&downPaymentDate=2019-02-15&downPayment=400000&monthlyPayment=5200&payDay=31&totalPeriods=360&operateRate=0.031&loanRate=0.052")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("test");
    }

}
