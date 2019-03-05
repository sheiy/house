package site.ownw.micro.house.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import site.ownw.micro.house.service.ComputeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.IsoFields;

/**
 * @author sofior
 * @date 2019/3/5 10:32
 */
@Service
public class ComputeServiceImpl implements ComputeService {

    @Override
    public Flux compute(LocalDate firstPayDay, Integer downPayment, Integer monthlyPayment, Integer payDay, Integer totalPeriods, Float operateRate, Float loanRate) {
        LocalDate now = LocalDate.now();
        LocalDate before = firstPayDay;
        BigDecimal totalPay = new BigDecimal(downPayment);
        for (int i = 0; i < totalPeriods; i++) {
            LocalDate localDate = before.plusMonths(1);
            int currentPayDay =  payDay>localDate.lengthOfMonth()?localDate.lengthOfMonth():payDay;
            LocalDate nextPayDay = localDate.withDayOfMonth(currentPayDay);
            if(nextPayDay.isBefore(now)){

            }
        }


        LocalDate temp = firstPayDay;
        int computeCount = 0;
        int between =
        do {

            temp = temp.plusMonths(1);
        } while (temp.isBefore(now) && computeCount <= totalPeriods);

        //买了多少天
        int buyDays = Period.between(buyTime, now).getDays();
        //自行运营日利率
        double dayOfOperateRate = operateRate / 365.0;
        //银行贷款日利率
        double dayOfLoanRate = loanRate / 365.0;
        //贷款日化支付资金
        double dayOfPayment = monthlyPayment * 12 / 365.0;
        //迄今为止支付的还款
        double totalMonthlyPayment = buyDays * dayOfPayment;
        //迄今为止包含首付的支出
        double totalPayment = (downPayment * 10000) + totalMonthlyPayment;

        totalPayment *
    }
}
