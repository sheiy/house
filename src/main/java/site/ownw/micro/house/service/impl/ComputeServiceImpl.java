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
        BigDecimal totalRate = new BigDecimal(0);
        //自行运营日利率
        BigDecimal dayOfOperateRate = request.getOperateRate().divide(BigDecimal.valueOf(365.0), 20, RoundingMode.UP);
        //还款日
        int repaymentDay = request.getPayDay();
        //首付到第一次还款日间隔天数
        long between = request.getDownPaymentDate().until(request.getFirstRepaymentDate(), ChronoUnit.DAYS);
        //首付到第一次还款日自行运营首付获得的利息
        totalRate = totalRate.add(totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between)));
        log.info("首付{}到首次还款{}自行运营{}天获得利息:{}", request.getDownPaymentDate(), request.getFirstRepaymentDate(), between, totalRate.setScale(2, RoundingMode.UP));

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

            if (nextPayDay.isBefore(sellDate)) {
                //两个还款日间隔天数
                between = upPayDay.until(nextPayDay, ChronoUnit.DAYS);
                //到nextPayDay时共计支出了多少钱(本金+月供)
                totalPay = totalPay.add(monthlyRepayment);

                //剩余欠款
                spareAmount = AverageCapitalUtil.spareAmount(request.getTotalAmount(), monthlyRate, BigDecimal.valueOf(i), monthlyRepayment);
            } else {
                //上一个还款日到卖出日期的天数
                between = upPayDay.until(sellDate, ChronoUnit.DAYS);
            }

            //两个还款日间隔时间自行运营情况下应获得的利息
            BigDecimal rate = totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between));
            totalRate = totalRate.add(rate);
            log.info("{}到{}自行运营{}天,支出金额{},自行运营可获得利息:{},目前总获利息:{},剩余欠款:{}", upPayDay, nextPayDay, between, totalPay.setScale(2, RoundingMode.UP), rate.setScale(2, RoundingMode.UP), totalRate.setScale(2, RoundingMode.UP), spareAmount.setScale(2, RoundingMode.UP));
            if (request.getTotalPeriods().equals(i) && sellDate.isAfter(nextPayDay)) {
                between = nextPayDay.until(sellDate, ChronoUnit.DAYS);
                rate = totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between));
                totalRate = totalRate.add(rate);
                log.info("欠款还清,{}到{}自行运营{}天,支出金额{},自行运营可获得利息:{},目前总获利息:{},剩余欠款:{}", nextPayDay, sellDate, between, totalPay.setScale(2, RoundingMode.UP), rate.setScale(2, RoundingMode.UP), totalRate.setScale(2, RoundingMode.UP), spareAmount.setScale(2, RoundingMode.UP));
            } else {
                log.info("欠款未还清,剩余欠款:{}", spareAmount.setScale(2, RoundingMode.UP));
            }
        }
        BigDecimal result = spareAmount.add(totalRate.add(totalPay));
        long totalDay = request.getDownPaymentDate().until(sellDate, ChronoUnit.DAYS);
        log.info("{}天后至少卖{}忽略通货膨胀不亏", totalDay, result.setScale(2, RoundingMode.UP));
        return Mono.just(result.setScale(2, RoundingMode.UP));
    }
}
