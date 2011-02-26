package space;

import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBounce {


    @Test
    public void straightOn() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, 0, 0, 1);
        PhysicalObject two = new PhysicalObject(1, -1, 0, 1, 0, 1);
        one.hitBy(two);
        assertEquals(1.0, one.vx, 0.0001);
        assertEquals(0.0, one.vy, 0.0001);
        assertEquals(0.0, two.vx, 0.0001);
        assertEquals(0.0, two.vy, 0.0001);
        assertTrue(-2.0 < two.x);
        assertEquals(1.0, one.x, 0.0001);
    }

    @Test
    public void straightOnVerticalDifferentMass() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, 0, -1, 0.5);
        PhysicalObject two = new PhysicalObject(2, 0, -1, 0, 1, 0.5);
        one.hitBy(two);
        assertEquals(5.0 / 3, one.vy, 0.0001);
        assertEquals(-1.0 / 3, two.vy, 0.0001);
    }

    @Test
    public void straightOnDifferentMass1() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, -1, 0, 0.5);
        PhysicalObject two = new PhysicalObject(2, -1, 0, 1, 0, 0.5);
        one.hitBy(two);
        assertEquals(5.0 / 3, one.vx, 0.0001);
        assertEquals(-1.0 / 3, two.vx, 0.0001);
    }

    @Test
    public void straightOnDifferentMass2() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, -1, 0, 0.5);
        PhysicalObject two = new PhysicalObject(2, -1, 0, 1, 0, 0.5);
        one.hitBy(two);
        assertEquals(-1.0 / 3, two.vx, 0.0001);
        assertEquals(0, two.vy, 0.0001);
    }

    @Test
    public void with90degImpactAngle() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 1, 0, 0, 1, sqrt(0.5));
        PhysicalObject two = new PhysicalObject(1, 0, 1, 1, 0, sqrt(0.5));
        one.hitBy(two);
        assertEquals(1, one.vx, 0.0001);
        assertEquals(0, one.vy, 0.0001);
        assertEquals(0, two.vx, 0.0001);
        assertEquals(1, two.vy, 0.0001);
    }

    @Test
    public void with90degImpactAngleTurned() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, 1, 1, 0.5);
        PhysicalObject two = new PhysicalObject(1, 1, 0, -1, 1, 0.5);
        one.hitBy(two);
        assertEquals(-1, one.vx, 0.0001);
        assertEquals(1, one.vy, 0.0001);
        assertEquals(1, two.vx, 0.0001);
        assertEquals(1, two.vy, 0.0001);
    }


    @Test
    public void with45degImpactAngle() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, 0, 0, 0.5);
        PhysicalObject two = new PhysicalObject(1, -1, 1, 1, 0, 0.5);
        one.hitBy(two);
        assertEquals(0.5, one.vx, 0.0001);
        assertEquals(-0.5, one.vy, 0.0001);
        assertEquals(0.5, two.vx, 0.0001);
        assertEquals(0.5, two.vy, 0.0001);
    }

    @Test
    public void with45degImpactAngleFromBelow() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 0, 0, 0, 0, 0.5);
        PhysicalObject two = new PhysicalObject(1, -1, 0, 1, 1, 0.5);
        one.hitBy(two);
        assertEquals(1, one.vx, 0.0001);
        assertEquals(0, one.vy, 0.0001);
        assertEquals(0, two.vx, 0.0001);
        assertEquals(1, two.vy, 0.0001);
    }

}
