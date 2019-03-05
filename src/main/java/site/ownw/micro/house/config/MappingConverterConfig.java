package site.ownw.micro.house.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * @author sofior
 * @date 2019/3/5 10:41
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MappingConverterConfig {

    private final GenericConversionService genericConversionService;

    @PostConstruct
    public void init() {
        genericConversionService.addConverter(String.class, LocalDate.class, LocalDate::parse);
    }


}
