package com.agger.springbootfunction;

import com.agger.springbootfunction.function.IHello;
import com.agger.springbootfunction.vo.Person;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

@SpringBootTest
class SpringBootFunctionApplicationTests {

    /**
     * @Title: test1
     * @Description: jdk1.8 之前使用匿名类方式来调用函数式接口
     * @author chenhx
     * @date 2019-11-27 10:33:50
     */
    @Test
    void test1() {
        IHello hello = new IHello() {
            @Override
            public void sayHello(String name) {
                System.out.println("hello "  + name);
            }
        };
        hello.sayHello("张三");
    }


    /**
     * @Title: test2
     * @Description: jdk1.8 后可以使用lambda表达式隐式的实现函数式接口
     * @author chenhx
     * @date 2019-11-27 10:47:02
     */
    @Test
    void test2() {
        IHello hello = name -> System.out.println("hello " + name);
        hello.sayHello("李四");
    }

    /**
     * @Title: test3
     * @Description: Supplier供给型函数式接口
     * @author chenhx
     * @date 2019-11-27 13:21:00
     */
    @Test
    void test3(){

        // 示例1
        int num1 = 100;
        int num2 = 200;
        // 提前定义好需要返回的指定类型结果，但不运行
        Supplier<Integer> supplier= () -> num1 + num2;
        // 调取get()方法获取一个结果
        System.out.println(supplier.get());

        // 示例2
        String str = "abcdefghijklmn";
        String s = getValue(()->str.substring(1,5));
        System.out.println(s);
    }

    public static String getValue(Supplier<String> supplier){
        return supplier.get();
    }

   /**
    * @Title: test4
    * @Description:Consumer消费型函数式接口
    * @author chenhx
    * @date 2019-11-27 13:32:18
    */
    @Test
    void test4(){
        List<Person> lisiList = new ArrayList<>();

        // 定义一个消费方法，将李四筛选出来存入lisiList
        Consumer <Person> consumer  = x -> {
            if (x.getName().equals("李四")){
                lisiList.add(x);
            }
        };

        // 整理出李四后，继续将年龄大于25的筛选出来
        consumer = consumer.andThen(x->{
            lisiList.removeIf(y->y.getAge()<25);
        });

        List<Person> list = new ArrayList<>();
        list.add(new Person(21,"张三"));
        list.add(new Person(22,"李四"));
        list.add(new Person(23,"张三"));
        list.add(new Person(16,"李四"));
        list.add(new Person(30,"王五"));
        list.add(new Person(52,"李四"));

        // 传入一个消费方法
        list.forEach(consumer);

        // 打印消费方法处理后的lisiList
        System.out.println(lisiList);

        // 示例2 使用双冒号来生成一个Consumer并执行accept()方法
        List<String> arr = Arrays.asList("a", "b", "c", "d");
        arr.forEach(SpringBootFunctionApplicationTests::printVal);
    }

    /**
     * @Title: test5
     * @Description: Predicate断言型函数式接口
     * @author chenhx
     * @date 2019-11-27 15:01:55
     */
    @Test
    public void test5(){

        // 示例1
        Predicate<Integer> predicate = (x)-> x==10;
        System.out.println(predicate.test(10));


        // 示例2
        // 1.断言 值大于20
        Predicate<Integer> predicate2 = (x)-> x>20;
        // 2.断言 并且值小于50
        predicate2 = predicate2.and(y->y<50);
        // 3.断言 或者值等于60
        predicate2 = predicate2.or(y->y==60);
        // 4.断言 逻辑取反
        predicate2 = predicate2.negate();

        List<Integer> list =  new ArrayList<>();
        list.add(9);
        list.add(12);
        list.add(21);
        list.add(60);

        // 使用lambda表达式Predicate，判断list里数是否满足条件，并删除
        list.removeIf(predicate2);
        System.out.println(list);

        // 示例3 统计集合中相等的对象的个数
        Person p = new Person(22, "李四");

        Predicate<Person> predicate3 =  Predicate.isEqual(p);

        Long count = Stream.of(
                new Person(21,"张三"),
                new Person(22,"李四"),
                new Person(23,"王五"),
                new Person(24,"王五"),
                new Person(22,"李四"),
                new Person(21,"张三")
        ).filter(predicate3).count();

        System.out.println(count);
    }

    /**
     * @Title: test6
     * @Description: Function函数型函数式接口
     * @author chenhx
     * @date 2019-11-27 16:18:50
     */
    @Test
    public void test6(){

        //示例1：定义一个funciton,实现将String转换为Integer
        Function<String,Integer> function = x->Integer.parseInt(x);
        Integer a = function.apply("100");
        System.out.println(a.getClass());

        //示例2：identity方法，返回与原参数一样的结果
        Function<String,String> funciton1 = Function.identity();
        System.out.println(funciton1.apply("aa"));

        //示例3：使用andThen() 实现一个函数 y=10x + 10;
        Function<Integer,Integer> function2 = x->10*x;
        function2 = function2.andThen(x->x+10);
        System.out.println(function2.apply(2));                 //结果：30

        //示例4：使用compose() 实现一个函数 y=(10+x)2;
        Function<Integer,Integer> function3 = x->x*2;
        function3 = function3.compose(x->x+10);
        System.out.println(function3.apply(3));                 //结果：26

        //示例5：使用使用compose()、andThen()实现一个函数 y=(10+x)2+10;
        Function<Integer,Integer> function4 = x->x*2;
        function4 = function4.compose(x->x+10);
        function4 = function4.andThen(x->x+10);
        System.out.println(function4.apply(3));                 //结果：36

    }

    public static void printVal(String str){
        System.out.println(str);
    }



}
