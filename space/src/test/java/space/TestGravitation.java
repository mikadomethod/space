package space;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGravitation {

    @Test
    public void gravitationalFormulaIsCorrect() throws Exception {
        Space s = new Space();
        s.setStepSize(1);
        double earthsWeight = 5.9736e24;
        int earthsRadius = 6371000;
        PhysicalObject earth = Space.add(earthsWeight, 0, -earthsRadius, 0, 0, 1);
        PhysicalObject lump = Space.add(1, 0, 10, 0, 0, 1);
        Space.IS_BOUNCING_BALLS = false;
        s.step();
        assertEquals(10 - 9.82 / 2, lump.y, 0.02);
        assertEquals(-9.82, lump.vy, 0.02);
        assertEquals(-earthsRadius, earth.y, 0.02);
        assertEquals(0, earth.vy, 0.02);
        s.step();
        assertEquals(10 - 4 * 9.82 / 2, lump.y, 0.02);
        assertEquals(-9.82 * 2, lump.vy, 0.02);
        assertEquals(-earthsRadius, earth.y, 0.02);
        assertEquals(0, earth.vy, 0.02);
    }

    @Test
    public void mergeWithoutSpeed() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 1, 0, 0, 0, 1);
        PhysicalObject other = new PhysicalObject(1, 0, 1, 0, 0, 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.5, merge.x, 0.00001);
        assertEquals(0.5, merge.y, 0.00001);
        assertEquals(0.0, merge.vx, 0.00001);
        assertEquals(0.0, merge.vy, 0.00001);
    }

    @Test
    public void mergeWithSpeed() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 1, 0, 1, 0, 1);
        PhysicalObject other = new PhysicalObject(1, 0, 1, 0, 1, 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.5, merge.x, 0.00001);
        assertEquals(0.5, merge.y, 0.00001);
        assertEquals(0.5, merge.vx, 0.00001);
        assertEquals(0.5, merge.vy, 0.00001);
        assertEquals(2, merge.mass, 0.00001);
    }

    @Test
    public void mergeWithSpeedAndDifferentMasses() throws Exception {
        PhysicalObject one = new PhysicalObject(1, 1, 1, 1, 0, 1);
        PhysicalObject other = new PhysicalObject(4, 0, 0, 0, 1, 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.2, merge.x, 0.00001);
        assertEquals(0.2, merge.y, 0.00001);
        assertEquals(0.2, merge.vx, 0.00001);
        assertEquals(0.8, merge.vy, 0.00001);
        assertEquals(5, merge.mass, 0.00001);
    }

    @Test
    public void headsOnMergeConservesZeroSumMomentum() throws Exception {
        PhysicalObject one = new PhysicalObject(10, 0, 0, 100, 100, 1);
        PhysicalObject other = new PhysicalObject(100, 0, 0, -10, -10, 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0, merge.x, 0.00001);
        assertEquals(0, merge.y, 0.00001);
        assertEquals(0, merge.vx, 0.00001);
        assertEquals(0, merge.vy, 0.00001);
        assertEquals(110, merge.mass, 0.00001);
    }

    @Test
    public void headsOnMergeConservesMomentum() throws Exception {
        PhysicalObject one = new PhysicalObject(10, 0, 0, 10, 10, 1);
        PhysicalObject other = new PhysicalObject(100, 0, 0, 0, 0, 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0, merge.x, 0.00001);
        assertEquals(0, merge.y, 0.00001);
        assertEquals(100 / 110.0, merge.vx, 0.00001);
        assertEquals(100 / 110.0, merge.vy, 0.00001);
        assertEquals(110, merge.mass, 0.00001);
    }


}
