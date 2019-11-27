package com.agger.springbootfunction.function;

/**
 * @classname: IHello
 * @description: 函数式接口（只有一个抽象接口），使用@FunctionalInterface注解表示
 * @author chenhx
 * @date 2019-11-27 10:22:23
 */
@FunctionalInterface
public interface IHello {
    public void sayHello(String name);
}
