/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Compressor;
import org.team399.y2014.robot.Config.Ports;
import org.team399.y2014.robot.Systems.Vision.Vision;

/**
 * Main robot class. Contains singleton instances of robot mechanisms for use by
 * autonomous and teleoperated code.
 *
 * (jeremy.germita@gmail.com) Jeremy Germita
 */
public class Robot {

    /**
     * Singleton instance of robot
     */
    private static Robot instance = null;

    /**
     * Instance of Shooter system.
     */
    public Shooter shooter;

    /**
     * Instance of intake system.
     */
    public Intake intake;

    /**
     * Instance of drivetrain system.
     */
    public DriveTrain drivetrain;

    /**
     * Instance of compressor.
     */
    public Compressor comp;

    /**
     * Instance of vision system.
     */
    public Vision camera;

    /**
     * Constructor.
     */
    private Robot() {
        shooter = new Shooter(Ports.LEFT_SHOOTER, Ports.RIGHT_SHOOTER, Ports.ARM_POT, Ports.ZERO_SWITCH);
        intake = new Intake(Ports.INTAKE_PWM, Ports.INTAKE_SOLA, Ports.INTAKE_SOLB);
        drivetrain = new DriveTrain(Ports.LEFT_DRIVE, Ports.RIGHT_DRIVE);
        drivetrain.setSensors(Ports.GYRO, Ports.LEFT_ENC_A, Ports.LEFT_ENC_B,
                Ports.RIGHT_ENC_A, Ports.RIGHT_ENC_B);
        drivetrain.resetSensors();
        camera = Vision.getInstance();
        comp = new Compressor(Ports.COMP_SWITCH, Ports.COMP_RELAY);
        comp.start();
    }

    /**
     * Gets current instance of the robot object. If one does not exist yet,
     * construct one and return it.
     *
     * @return
     */
    public static Robot getInstance() {
        if (instance == null) {
            instance = new Robot();
        }
        return instance;
    }
}
