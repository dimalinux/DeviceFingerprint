/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync(mode = AdviceMode.ASPECTJ)
@Import({
    PropertiesConfig.class,
    JpaConfig.class,
    MailConfig.class,
    SecurityConfig.class
})
@ComponentScan(basePackages = "to.noc.devicefp.server.service")
public class ApplicationContextConfig implements AsyncConfigurer {

    /*
     * In theory SimpleAsyncTaskExecutor should be used by default if I
     * didn't extend AsyncConfigurer and implement the method below.  In
     * practice I was getting an error saying that no executor was set
     * when Async annotated methods were invoked.
     */
    @Override
    public Executor getAsyncExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
