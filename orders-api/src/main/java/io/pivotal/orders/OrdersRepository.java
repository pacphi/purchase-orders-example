package io.pivotal.orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class OrdersRepository {

    private static final String TABLE_NAME = "PURCHASE_ORDER";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public OrdersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TABLE_NAME);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        return jdbcTemplate.queryForObject(sql, Integer.class); 
    }

    public Order findById(UUID id) {
        Assert.notNull(id, "Cannot execute findById because id was null!");
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = ?";
        return (Order) jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<Order>());
    }

    public UUID create(Order order) {
        Assert.notNull(order, "Cannot create a new line item because order must not be null!");
        Assert.isNull(order.getId(), "Cannot execute create new line item because id must be null");
        String sql = "SELECT SYS_GUID() FROM DUAL";
        UUID newId = jdbcTemplate.queryForObject(sql, UUID.class);
        order.id(newId);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
        simpleJdbcInsert.execute(parameterSource);
        return newId;
    }

    public int delete(UUID id) {
        Assert.notNull(id, "Cannot execute delete because id was null!");
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";
        return jdbcTemplate.update(sql, new Object[] { id });
    }

    public List<Order> findByCreatedDate(LocalDateTime dateCreated) {
        Assert.notNull(dateCreated, "Cannot execute findByCreatedDate because dateCreated was null!");
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        // TODO complete implementation
        return null;
    }
}