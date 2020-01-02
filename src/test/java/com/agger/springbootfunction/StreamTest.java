package com.agger.springbootfunction;

import com.agger.springbootfunction.vo.Person;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: StreamTest
 * @description: TODO
 * @author: chenhx
 * @create: 2020-01-02 14:06
 **/
public class StreamTest {
   static List<Person> list = null;


    static {
        list = new ArrayList<Person>(){
            {
                this.add(new Person(12,"aaa"));
                this.add(new Person(9,"bbb"));
                this.add(new Person(32,"ccc"));
                this.add(new Person(21,"ddd"));
                this.add(new Person(9,"bbb"));
            }};
    }

    /**
     * @Title: sortTest
     * @Description: 排序测试
     * @author chenhx
     * @date 2020-01-02 14:07:48
     */
    @Test
    public void sortTest(){
        //jdk1.8以前
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge().compareTo(o2.getAge());
            }
        });
        for (Person p:list) {
            System.out.println(p);
        }

        //jdk1.8 stream方式
        list.stream().sorted(Comparator.comparing(Person::getAge)).forEach(p-> System.out.println(p));
    }

    /**
     * @Title: filterTest
     * @Description: 过滤测试、过滤年龄大于20的人
     * @author chenhx
     * @date 2020-01-02 14:22:09
     */
    @Test
    public void filterTest(){
        //jdk1.8之前
        System.out.println("jdk1.8之前");
        for (Person p:list) {
            if(p.getAge()>20){
                System.out.println(p);
            }
        }

        // jdk1.8 stream方式
        System.out.println("jdk1.8之后");
        list.stream().filter(p->p.getAge()>20).forEach(p-> System.out.println(p));
    }

    /**
     * @Title: limitTest
     * @Description: limit截断（只取前三个元素）
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void limitTest(){
        list.stream().limit(3).forEach(p-> System.out.println(p));
    }

    /**
     * @Title: skipTest
     * @Description: skip跳过 （跳过前三个元素）
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void skipTest(){
        // 跳过前三个元素遍历 （skip与limit相对）
        list.stream().skip(3).forEach(p-> System.out.println(p));
    }

    /**
     * @Title: distinctTest
     * @Description: distinct去重
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void distinctTest(){
        // 去除重复对象（对象必须重写hashCode()和equals()方法）
        list.stream().distinct().forEach(p-> System.out.println(p));
    }

    /**
     * @Title: map
     * @Description: map 映射
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void mapTest(){
        // map需要传入一个方法，这个方法会映射集合中每个元素
        list.stream().map(p->p.getAge()).collect(Collectors.toList()).forEach(age-> System.out.println(age));

        // 全部转大写
        List<String> strList = Arrays.asList("aa","bbb","aDfd");
        strList.stream().map(str->str.toUpperCase()).forEach(str-> System.out.println(str));
    }

    /**
     * @Title: findFirst
     * @Description: findFirst 找到第一个元素
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void findFirstTest(){
        Person person = list.stream().findFirst().get();
        System.out.println(person);
    }

    /**
     * @Title: reduce
     * @Description: reduce 多面操作
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void reduceTest(){
        // sum()、max()、min()、count()都是reduce操作
        // 使用reduce找出年龄最大的用户
        Optional<Person> optional = list.stream().reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2);
        Person person = optional.get();
        System.out.println(person);

        // 使用max方法
        Optional<Person> max = list.stream().max(Comparator.comparing(Person::getAge));
        System.out.println(max);
    }

    /**
     * @Title: collect
     * @Description: collect 对字符串做join
     * @author chenhx
     * @date 2020-01-02 14:28:29
     */
    @Test
    public void collectTest(){

        Stream<String> stream = Stream.of("aa", "bb", "cc");
        String collect = stream.collect(Collectors.joining());
        System.out.println(collect);

        // 必须再给stream赋值，因为已经被用过已经被注销
        stream = Stream.of("aa", "bb", "cc");
        String collect2 = stream.collect(Collectors.joining("-"));
        System.out.println(collect2);

        // 必须再给stream赋值，因为已经被用过已经被注销
        stream = Stream.of("aa", "bb", "cc");
        String collect3 = stream.collect(Collectors.joining("-","(",")"));
        System.out.println(collect3);

    }


}
