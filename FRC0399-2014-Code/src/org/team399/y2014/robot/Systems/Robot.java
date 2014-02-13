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
 *
 * @author Jeremy
 */
public class Robot {

    private static Robot instance = null;
    public Shooter shooter;
    public Intake intake;
    public DriveTrain drivetrain;
    public Compressor comp;
    public Vision camera = Vision.getInstance();
    

    private Robot() {
        shooter = new Shooter(Ports.LEFT_SHOOTER, Ports.RIGHT_SHOOTER, Ports.ARM_POT);
        intake = new Intake(Ports.INTAKE_PWM, Ports.INTAKE_SOLA, Ports.INTAKE_SOLB);
        drivetrain = new DriveTrain(Ports.LEFT_DRIVE, Ports.RIGHT_DRIVE);
        drivetrain.setSensors(Ports.GYRO, Ports.LEFT_ENC_A, Ports.LEFT_ENC_B, 
                Ports.RIGHT_ENC_A, Ports.RIGHT_ENC_B);
        comp = new Compressor(Ports.COMP_SWITCH, Ports.COMP_RELAY);
        comp.start();
    }

    public static Robot getInstance() {
        if (instance == null) {
            instance = new Robot();
        }
        return instance;
    }
}
