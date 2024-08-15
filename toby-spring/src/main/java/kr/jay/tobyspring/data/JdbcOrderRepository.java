package kr.jay.tobyspring.data;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import kr.jay.tobyspring.order.Order;
import kr.jay.tobyspring.order.OrderRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * JdbcOrderRepository
 *
 * @author jaypark
 * @version 1.0.0
 * @since 8/15/24
 */
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @PostConstruct
    void initDB(){
        jdbcClient.sql("""
                create table orders (id bigint not null, no varchar(255), total numeric(38,2), primary key (id));
                alter table if exists orders drop constraint if exists UK43egxxciqr9ncgmxbdx2avi8n;
                alter table if exists orders add constraint UK43egxxciqr9ncgmxbdx2avi8n unique (no);
                create sequence orders_SEQ start with 1 increment by 50;
                """).update();
    }

    @Override
    public void save(Order order) {
        Long id = jdbcClient.sql("select next value for orders_SEQ").query(Long.class)
            .single();
        order.setId(id);

        jdbcClient.sql("insert into orders (id, no, total) values (?, ?, ?)")
            .params(id, order.getNo(), order.getTotal())
            .update();
    }
}
