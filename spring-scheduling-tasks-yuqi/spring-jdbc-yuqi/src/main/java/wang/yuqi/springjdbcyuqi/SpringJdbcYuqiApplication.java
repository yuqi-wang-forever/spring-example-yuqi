package wang.yuqi.springjdbcyuqi;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import wang.yuqi.springjdbcyuqi.bean.Customer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringJdbcYuqiApplication implements CommandLineRunner {

	private final static Logger log = LoggerFactory.getLogger(SpringJdbcYuqiApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringJdbcYuqiApplication.class, args);
	}


	private final JdbcTemplate jdbcTemplate;
	public SpringJdbcYuqiApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * @description 将在加载应用程序上下文后执行该 run() 方法。
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		log.info("SpringJdbcYuqiApplication start");
		log.info("create tables");
		jdbcTemplate.execute("Drop table t_customer if exists ");
		//jdbcTemplate.execute("create table t_customer (\" id SERIAL NOT NULL PRIMARY KEY, firstname VARCHAR(50) NOT NULL,firstname VARCHAR(50) NOT NULL \" )");
		jdbcTemplate.execute("CREATE TABLE T_CUSTOMER(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
		// 数据
		List<Object[]> names = Stream.of("Jack ONeill","Samantha Carter","Daniel Jackson").map(e -> e.split(" ")).collect(Collectors.toList());
		// 遍历打印添加的数据
		names.forEach(e -> {
			log.info(String.format("insert into t_customer values(%s,%s)",e[0],e[1]));
		});
		log.info("names的属性{}",names.getClass().getTypeName());
		jdbcTemplate.batchUpdate("insert into t_customer(first_name,last_name) values(?,?)", names);

		log.info("查询Jack");
		jdbcTemplate.query("select * from t_customer where first_name = ?", (result,rowNum) ->
				new Customer(result.getLong("id"),
               result.getString("first_name"),
               result.getString("last_name")),
						"Jack") //查询参数
				.forEach(customer -> log.info(customer.toString()));
	}
}
