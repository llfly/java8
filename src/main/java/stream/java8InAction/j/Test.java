package stream.java8InAction.j;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by fangyou on 2018/1/5.
 */
public class Test {
    Person person = new Person();
    Car car = new Car();
    Insurance insurance = new Insurance();

    public void testOptional(){
        // 声明一个空的 Optional
        Optional<Car> optCar = Optional.empty();

        // 依据一个非空值创建 Optional
        // 如果 car 是一个 null ，这段代码会立即抛出一个 NullPointerException
        Optional<Car> optCar2 = Optional.of(car);

        // 可接收 null 的 Optional
        Optional<Car> optCar3 = Optional.ofNullable(car);


        optCar.get();
    }

    public void testOptionalMap(){
        // map
        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        Optional<String> name = optInsurance.map(Insurance::getName);

        Optional<Person> optPerson = Optional.of(person);
        // 嵌套式 optional 结构
//        Optional<String> name2 = optPerson.map(Person::getCar)
//                .map(Car::getInsurance)
//                .map(Insurance::getName);

        //faltMap
        String name2 = optPerson.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }

    public void optionalFunction() {
        // get 简单又最不安全 变量存在 ？ 直接返回封装值 : NoSuchElementException
        // orElse(T other) 允许在 Optional 对象不包含值时提供一个默认值
        // orElseGet(Supplier <? extends T> other) 是 orElse 方法的延迟调用版
        // Supplier 方法只有在 Optional 对象不含值时才执行调用
        // orElseThrow(Supplier <? extends X> exceptionSupplier) 和 get 方法类似，为空时抛异常
        // ifPresent(Consumer<? super T>) 变量存在时执行参数传入的方法，否则不进行任何操作
        // map flatMap filter
    }

    public Insurance findCheapestInsurance(Person person, Car car) {
        Insurance cheapestCompany = new Insurance();
        return cheapestCompany;
    }

    // 不优雅
    public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
        if (person.isPresent() && car.isPresent()) {
            return Optional.of(findCheapestInsurance(person.get(), car.get()));
        }else {
            return Optional.empty();
        }
    }

    public Optional<Insurance> nullSafeFindCheapestInsurance2(Optional<Person> person, Optional<Car> car){
        return person.flatMap(p -> car.map(c -> findCheapestInsurance(p , c)));
        // 第一个 Optional 对象调用 flatMap 方法，如果为空，lambda 不会执行，直接返回空 Optional 对象
        //
    }

    public void testOptionalFilter() {
        Optional<Insurance> optional = Optional.empty();// 返回一个空的 Optional 实例

        // 如果值存在并且满足提供的谓词，就返回包含该值的 optional 对象；否则返回一个空的 Optional 对象
        optional.filter(insurance -> "xxx".equals(insurance.getName())).ifPresent(x -> System.out.println("ok"));
    }


    public static void main(String [] args) {

    }

    // 封装到工具类中，OptionalUtility
    public static Optional<Integer> stringToInt(String s) {
        try{
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }


    public int readDuration(Properties props, String name) {
        String value = props.getProperty(name);
        if (value != null) {
            try {
                int i = Integer.parseInt(value);
                if (i > 0){
                    return i;
                }
            } catch (NumberFormatException nfe) {}
        }
        return 0;
    }

    public int readDuration2(Properties props, String name) {
        return Optional.ofNullable(props.getProperty(name))
                .flatMap(Test::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

}


class Person{
    private Optional<Car> car;
    public Optional<Car> getCar() {return car;}
}

class Car {
    private Optional<Insurance> insurance;
    public Optional<Insurance> getInsurance() { return insurance;}
}

class Insurance{
    private String name;
    public String getName() {return name;}
}

class PersonTest{
    private Car car;
    public Optional<Car> getCarAsOptional() {
        return Optional.ofNullable(car);
    }
}