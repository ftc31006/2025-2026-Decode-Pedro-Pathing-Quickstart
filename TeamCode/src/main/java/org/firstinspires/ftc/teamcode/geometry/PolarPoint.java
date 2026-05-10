package org.firstinspires.ftc.teamcode.geometry;

public class PolarPoint {
    public final double Radius;
    public final double Theta;

    public PolarPoint(double radius, double theta) {
        Radius = radius;
        Theta = theta;
    }

    public PolarPoint move(double deltaRadius, double deltaTheta) {
        return new PolarPoint(Radius + deltaRadius, Theta + deltaTheta);
    }

    public CartesianPoint toCartesian() {
        double x = Radius * Math.cos(Theta);
        double y = Radius * Math.sin(Theta);
        return new CartesianPoint(x, y);
    }
}
