package xyz.game.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import xyz.game.task.PVUVTask;

@Configuration
public class QuartzConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobDetail pvuvJobDetail() {
        return JobBuilder.newJob(PVUVTask.class)
                .withIdentity("pvuvTask", "statisticsGroup")
                .storeDurably()
                .build();
    }
    
    @Bean
    public Trigger pvuvTrigger(JobDetail pvuvJobDetail) {
        return TriggerBuilder.newTrigger()
                .withIdentity("pvuvTrigger", "statisticsGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .forJob(pvuvJobDetail)
                .build();
    }
    
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger pvuvTrigger, JobDetail pvuvJobDetail) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        
        // 设置自定义的JobFactory
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setTriggers(pvuvTrigger);
        schedulerFactoryBean.setJobDetails(pvuvJobDetail);
        return schedulerFactoryBean;
    }
}