package site.ownw.micro.house.model.request;

import lombok.Data;

/**
 * @author sofior
 * @date 2019/3/5 10:23
 */
@Data
public class Compute {

    /**
     * 本金
     */
    private String principal;

    /**
     * 月供
     */
    private String monthly;

    /**
     * 期数
     */
    private String periods;

    /**
     * 利率
     */
    private String rate;
}
