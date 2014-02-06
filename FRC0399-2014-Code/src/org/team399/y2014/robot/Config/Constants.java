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

    public static class Shooter {

        public static final double STOW_POS = 0.0;  // Stow position value
        public static final double STOW_P = 0.8;    // Stow PID gains
        public static final double STOW_I = 0.0;
        public static final double STOW_D = 0.0;
        public static final double STOW_F = 0.0;    // Stow Feed Forward Gain
        public static final double STOW_S = 0.5;    // Stow Speed limit

        public static final double PASS_POS = 0.0;  // Pass position
        public static final double PASS_P = 0.8;    // Pass PID gains
        public static final double PASS_I = 0.0;
        public static final double PASS_D = 0.0;
        public static final double PASS_F = 0.0;    // Pass Feed Forward Gain
        public static final double PASS_S = 0.4;    // Pass Speed limit
    }
}
