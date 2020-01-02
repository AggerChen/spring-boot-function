package com.agger.springbootfunction.vo;

import lombok.*;

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
@EqualsAndHashCode
public class Person implements Serializable {
    private Integer age;
    private String name;

}
