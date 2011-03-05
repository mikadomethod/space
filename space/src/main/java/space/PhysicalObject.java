package space;

import java.awt.Color;
import java.awt.Graphics2D;

import static java.lang.Math.sqrt;

public class PhysicalObject {

    public double mass;
    public double x;
    public double y;
    public double vx;
    public double vy;
    public double radius;

    public PhysicalObject(double weightKilos, double x, double y, double vx,
                          double vy, double radius) {
        this.mass = weightKilos;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
    }

    public PhysicalObject absorb(PhysicalObject other) {
        double totalMass = mass + other.mass;
        x = (x * mass + other.x * other.mass) / totalMass;
        y = (y * mass + other.y * other.mass) / totalMass;
        vx = (vx * mass + other.vx * other.mass) / totalMass;
        vy = (vy * mass + other.vy * other.mass) / totalMass;
        mass = totalMass;
        return this;
    }

    public void hitBy(PhysicalObject other) {
        // find collision point by backstepping

        //backstep increment
        final double s = -Space.seconds / 10;
        //total backstep size to be found incrementally
        double dt = 0;
        //vector from this object to the other object
        double[] new12 = {x - other.x, y - other.y};
        // new distance
        double d = sqrt(new12[0] * new12[0] + new12[1] * new12[1]);
        // backstep to find collision point
        while (d < radius + other.radius) {
            dt += s;
            new12[0] = new12[0] + s * (vx - other.vx);
            new12[1] = new12[1] + s * (vy - other.vy);
            d = sqrt(new12[0] * new12[0] + new12[1] * new12[1]);
        }

        // simplify variables
        double m1 = other.mass;
        double vx1 = other.vx;
        double vy1 = other.vy;
        // point of impact for other object
        double x1 = other.x + dt * vx1;
        double y1 = other.y + dt * vy1;

        double m2 = mass;
        double vx2 = vx;
        double vy2 = vy;
        // point of impact for this object
        double x2 = x + dt * vx2;
        double y2 = y + dt * vy2;

        // direction of impact
        double[] p12 = {x2 - x1, y2 - y1};
        // normalize p12 to length 1
        double p12_abs = sqrt(p12[0] * p12[0] + p12[1] * p12[1]);
        double[] p12n = {p12[0] / p12_abs, p12[1] / p12_abs};

        // factor in calculation
        double c = p12n[0] * (vx1 - vx2) + p12n[1] * (vy1 - vy2);
        // fully elastic
        double e = 1;
        // new speeds
        double[] v1prim = {vx1 - p12n[0] * (1 + e) * (m2 * c / (m1 + m2)),
                vy1 - p12n[1] * (1 + e) * (m2 * c / (m1 + m2))};
        double[] v2prim = {vx2 + p12n[0] * (1 + e) * (m1 * c / (m1 + m2)),
                vy2 + p12n[1] * (1 + e) * (m1 * c / (m1 + m2))};

        // set variables back
        vx = v2prim[0];
        vy = v2prim[1];

        other.vx = v1prim[0];
        other.vy = v1prim[1];

        // step forward to where the objects should be
        x = x + v2prim[0] * (-dt);
        y = y + v2prim[1] * (-dt);

        other.x = other.x + v1prim[0] * (-dt);
        other.y = other.y + v1prim[1] * (-dt);

    }

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y + ",vx=" + vx + ",vy=" + vy + ",mass="
                + mass + ",radius=" + radius;
    }

    public void paintPhysicalObject(Graphics2D graphics) {
        if (!Space.IS_BOUNCING_BALLS) {
            graphics.setColor(Space.weightToColor(mass));
            int diameter = mass >= Space.EARTH_WEIGHT * 10000 ? 7 : 2;
            int xtmp = (int) ((x - Space.centrex) / Space.scale + Space.frame.getSize().width / 2);
            int ytmp = (int) ((y - Space.centrey) / Space.scale + Space.frame.getSize().height / 2);
            graphics.fillOval(
                    xtmp-diameter/2,
                    ytmp-diameter/2,
                    diameter,
                    diameter);
        } else { //BREAKOUT
            graphics.setColor(Color.WHITE);
            int xtmp = (int) ((x - Space.centrex)  + Space.frame.getSize().width / 2);
            int ytmp = (int) ((y - Space.centrey)  + Space.frame.getSize().height / 2);
            graphics.fillOval(
                    (int) (xtmp - radius ),
                    (int) (ytmp - radius ),
                    (int) (2 * radius),
                    (int) (2 * radius));
        }
    }
}
