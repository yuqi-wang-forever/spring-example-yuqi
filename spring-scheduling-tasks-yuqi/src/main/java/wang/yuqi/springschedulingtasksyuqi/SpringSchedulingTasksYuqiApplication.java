package wang.yuqi.springschedulingtasksyuqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling // 确保创建后台任务执行程序
public class SpringSchedulingTasksYuqiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSchedulingTasksYuqiApplication.class, args);
	}

}
