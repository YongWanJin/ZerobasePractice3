package zerobase.ShowMeTheDividend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

// # 쓰레드 풀 Thread Pool 사용을 위한 클래스
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
        // 쓰레드 풀 생성
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        // 쓰레드 풀의 사이즈 결정
        int coreNum = Runtime.getRuntime().availableProcessors();
        threadPool.setPoolSize(coreNum);
        threadPool.initialize();
        // 쓰레드풀 사용
        taskRegistrar.setTaskScheduler(threadPool);
    }
}
