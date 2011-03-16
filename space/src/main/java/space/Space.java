package space;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Space extends JFrame implements MouseWheelListener,
        MouseMotionListener, KeyListener {
    public static final double EARTH_WEIGHT = 5.9736e24;
    private static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    static boolean IS_BOUNCING_BALLS = false;
    static boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true


    private static final long serialVersionUID = 1532817796535372081L;

    private static final double G = 6.67428e-11; // m3/kgs2
    public static double seconds = 1;
    private static List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    static double centrex = 0.0;
    static double centrey = 0.0;
    static double scale = 10;
    private static boolean showWake = false;
    private static int step = 0;
    private static int nrOfObjects = 75;
    private static int frameRate = 25;

    static JFrame frame;

    public Space() {
        setBackground(Color.BLACK);
        Space.frame = this;
    }

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();

            if (!showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : objects) {
                po.paintPhysicalObject(graphics);
                String string = "Objects:" + objects.size() + " scale:" + scale + " steps:" + step + " frame rate: " + frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }

    }

    public static Color weightToColor(double weight) {
        if (weight < 1e10) return Color.GREEN;
        if (weight < 1e12) return Color.CYAN;
        if (weight < 1e14) return Color.MAGENTA;
        if (weight < 1e16) return Color.BLUE;
        if (weight < 1e18) return Color.GRAY;
        if (weight < 1e20) return Color.RED;
        if (weight < 1e22) return Color.ORANGE;
        if (weight < 1e25) return Color.PINK;
        if (weight < 1e28) return Color.YELLOW;
        return Color.WHITE;
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        final Space space = new Space();
        space.addMouseWheelListener(space);
        space.addMouseMotionListener(space);
        space.addKeyListener(space);
        space.setSize(800, 820);

        if (!IS_BOUNCING_BALLS) {
            space.setStepSize(3600 * 24 * 7);

            double outerLimit = ASTRONOMICAL_UNIT * 20;

            for (int i = 0; i < nrOfObjects; i++) {
                double angle = randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
                double weightKilos = 1e3 * EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                add(weightKilos, x, y, vx, vy, 1);
            }

            scale = outerLimit / space.getWidth();

            add(EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
        } else {
            nrOfObjects = 50;
            space.setStepSize(1); // One second per iteration
            for (int i = 0; i < nrOfObjects; i++) {
                // radius,weight in [1,20]
                double radiusAndWeight = 1 + 19 * Math.random();
                //x,y in [max radius, width or height - max radius]
                Space.add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
            }
            scale = 1;
            centrex = 400;
            centrey = 390; //Must compensate for title bar
        }
        space.setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    space.collide();
                    space.step();
                }
            });
            try {
                long ahead = 1000 / frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(frameRate<25) frameRate++;
                } else {
                    Thread.sleep(50);
                    frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static double randSquare() {
        double random = Math.random();
        return random * random;
    }

    public void setStepSize(double seconds) {
        Space.seconds = seconds;
    }

    public static PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    public void step() {
        if (!IS_BOUNCING_BALLS) {
            for (PhysicalObject aff : objects) {
                double fx = 0;
                double fy = 0;
                for (PhysicalObject oth : objects) {
                    if (aff == oth)
                        continue;
                    double[] d = new double[]{aff.x - oth.x, aff.y - oth.y};
                    double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                    double f = G * aff.mass * oth.mass / r2;
                    double sqrtOfR2 = Math.sqrt(r2);
                    fx += f * d[0] / sqrtOfR2;
                    fy += f * d[1] / sqrtOfR2;
                }
                double ax = fx / aff.mass;
                double ay = fy / aff.mass;
                aff.x = aff.x - ax * Math.pow(seconds, 2) / 2 + aff.vx * seconds;
                aff.y = aff.y - ay * Math.pow(seconds, 2) / 2 + aff.vy * seconds;
                aff.vx = aff.vx - ax * seconds;
                aff.vy = aff.vy - ay * seconds;
            }
        } else {
            for (PhysicalObject physicalObject : objects) {
                physicalObject.x = physicalObject.x + physicalObject.vx * seconds;
                physicalObject.y = physicalObject.y + physicalObject.vy * seconds;
            }

        }
        step++;
        paint(getGraphics());

    }

    private void collide() {
        List<PhysicalObject> remove = new ArrayList<PhysicalObject>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                if (!IS_BOUNCING_BALLS) {
                    if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
                        one.absorb(other);
                        remove.add(other);
                    }
                } else {
                    double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                    double collsionDistance = one.radius + other.radius;
                    if (distance < collsionDistance) {
                        one.hitBy(other);
                    }
                }
            }
            // Wall collision reverses speed in that direction
            if (IS_BOUNCING_BALLS) {
                if (one.x - one.radius < 0) {
                    one.vx = -one.vx;
                }
                if (one.x + one.radius > 800) {
                    one.vx = -one.vx;
                }
                if (one.y - one.radius < 0) {
                    one.vy = -one.vy;
                }
                if (one.y + one.radius > 800 && !IS_BREAKOUT) {
                    one.vy = -one.vy;
                } else if (one.y - one.radius > 800) {
                    remove.add(one);
                }
            }
        }
        objects.removeAll(remove);
    }


    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (!IS_BOUNCING_BALLS) {
            scale = scale + scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }

    private static Point lastDrag = null;


    public void mouseDragged(final MouseEvent e) {
        if (!IS_BOUNCING_BALLS) {
            if (lastDrag == null) {
                lastDrag = e.getPoint();
            }
            centrex = centrex - ((e.getX() - lastDrag.x) * scale);
            centrey = centrey - ((e.getY() - lastDrag.y) * scale);
            lastDrag = e.getPoint();
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }


    public void mouseMoved(MouseEvent e) {
        lastDrag = null;
    }


    public void keyPressed(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }


    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w')
            showWake = !showWake;
    }

}
