package com.snowalker.executor.rmqtest;

/**
 * @author snowalker
 * @date 2018/9/17
 * @desc
 */
class Demo {
    String name;
    String age;

    public Demo(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
