/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Config;

/**
 *
 * (jeremy.germita@gmail.com) Jeremy Germita
 */
public class Constants {

    public static final int COMP_SWITCH = 5;
    public static final int COMP_RELAY = 1;

    public static class DriveTrain {

        public static double TURN_I = 0.0;
        //TURN PID VALUES
        public static double TURN_P = -10.0;
        public static double DRIVE_D = -.005;
        public static double TURN_D = 1.0;
        public static final double WIDTH = 26.0;
        
        //DRIVE PID VALUES
        public static double DRIVE_P = .001;
        public static double DRIVE_I = .1;
        public static double STRAIGHT_P = .000;
        public static final double TICKS_TO_INCHES = 28.6624;
        public static double BRAKE_P = .01;

    }

    public static class Shooter {
        public static double SPEED_LIMIT = 1.0;
        public static double DOWN_SPEED = .4;

        public static double LOWER_LIMIT = 1.896; // the lower limits of the pot
        public static double UPPER_LIMIT = 3.105; // the upper limits of the pot
        public static double INTAKE_LIMIT = .6720;

        public static final double STOW_POS = 0.007;  // Stow position value
        public static final double STOW_P = 1.0;    // Stow PID gains
        public static final double STOW_I = 0.0;
        public static final double STOW_D = 0.0;
        public static final double STOW_F = 0.0;    // Stow Feed Forward Gain
        public static final double STOW_S = 0.4;    // Stow Speed limit

        public static final double STAGE_POS = .45;  // Pass position //short_stage .4
        public static final double STAGE_P = 1.2;    // Pass PID gains
        public static final double STAGE_I = 0.0;
        public static final double STAGE_D = 0.0;
        public static final double STAGE_F = 0.0;    // Pass Feed Forward Gain
        public static final double STAGE_S = 0.3;    // Pass Speed limit

        public static final double TRUSS_POS = 0.4021;  // Pass position
        public static final double TRUSS_P = 3.0;    // Pass PID gains
        public static final double TRUSS_I = 0.0;
        public static final double TRUSS_D = 0.0;
        public static final double TRUSS_F = 0.0;    // Pass Feed Forward Gain
        public static final double TRUSS_S = 1.0;    // Pass Speed limit
        
        public static final double HOLD_POS = .225;  // Pass position
        public static final double HOLD_P = 1.9;    // Pass PID gains
        public static final double HOLD_I = 0.0;
        public static final double HOLD_D = 0.0;
        public static final double HOLD_F = 0.0;    // Pass Feed Forward Gain
        public static final double HOLD_S = 0.8;    // Pass Speed limit

        public static final double SHOT_POS = 1.1; //1.3186;  // Shot final position
        public static final double SHOT_START = 0.0;// Shot start threshold position
        public static final double SHOT_INIT_SPEED = -1.0;// Shot start speed
        public static final double SHOT_FINAL_SPEED = -1.0; // Shot Final speed.
        public static final double SHOT_P = 5;    // Pass PID gains
        public static final double SHOT_I = 0.0;
        public static final double SHOT_D = 0.0;
        public static final double SHOT_F = 0.0;    // Pass Feed Forward Gain
        public static final double SHOT_S = 0.85;    // Pass Speed limit

        public static final double SHORT_POS = 0.80
                ;  // Shot final position
        public static final double SHORT_START = 0.0;// Shot start threshold position
        public static final double SHORT_P = 5;    // Pass PID gains
        public static final double SHORT_I = 0.0;
        public static final double SHORT_D = 0.0;
        public static final double SHORT_F = 0.0;    // Pass Feed Forward Gain
        public static final double SHORT_S = .75;    // Pass Speed limit

        public static final double SHORT_STAGE_POS = .4;  // Pass position
        public static final double SHORT_STAGE_P = 1.8;    // Pass PID gains
        public static final double SHORT_STAGE_I = 0.0;
        public static final double SHORT_STAGE_D = 0.0;
        public static final double SHORT_STAGE_F = 0.0;    // Pass Feed Forward Gain
        public static final double SHORT_STAGE_S = 0.45;    // Pass Speed limit
        
         public static final double AUTON_STAGE_POS = .475;  // Pass position
        public static final double AUTON_STAGE_P = 1.2;    // Pass PID gains
        public static final double AUTON_STAGE_I = 0.0;
        public static final double AUTON_STAGE_D = 0.0;
        public static final double AUTON_STAGE_F = 0.0;    // Pass Feed Forward Gain
        public static final double AUTON_STAGE_S = 0.3;
        
         public static final double AUTON_SHOT_POS = 1.1; //1.3186;  // Shot final position
        public static final double AUTON_SHOT_START = 0.0;// Shot start threshold position
        public static final double AUTON_SHOT_INIT_SPEED = -1.0;// Shot start speed
        public static final double AUTON_SHOT_FINAL_SPEED = -1.0; // Shot Final speed.
        public static final double AUTON_SHOT_P = 5;    // Pass PID gains
        public static final double AUTON_SHOT_I = 0.0;
        public static final double AUTON_SHOT_D = 0.0;
        public static final double AUTON_SHOT_F = 0.0;    // Pass Feed Forward Gain
        public static final double AUTON_SHOT_S = 0.85;    // Pass Speed limit
        
        

    }

    public static class Intake {

        public static final boolean EXTENDED = true;
        public static final boolean RETRACTED = false;
        public static final double INTAKE_SPEED = 1.0;
        public static final double EXHAUST_SPEED = -1.0;
    }
}
