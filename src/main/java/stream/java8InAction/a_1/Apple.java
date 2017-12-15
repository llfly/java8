package stream.java8InAction.a_1;

/**
 * Created by fangyou on 2017/12/14.
 */
public class Apple {
    private int weight;
    private String color;

    Apple(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }

    public int sort(){
        //return
        return 1;
    }
}
