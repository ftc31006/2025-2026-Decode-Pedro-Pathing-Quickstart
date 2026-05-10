package org.firstinspires.ftc.teamcode.geometry;

public class CartesianPoint {
    public final double X;
    public final double Y;

    public CartesianPoint(double x, double y) {
        X = x;
        Y = y;
    }

    public CartesianPoint move(double deltaX, double deltaY) {
        return new CartesianPoint(X + deltaX, Y + deltaY);
    }

    public PolarPoint toPolar() {
        double radius = Math.sqrt(X * X + Y * Y);
        double theta = Math.atan2(Y, X); // radians, range: (-π, π]
        return new PolarPoint(radius, theta);
    }
}
