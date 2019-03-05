package site.ownw.micro.house.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import site.ownw.micro.house.model.request.ComputeRequest;
import site.ownw.micro.house.service.ComputeService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author sofior
 * @date 2019/3/5 10:32
 */
@Service
public class ComputeServiceImpl implements ComputeService {

    @Override
    public Flux compute(ComputeRequest request) {
        //当前日期
        LocalDate now = LocalDate.now();
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
        //首付到第一次还款日自行运营首付获得的利息
        rate = rate.add(totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between)));

        int day;
        LocalDate localDate;
        LocalDate nextPayDay;
        LocalDate upPayDay;
        for (int i = 1; i < request.getTotalPeriods(); i++) {
            //下一个还款日
            localDate = request.getFirstRepaymentDate().plusMonths(i);
            day = repaymentDay <= localDate.lengthOfMonth() ? repaymentDay : localDate.lengthOfMonth();
            nextPayDay = localDate.withDayOfMonth(day);

            //上一个还款日
            localDate = nextPayDay.minusMonths(1);
            day = repaymentDay <= localDate.lengthOfMonth() ? repaymentDay : localDate.lengthOfMonth();
            upPayDay = localDate.withDayOfMonth(day);

            if (upPayDay.isBefore(now)) {

                //两个还款日间隔天数
                between = upPayDay.until(nextPayDay, ChronoUnit.DAYS);

                //到nextPayDay时共计支出了多少钱(本金+月供)
                totalPay = totalPay.add(request.getMonthlyPayment());

                //两个还款日间隔时间自行运营情况下应获得的利息
                BigDecimal multiply = totalPay.multiply(dayOfOperateRate).multiply(BigDecimal.valueOf(between));
                rate = rate.add(multiply);
            } else {
                break;
            }
        }
        return Flux.just(rate.add(totalPay));
//
//
//        LocalDate temp = firstPayDay;
//        int computeCount = 0;
//        int between =
//        do {
//
//            temp = temp.plusMonths(1);
//        } while (temp.isBefore(now) && computeCount <= totalPeriods);
//
//        //买了多少天
//        int buyDays = Period.between(buyTime, now).getDays();
//
//
//        //贷款日化支付资金
//        double dayOfPayment = monthlyPayment * 12 / 365.0;
//        //迄今为止支付的还款
//        double totalMonthlyPayment = buyDays * dayOfPayment;
//        //迄今为止包含首付的支出
//        double totalPayment = (downPayment * 10000) + totalMonthlyPayment;
//
//        totalPayment *
    }
}
