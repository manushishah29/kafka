package com.example.springbatchkafka.config.StepConfig;

import com.example.springbatchkafka.entity.TestEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class CustomItemReader extends JdbcCursorItemReader<TestEntity> implements ItemReader<TestEntity> {
    public CustomItemReader(@Autowired DataSource primaryDataSource) {
        setDataSource(primaryDataSource);
        setSql("SELECT * FROM test_data_example");
        setFetchSize(10000);
        setRowMapper(new EmployeeRowMapper());
    }

    public static class EmployeeRowMapper implements RowMapper<TestEntity> {

        @Override
        public TestEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//            KafkaService kafkaService = new KafkaService();
//            kafkaService.createTopic("CreditCard", 10, (short) 1);
//            kafkaService.createTopic("DebitCard", 10, (short) 1);
            TestEntity testEntity  = new TestEntity();
            testEntity.setId(rs.getInt("id"));
            testEntity.setDate(rs.getDate("joined"));
            testEntity.setName(rs.getString("firstname"));
            return testEntity;
        }

    }

    /*public static class EmployeeRowMapper implements RowMapper<TestEntity> {
        @Override
        public TestEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee  = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("firstname"));
            employee.setLastName(rs.getString("lastname"));
            employee.setDate(rs.getDate("joined"));
            employee.setEmail(rs.getString("email"));
            employee.setCompany(rs.getString("company"));
            employee.setAddress(rs.getString("address"));
            employee.setDesignation(rs.getString("designation"));
            employee.setJobType(rs.getString("jobtype"));
            employee.setDate(rs.getDate("resign"));
            return employee;
        }

    }*/
}
