package zhu.liang.common.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import zhu.liang.common.constants.RedisPropertiesConstants;

@Configuration
@ComponentScan("zhu.liang.common")
@EnableMBeanExport(
        registration = RegistrationPolicy.IGNORE_EXISTING
)
@Import({RedisPropertiesConstants.class})
public class CommonAutoConfiguration {

}
