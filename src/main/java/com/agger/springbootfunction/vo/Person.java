package com.agger.springbootfunction.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: Person
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-27 14:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person implements Serializable {
    private Integer age;
    private String name;

}
