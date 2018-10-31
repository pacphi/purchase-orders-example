package io.pivotal.orders;

import java.util.List;
import java.util.UUID;

import com.fasterxml.uuid.Generators;

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
public class LineItemsRepository {

    private static final String TABLE_NAME = "PURCHASE_ORDER_LINE_ITEM";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public LineItemsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TABLE_NAME);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        return jdbcTemplate.queryForObject(sql, Integer.class); 
    }

    public LineItem findById(UUID id) {
        Assert.notNull(id, "Cannot execute findById because id was null!");
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { id.toString() }, new BeanPropertyRowMapper<LineItem>(LineItem.class));
    }

    public UUID create(LineItem lineItem) {
        Assert.notNull(lineItem, "Cannot create a new line item because lineItem must not be null!");
        Assert.isTrue(lineItem.getId() == null && lineItem.getOrderId() != null, "Cannot create a new line item because orderId must not be null and id must be null");
        UUID newId = Generators.timeBasedGenerator().generate();
        lineItem.id(newId);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(lineItem);
        simpleJdbcInsert.execute(parameterSource);
        return newId;
    }

    public int delete(UUID id) {
        Assert.notNull(id, "Cannot execute delete because id was null!");
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";
        return jdbcTemplate.update(sql, new Object[] { id.toString() });
    }

    public List<LineItem> findByOrderId(UUID orderId) {
        Assert.notNull(orderId, "Cannot execute findByOrderId because orderId was null!");
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        // TODO complete implementation
        return null;
    }
}