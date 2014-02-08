/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2014.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team399.y2014.Utilities.GamePad;
import org.team399.y2014.robot.Config.Ports;
import org.team399.y2014.robot.Systems.DriveTrain;
import org.team399.y2014.robot.Systems.Intake;
import org.team399.y2014.robot.Systems.Shooter;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Main extends IterativeRobot {
    
    public Shooter shooter;
    public Intake intake;
    public DriveTrain drivetrain;
    
    Compressor comp = new Compressor(5, 1);
    
    Joystick driverLeft = new Joystick(Ports.DRIVER_LEFT_JOYSTICK_USB);
    Joystick driverRight = new Joystick(Ports.DRIVER_RIGHT_JOYSTICK_USB);
    GamePad gamePad = new GamePad(Ports.OPERATOR_GAMEPAD_USB);
    
    
    

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        shooter = new Shooter(Ports.LEFT_SHOOTER, Ports.RIGHT_SHOOTER, Ports.ARM_POT);
        intake = new Intake(Ports.INTAKE_PWM, Ports.INTAKE_SOLA, Ports.INTAKE_SOLB);
        drivetrain = new DriveTrain(Ports.LEFT_DRIVE, Ports.RIGHT_DRIVE);
        comp.start();
    }
    
    public void testInit() {
        shooter.setState(Shooter.States.TEST);  //shooter autocalibrate mode
    }
    
    public void testPeriodic() {
        shooter.run();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    public void disabledPeriodic(){
         System.out.println("ARM_POT" + shooter.getPosition());
    }
    
    public void teleopInit() {
        shooter.setState(Shooter.States.MANUAL);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        System.out.println("ARM_POT" + shooter.getPosition());
        drivetrain.tankDrive(driverLeft.getRawAxis(2), 
                               driverRight.getRawAxis(2));
        
        //shooter.setManual(gamePad.getLeftY());
        if(gamePad.getButton(1)){
            shooter.setState(0);
        }
        
        shooter.run();
        
        if(gamePad.getDPad(GamePad.DPadStates.UP)) {
            intake.setMotors(1.0);
        } else if(gamePad.getDPad(GamePad.DPadStates.DOWN)) {
            intake.setMotors(-1.0);
        } else {
            intake.setMotors(0);
        }
        
        intake.setActuators(gamePad.getButton(5));
        

    }
}
