package org.firstinspires.ftc.teamcode.geometry;

public class CartesianPoint {
    private final double x;
    private final double y;

    public CartesianPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public CartesianPoint move(double deltaX, double deltaY) {
        return new CartesianPoint(x + deltaX, y + deltaY);
    }

    public PolarPoint toPolar() {
        double radius = Math.sqrt(x * x + y * y);
        double theta = Math.atan2(y, x); // radians, range: (-π, π]
        return new PolarPoint(radius, theta);
    }
}
