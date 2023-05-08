package com.example.springbatchkafka.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "test_data_example")
@Entity
public class TestEntity implements Serializable {

    @Id
    private Integer id;
    @Column(name = "firstname")
    private String name;
    @Column(name = "joined")
    private Date date;
}
