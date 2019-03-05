package site.ownw.micro.house.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class AverageCapitalPlusInterestUtils {

    /**
     * 等额本息计算获取还款方式为等额本息的每月偿还本金和利息
     * <p>
     * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 每月偿还本金和利息, 不四舍五入，直接截取小数点最后两位
     */
    public static double getPerMonthPrincipalInterest(double invest, double yearRate, int totalMonth) {
        BigDecimal monthRate = BigDecimal.valueOf(yearRate).divide(BigDecimal.valueOf(12),20, RoundingMode.UP);

        BigDecimal monthIncome = new BigDecimal(invest)
                .multiply(monthRate.add(BigDecimal.ONE).pow(totalMonth))
                .divide(monthRate.add(BigDecimal.ONE).pow(totalMonth).subtract(BigDecimal.ONE), 20, RoundingMode.UP);
        return monthIncome.doubleValue();
    }

    /**
     * 等额本息计算获取还款方式为等额本息的每月偿还利息
     * <p>
     * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 每月偿还利息
     */
    public static Map<Integer, BigDecimal> getPerMonthInterest(double invest, double yearRate, int totalMonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        double monthRate = yearRate / 12;
        BigDecimal monthInterest;
        for (int i = 1; i < totalMonth + 1; i++) {
            BigDecimal multiply = new BigDecimal(invest).multiply(new BigDecimal(monthRate));
            BigDecimal sub = new BigDecimal(Math.pow(1 + monthRate, totalMonth)).subtract(new BigDecimal(Math.pow(1 + monthRate, i - 1)));
            monthInterest = multiply.multiply(sub).divide(new BigDecimal(Math.pow(1 + monthRate, totalMonth) - 1), 6, BigDecimal.ROUND_DOWN);
            monthInterest = monthInterest.setScale(2, BigDecimal.ROUND_DOWN);
            map.put(i, monthInterest);
        }
        return map;
    }

    /**
     * 等额本息计算获取还款方式为等额本息的每月偿还本金
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 每月偿还本金
     */
    public static Map<Integer, BigDecimal> getPerMonthPrincipal(double invest, double yearRate, int totalMonth) {
        double monthRate = yearRate / 12;
        BigDecimal monthIncome = new BigDecimal(invest)
                .multiply(new BigDecimal(monthRate * Math.pow(1 + monthRate, totalMonth)))
                .divide(new BigDecimal(Math.pow(1 + monthRate, totalMonth) - 1), 20, BigDecimal.ROUND_DOWN);
        Map<Integer, BigDecimal> mapInterest = getPerMonthInterest(invest, yearRate, totalMonth);
        Map<Integer, BigDecimal> mapPrincipal = new HashMap<Integer, BigDecimal>();

        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            mapPrincipal.put(entry.getKey(), monthIncome.subtract(entry.getValue()));
        }
        return mapPrincipal;
    }

    /**
     * 等额本息计算获取还款方式为等额本息的总利息
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 总利息
     */
    public static double getInterestCount(double invest, double yearRate, int totalMonth) {
        BigDecimal count = new BigDecimal(0);
        Map<Integer, BigDecimal> mapInterest = getPerMonthInterest(invest, yearRate, totalMonth);

        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            count = count.add(entry.getValue());
        }
        return count.doubleValue();
    }

    /**
     * 应还本金总和
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 应还本金总和
     */
    public static double getPrincipalInterestCount(double invest, double yearRate, int totalMonth) {
        double monthRate = yearRate / 12;
        BigDecimal perMonthInterest = new BigDecimal(invest)
                .multiply(new BigDecimal(monthRate * Math.pow(1 + monthRate, totalMonth)))
                .divide(new BigDecimal(Math.pow(1 + monthRate, totalMonth) - 1), 20, BigDecimal.ROUND_DOWN);
        BigDecimal count = perMonthInterest.multiply(new BigDecimal(totalMonth));
        count = count.setScale(20, BigDecimal.ROUND_DOWN);
        return count.doubleValue();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        double invest = 900000; // 本金
        int month = 360;
        double yearRate = 0.49 * (1 + 0.15); // 年利率
        double perMonthPrincipalInterest = getPerMonthPrincipalInterest(invest, yearRate, month);
        System.out.println("等额本息---每月还款本息：" + perMonthPrincipalInterest);
        Map<Integer, BigDecimal> mapInterest = getPerMonthInterest(invest, yearRate, month);
        System.out.println("等额本息---每月还款利息：" + mapInterest);
        Map<Integer, BigDecimal> mapPrincipal = getPerMonthPrincipal(invest, yearRate, month);
        System.out.println("等额本息---每月还款本金：" + mapPrincipal);
        double count = getInterestCount(invest, yearRate, month);
        System.out.println("等额本息---总利息：" + count);
        double principalInterestCount = getPrincipalInterestCount(invest, yearRate, month);
        System.out.println("等额本息---应还本息总和：" + principalInterestCount);
    }
}