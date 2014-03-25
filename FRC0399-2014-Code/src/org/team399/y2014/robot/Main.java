/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2014.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team399.y2014.Utilities.GamePad;
import org.team399.y2014.robot.Auton.FeederAuton;
import org.team399.y2014.robot.Auton.MobilityAuton;
import org.team399.y2014.robot.Auton.OneBallAuton;
import org.team399.y2014.robot.Auton.TestAuton;
import org.team399.y2014.robot.Auton.TestAutonShot;
import org.team399.y2014.robot.Auton.TwoBallAuton;
import org.team399.y2014.robot.Commands.DoNothingAuton;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Config.Ports;
import org.team399.y2014.robot.Systems.DriveTrain;
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
    SendableChooser autonChooser = new SendableChooser();
    CommandGroup currAuton = null;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        robot = Robot.getInstance();


        autonChooser.addObject("Test Auton", new TestAuton());
        autonChooser.addObject("Mobility Only", new MobilityAuton());
        autonChooser.addObject("One Ball", new OneBallAuton());
        autonChooser.addDefault("Two Ball", new TwoBallAuton());
        autonChooser.addObject("Feeder", new FeederAuton());
        autonChooser.addObject("Do Nothing Auton #Sweg ", new DoNothingAuton());
        autonChooser.addObject("TestShoot", new TestAutonShot());
        //autonChooser.addObject("Three Ball", new ThreeBallAuton());
        SmartDashboard.putData("auton Chooser", autonChooser);
    }

    public void testInit() {
        robot.shooter.setState(Shooter.States.TEST);  // shooter autocalibrate mode
        if (currAuton != null) {    //Cancel auton if it hasn't already ended.
            currAuton.cancel();
            currAuton = null;
        
        }
        LiveWindow.setEnabled(false);
    }

    public void testPeriodic() {
        LiveWindow.setEnabled(false);
        robot.shooter.run();
        updateSmartDashboard();
    }

    public void autonomousInit() {
        robot.comp.stop();
        robot.drivetrain.resetSensors();
        /*System.out.println("[AUTON] Starting: "
                + autonChooser.getSelected().toString());*/
        if (currAuton != null) {
            currAuton.cancel();
            currAuton = null;
        }
        currAuton = (CommandGroup) autonChooser.getSelected();
        Scheduler.getInstance().add(currAuton);
//        this.updateLcd();
    }
   
    

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateSmartDashboard();
        
        //System.out.println( "displacement "+robot.drivetrain.getEncoderDisplacement(true));
    }

    public void disabledInit() {
        if (currAuton != null) {
            currAuton.cancel();
            currAuton = null;
        }
    }

    public void disabledPeriodic() {
        updateSmartDashboard();

        System.out.println("right" + robot.drivetrain.rightEnc.getDistance());
        System.out.println("left" + -robot.drivetrain.leftEnc.getDistance());


        //System.out.println("D: " + robot.drivetrain.getEncoderDisplacement(false));

        robot.drivetrain.getEncoderDisplacement(false);
        //System.out.println("T: " + robot.drivetrain.getEncoderTurn(false));
        this.updateLcd();
    }
    double armPotInit = 0.0;

    public void teleopInit() {
        
        robot.comp.start();
        robot.shooter.setManual(0);
        if (currAuton != null) {
            
            currAuton.cancel();
            currAuton = null;
        }

        if (robot.shooter.getPosition()
                < (Constants.Shooter.INTAKE_LIMIT + robot.shooter.m_lowerLim)) {
            state = Shooter.States.HOLD;
            System.out.println("ARM TOO LOW, STOWING ARM");
        } else {
            state = Shooter.States.MANUAL;
            System.out.println("ARM TOO HIGH, MANUAL!");
        }
        
        if(!robot.shooter.isCalibrated){
            robot.shooter.setState(Shooter.States.TEST);  // shooter autocalibrate mode
            robot.shooter.run();
        }

        robot.shooter.setState(state);
    }
    int state = Shooter.States.MANUAL;
    double stageOffset = 0.0;
    int driveState = 0;

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("ARM_POT" + robot.shooter.getPosition());







        double leftIn = driverLeft.getRawAxis(2);
        double rightIn = driverRight.getRawAxis(2);
        double scalar = .75;

        if (driverRight.getRawButton(12)|| driverRight.getRawButton(1)) {
            scalar = 1.0;
        } else if (driverRight.getRawButton(1)){
            scalar = 1.0;
        }

        //this.updateLcd();
        if (driverLeft.getRawButton(2)) {
            robot.drivetrain.setState(DriveTrain.States.PID_BRAKE);
            //robot.drivetrain.setState(driveState);
        } else {
            robot.drivetrain.setState(DriveTrain.States.TANK_DRIVE);
            //

        }

        robot.drivetrain.setTankDrive(leftIn * scalar, rightIn * scalar);
        robot.drivetrain.run();

        double offset = 0.0;

        if (gamePad.getButton(9)) {
            state = Shooter.States.MANUAL;
        } else if (gamePad.getButton(10)) {
            state = Shooter.States.STAGE;
        } else if (gamePad.getButton(8) && robot.intake.state == Constants.Intake.EXTENDED) {

//            if (gamePad.getButton(1) && gamePad.getButton(2)) {
//                offset = .05;
//            } else if (gamePad.getButton(2) && gamePad.getButton(3)) {
//                offset = .1;
//            } else if (gamePad.getButton(3) && gamePad.getButton(4)) {
//                offset = -.25;
//            } else if (gamePad.getButton(1) && gamePad.getButton(4)) {
//                offset = -.3;
//            } else if (gamePad.getButton(1)) {
//                offset = -.05;
//            } else if (gamePad.getButton(2)) {
//                offset = -.1;
//            } else if (gamePad.getButton(3)) {
//                offset = -.15;
//            } else if (gamePad.getButton(4)) {
//                offset = -.2;
//            }
            robot.shooter.setGoalOffset(offset);
            state = Shooter.States.SHOOT;
        } else if (gamePad.getButton(1)) {
            state = Shooter.States.SHORT_STAGE;
        } else if (gamePad.getButton(7) && robot.intake.state == Constants.Intake.EXTENDED) {
            state = Shooter.States.SHORT_SHOT;
        } else if (robot.intake.state == Constants.Intake.RETRACTED && robot.shooter.getOffsetFromBottom() < .75 )  {
            state = Shooter.States.HOLD;
        } else if (robot.shooter.getShootDone()) {
            state = Shooter.States.SHORT_STAGE;
        } else if (robot.shooter.wantLiveCal()) {
            state = Shooter.States.LIVE_CAL;
        } else if (robot.intake.output < 0) {
            state = Shooter.States.HOLD;
            robot.shooter.setGoalOffset(SmartDashboard.getNumber("StageOffset", 0));
        }

        if (robot.shooter.getState() == Shooter.States.SHOOT || robot.shooter.getState() == Shooter.States.SHORT_SHOT) {
            robot.comp.stop();
        } else {
            robot.comp.start();
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

        robot.intake.setToggle(gamePad.getButton(5) || driverLeft.getRawButton(1) && driverRight.getRawButton(1));
        this.updateLcd();
        this.updateSmartDashboard();
    }

    /**
     * Updates driver station LCD with important diagnostic information.
     */
    public void updateLcd() {
        robot.drivetrain.getEncoderDisplacement(false);
        String driveIo = "D: [Le: " + robot.drivetrain.leftE + " Re: "
                + robot.drivetrain.rightE + " Y: " + robot.drivetrain.getHeading() + "]";

        String shooterIo = "S:";
        String intakeIo;

        //System.out.println(driveIo);
    }
    
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("armOffset", robot.shooter.getOffsetFromBottom());
        SmartDashboard.putNumber("armPosition", robot.shooter.getPosition());
        SmartDashboard.putString("Current armState", Shooter.States.toString(robot.shooter.getState()));
        SmartDashboard.putNumber("Battery voltage", DriverStation.getInstance().getBatteryVoltage());
        SmartDashboard.putBoolean("is calibrated", robot.shooter.isCalibrated);
        SmartDashboard.putBoolean("Intake State", robot.intake.state);
        SmartDashboard.putData("auton Chooser", autonChooser);

    }
}
