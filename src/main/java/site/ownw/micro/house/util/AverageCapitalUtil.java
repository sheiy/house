package site.ownw.micro.house.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AverageCapitalUtil {

    /**
     * 月还款额
     *
     * @param A 贷款总额度
     * @param β 贷款月利率
     * @param m 总期数
     * @return 月还款额
     */
    public static BigDecimal monthlyRepayment(BigDecimal A, BigDecimal β, BigDecimal m) {
        BigDecimal onePlusMonthlyRate = β.add(BigDecimal.ONE).pow(m.intValue());
        BigDecimal numerator = β.multiply(onePlusMonthlyRate);
        BigDecimal denominator = onePlusMonthlyRate.subtract(BigDecimal.ONE);
        BigDecimal result = numerator.divide(denominator, 20, RoundingMode.UP);
        return A.multiply(result);
    }

    /**
     * 剩余欠款
     *
     * @param A 贷款总额度
     * @param β 贷款月利率
     * @param n 需要计算还款第几期后剩余的额度
     * @param X 月还款额
     * @return 第n期还款后剩余的欠款
     */
    public static BigDecimal spareAmount(BigDecimal A, BigDecimal β, BigDecimal n, BigDecimal X) {
        if (n.intValue() <= 0) {
            return A;
        }
        BigDecimal onePlusMonthlyRate = β.add(BigDecimal.ONE).pow(n.intValue());
        BigDecimal numerator = X.multiply((onePlusMonthlyRate.subtract(BigDecimal.ONE)));
        return A.multiply(onePlusMonthlyRate).subtract(numerator.divide(β, 20, RoundingMode.UP));
    }

    public static void main(String[] args) {
        BigDecimal A = BigDecimal.valueOf(900000);
        BigDecimal β = BigDecimal.valueOf((0.049 * (1.15)) / 12);
        BigDecimal m = BigDecimal.valueOf(360);

        BigDecimal X = AverageCapitalUtil.monthlyRepayment(A, β, m);
        System.out.println("每月还款:" + X);
        System.out.println("剩余应还:" + AverageCapitalUtil.spareAmount(A, β, BigDecimal.valueOf(1), X));
    }
}
