package site.ownw.micro.house.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.ownw.micro.house.model.request.ComputeRequest;
import site.ownw.micro.house.service.ComputeService;
import site.ownw.micro.house.util.AverageCapitalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author sofior
 * @date 2019/3/5 10:32
 */
@Slf4j
@Service
public class ComputeServiceImpl implements ComputeService {

    @Override
    public Mono<BigDecimal> compute(ComputeRequest request) {
        //当前日期
        LocalDate sellDate = request.getSellDate();
        if (sellDate == null) {
            sellDate = LocalDate.now();
        }
        //总支出
        BigDecimal totalPay = request.getDownPayment();
        //自行运营总利息
        BigDecimal rate = new BigDecimal(0);
        //自行运营日利率
        BigDecimal dayOfOperateRate = request.getOperateRate().divide(BigDecimal.valueOf(365.0), 20, RoundingMode.UP);
        //银行贷款日利率
        BigDecimal dayOfLoanRate = request.getLoanRate().divide(BigDecimal.valueOf(365.0), 20, RoundingMode.UP);
        //还款日
        int repaymentDay = request.getPayDay();
        //首付到第一次还款日间隔天数
        long between = request.getDownPaymentDate().until(request.getFirstRepaymentDate(), ChronoUnit.DAYS);
        //付首付到第一次还款日自行运营首付获得的利息
        rate = rate.add(totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between)));
        log.info("付首付到首次还款日自行运营首付获得的利息：{}",rate);

        int day;
        LocalDate localDate;
        LocalDate nextPayDay;
        LocalDate upPayDay;
        BigDecimal monthlyRate = request.getLoanRate().divide(BigDecimal.valueOf(12), 20, RoundingMode.UP);
        BigDecimal monthlyRepayment = AverageCapitalUtil.monthlyRepayment(request.getTotalAmount(), monthlyRate, BigDecimal.valueOf(request.getTotalPeriods()));

        BigDecimal spareAmount = BigDecimal.ZERO;
        for (int i = 1; i <= request.getTotalPeriods(); i++) {
            //下一个还款日
            localDate = request.getFirstRepaymentDate().plusMonths(i);
            day = repaymentDay <= localDate.lengthOfMonth() ? repaymentDay : localDate.lengthOfMonth();
            nextPayDay = localDate.withDayOfMonth(day);

            //上一个还款日
            localDate = nextPayDay.minusMonths(1);
            day = repaymentDay <= localDate.lengthOfMonth() ? repaymentDay : localDate.lengthOfMonth();
            upPayDay = localDate.withDayOfMonth(day);

            spareAmount = AverageCapitalUtil.spareAmount(request.getTotalAmount(), monthlyRate, BigDecimal.valueOf(i), monthlyRepayment);
            if (upPayDay.isBefore(sellDate)) {

                //两个还款日间隔天数
                between = upPayDay.until(nextPayDay, ChronoUnit.DAYS);

                //到nextPayDay时共计支出了多少钱(本金+月供)
                totalPay = totalPay.add(monthlyRepayment);

                //两个还款日间隔时间自行运营情况下应获得的利息
                BigDecimal multiply = totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between));
                log.info("{}到{}首付加利息{}自行运营获得利息:{}",upPayDay,nextPayDay,totalPay,multiply);
                log.info("第{}期还款后剩余本金:{}",i,spareAmount);
                rate = rate.add(multiply);
            } else {
                break;
            }
        }
        return Mono.just(spareAmount.add(rate.add(totalPay)));
    }
}
