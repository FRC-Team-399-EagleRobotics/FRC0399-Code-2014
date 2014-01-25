/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Config;

/**
 *
 * @author Jeremy
 */
public class Constants {

    //JOYSTICK USB CONTROLLER PORTS
    public final static int DRIVER_LEFT_JOYSTICK_USB = 1;
    public final static int DRIVER_RIGHT_JOYSTICK_USB = 2;
    public final static int OPERATOR_GAMEPAD_USB = 3;

    //TURN PID VALUES
    public static double TURN_P = -10.0;
    public static double TURN_I = 0.0;
    public static double TURN_D = 1.0;
    //DRIVE PID VALUES
    public static double DRIVE_P = -.01;
    public static double DRIVE_I = 0;
    public static double DRIVE_D = 0;

    public static final double TICKS_TO_INCHES = 16.5694;
    public static final double WIDTH = 26.0;
}
