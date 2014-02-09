/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2014.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team399.y2014.Utilities.GamePad;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Config.Ports;
import org.team399.y2014.robot.Systems.Robot;
import org.team399.y2014.robot.Systems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Main extends IterativeRobot {

    Robot robot;

    Joystick driverLeft = new Joystick(Ports.DRIVER_LEFT_JOYSTICK_USB);
    Joystick driverRight = new Joystick(Ports.DRIVER_RIGHT_JOYSTICK_USB);
    GamePad gamePad = new GamePad(Ports.OPERATOR_GAMEPAD_USB);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        robot = Robot.getInstance();
    }

    public void testInit() {
        robot.shooter.setState(Shooter.States.TEST);  //shooter autocalibrate mode
    }

    public void testPeriodic() {
        robot.shooter.run();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    public void disabledPeriodic() {
        SmartDashboard.putNumber("ArmPosition", robot.shooter.getPosition());
    }

    public void teleopInit() {
        robot.shooter.setState(Shooter.States.MANUAL);
    }
int state = Shooter.States.MANUAL;
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("ARM_POT" + robot.shooter.getPosition());
        SmartDashboard.putNumber("ArmPosition", robot.shooter.getPosition());
        robot.drivetrain.tankDrive(driverLeft.getRawAxis(2),
                driverRight.getRawAxis(2));

        
        if (gamePad.getButton(10)) {
            state = Shooter.States.STOW;
        } else if (gamePad.getButton(2)) {
            state = Shooter.States.TRUSS;
        } else if (gamePad.getButton(8)) {
            state = Shooter.States.SHOOT;
        } else if (gamePad.getButton(6)) {
            state = Shooter.States.STAGE;
        } else if (robot.intake.state == Constants.Intake.RETRACTED) {
            state = Shooter.States.HOLD;
        } else if (gamePad.getButton(9)){
            state = Shooter.States.MANUAL;
            
        }
        robot.shooter.setManual(gamePad.getRightY() / 2);

        robot.shooter.setState(state);

        robot.shooter.run();

        if (gamePad.getDPad(GamePad.DPadStates.UP)) {
            robot.intake.setMotors(1.0);
        } else if (gamePad.getDPad(GamePad.DPadStates.DOWN)) {
            robot.intake.setMotors(-1.0);
        } else {
            robot.intake.setMotors(0);
        }

        robot.intake.setToggle(gamePad.getButton(5));

    }
}
