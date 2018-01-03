import stream.java8InAction.h.Point;


//@Test
public class PointTest{
    public void testMoveRightBy() throws Exception {
        Point p1 = new Point(5, 5);
        Point p2 = p1.moveRightBy(10);
        // assertEquals(15, p2.getX());
        // assertEquals(5, p2.getY());
    }
}