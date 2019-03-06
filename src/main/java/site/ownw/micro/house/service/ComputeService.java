package site.ownw.micro.house.service;

import reactor.core.publisher.Mono;
import site.ownw.micro.house.model.request.ComputeRequest;

import java.math.BigDecimal;

/**
 * @author sofior
 * @date 2019/3/5 10:32
 */
public interface ComputeService {

    /**
     * 计算当前日期下房子卖多少钱不亏
     *
     * @param request 请求对象
     * @return 当前卖多少钱不亏
     */
    Mono<BigDecimal> compute(ComputeRequest request);
}
