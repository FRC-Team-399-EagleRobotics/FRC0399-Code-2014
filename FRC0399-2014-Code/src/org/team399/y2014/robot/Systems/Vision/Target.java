/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems.Vision;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class Target {

    public double x = 0.0;
    public double y = 0.0;
    public double h = 0.0;
    public double w = 0.0;
    public boolean isVertical = false;

    public Target(double x, double y, double h, double w) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.isVertical = Math.abs(w) < Math.abs(h);
    }
}
