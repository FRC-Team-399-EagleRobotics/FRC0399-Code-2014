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

    public static final int COMP_SWITCH = 5;
    public static final int COMP_RELAY = 1;

    public static class Shooter {

        public static final double LOWER_LIMIT = 3.15726; // the lower limits of the pot
        public static final double UPPER_LIMIT = 4.56724; // the upper limits of the pot

        public static final double STOW_POS = LOWER_LIMIT + 0.09;  // Stow position value
        public static final double STOW_P = 1.0;    // Stow PID gains
        public static final double STOW_I = 0.0;
        public static final double STOW_D = 0.0;
        public static final double STOW_F = 0.0;    // Stow Feed Forward Gain
        public static final double STOW_S = 0.65;    // Stow Speed limit

        public static final double STAGE_POS = LOWER_LIMIT + .21274;  // Pass position
        public static final double STAGE_P = 1.0;    // Pass PID gains
        public static final double STAGE_I = 0.0;
        public static final double STAGE_D = 0.0;
        public static final double STAGE_F = 0.0;    // Pass Feed Forward Gain
        public static final double STAGE_S = 0.7;    // Pass Speed limit

        public static final double TRUSS_POS = LOWER_LIMIT + 0.4611;  // Pass position
        public static final double TRUSS_P = 3.0;    // Pass PID gains
        public static final double TRUSS_I = 0.0;
        public static final double TRUSS_D = 0.0;
        public static final double TRUSS_F = 0.0;    // Pass Feed Forward Gain
        public static final double TRUSS_S = 1.0;    // Pass Speed limit

        public static final double HOLD_POS = LOWER_LIMIT + .275;  // Pass position
        public static final double HOLD_P = 1.0;    // Pass PID gains
        public static final double HOLD_I = 0.0;
        public static final double HOLD_D = 0.0;
        public static final double HOLD_F = 0.0;    // Pass Feed Forward Gain
        public static final double HOLD_S = 0.8;    // Pass Speed limit

        public static final double SHOT_POS = LOWER_LIMIT + 1.16803;  // Shot final position
        public static final double SHOT_START = LOWER_LIMIT + 0.41;// Shot start threshold position
        public static final double SHOT_INIT_SPEED = -1.0;// Shot start speed
        public static final double SHOT_FINAL_SPEED = -1.0; // Shot Final speed.
        public static final double SHOT_P = 5;    // Pass PID gains
        public static final double SHOT_I = 0.0;
        public static final double SHOT_D = 0.0;
        public static final double SHOT_F = 0.0;    // Pass Feed Forward Gain
        public static final double SHOT_S = 1.0;    // Pass Speed limit

    }

    public static class Intake {

        public static final boolean EXTENDED = false;
        public static final boolean RETRACTED = true;
        public static final double INTAKE_SPEED = 1.0;
        public static final double EXHAUST_SPEED = -1.0;
    }
}
