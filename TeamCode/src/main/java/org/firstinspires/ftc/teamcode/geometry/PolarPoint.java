package org.firstinspires.ftc.teamcode.geometry;

public class PolarPoint {
    private final double radius;
    private final double theta;

    public PolarPoint(double radius, double theta) {
        this.radius = radius;
        this.theta = theta;
    }

    public double getRadius() {
        return radius;
    }

    public double getTheta() {
        return theta;
    }

    public PolarPoint move(double deltaRadius, double deltaTheta) {
        return new PolarPoint(radius + deltaRadius, theta + deltaTheta);
    }

    public CartesianPoint toCartesian() {
        double x = radius * Math.cos(theta);
        double y = radius * Math.sin(theta);
        return new CartesianPoint(x, y);
    }
}
