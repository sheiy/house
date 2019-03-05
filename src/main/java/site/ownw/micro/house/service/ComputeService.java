package site.ownw.micro.house.service;

import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author sofior
 * @date 2019/3/5 10:32
 */
public interface ComputeService {

    /**
     * 计算当前日期下房子卖多少钱不亏
     *
     * @param firstPayDay 首次购买日期
     * @param downPayment 首付
     * @param monthlyPayment 月供
     * @param payDay 月供扣款日
     * @param totalPeriods 总共多少期
     * @param operateRate 运作利率
     * @param loanRate 贷款利率
     * @return 当前卖多少金额不亏
     */
    Flux compute(LocalDate firstPayDay, Integer downPayment, Integer monthlyPayment, Integer payDay, Integer totalPeriods,Float operateRate, Float loanRate);
}
