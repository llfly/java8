package stream.java8InAction.h;

/**
 * Created by fangyou on 2018/1/3.
 */
public class ProductFactory {
    public static Product createProduct(String name) {
        switch (name){
            case "loan": return new Loan();
            case "stock": return new Stock();
            default: throw new RuntimeException("No such product" + name);
        }
    }
}


class Product{

}

class Loan extends Product{

}

class Stock extends Product{

}