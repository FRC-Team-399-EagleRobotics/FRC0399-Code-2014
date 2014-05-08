/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2014.robot;

import org.team399.y2014.robot.Auton.DoNothingAuton;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.livewindow.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import org.team399.y2014.Utilities.*;
import org.team399.y2014.robot.Auton.*;
import org.team399.y2014.robot.Commands.*;
import org.team399.y2014.robot.Config.*;
import org.team399.y2014.robot.Systems.*;

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
    

    // Cheesy Vision server instance and port number for auton hot goal
    // indication.
    public static CheesyVisionServer server = CheesyVisionServer.getInstance();
    public final int listenPort = 1180;

    double intake = 0.0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Initialization of robot system.s
        robot = Robot.getInstance();

        // Note: think about removing some unused autons for competition.
        autonChooser.addObject("Test Auton", new TestAuton());
        autonChooser.addObject("Mobility Only", new MobilityAuton());
        autonChooser.addObject("One Ball", new OneBallAuton());
        autonChooser.addObject("Two Ball", new TwoBallAuton());
        autonChooser.addObject("Feeder", new FeederAuton());
        autonChooser.addObject("Do Nothing Auton #Sweg ", new DoNothingAuton());
        autonChooser.addObject("TestShoot", new TestAutonShot());
        autonChooser.addObject("TwoBallEncoder", new TwoBallEncoder());
        autonChooser.addDefault("HotGoalOneBall", new HotGoalOneBall());
        autonChooser.addObject("Tuning Vision", new VisionTestAuton());
        autonChooser.addObject(" Cheesy Vision ", new CheesyVisionTestAuton());
        autonChooser.addObject(" TwoBall Hot", new TwoBallHotGoal());
        SmartDashboard.putData("auton Chooser", autonChooser);

        // Cheesy Vision server initialization.
        server.setPort(listenPort);
        server.start();
    }

    public void testInit() {
        // shooter autocalibrate mode
        
        robot.shooter.setState(Shooter.States.TEST);
        if (currAuton != null) {    // Cancel auton if it hasn't already ended.
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

        // CheesyVision server begin.  Resets counter and starts it for auton.
        server.reset();
        server.startSamplingCounts();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateSmartDashboard();
        System.out.println("Current left: " + server.getLeftStatus() + ", current right: " + server.getRightStatus());
        System.out.println("Left count: " + server.getLeftCount() + ", right count: " + server.getRightCount() + ", total: " + server.getTotalCount() + "\n");
        //System.out.println("displacement " +
        //robot.drivetrain.getEncoderDisplacement(true));
    }

    public void disabledInit() {

        // Stop cheesy vision server polling in disabled.
        server.stopSamplingCounts();

        if (currAuton != null) {
            currAuton.cancel();
            currAuton = null;
        }
    }

    public void disabledPeriodic() {
        updateSmartDashboard();

        System.out.println("right" + robot.drivetrain.rightEnc.getDistance());
        System.out.println("left" + -robot.drivetrain.leftEnc.getDistance());

        //System.out.println("D: " +
        //robot.drivetrain.getEncoderDisplacement(false));
        robot.drivetrain.getEncoderDisplacement(false);
        //System.out.println("T: " +
        //robot.drivetrain.getEncoderTurn(false));
        this.updateLcd();
    }
    double armPotInit = 0.0;

    public void teleopInit() {

        robot.comp.start();         // Begin compressor control loop
        robot.shooter.setManual(0); // Zero out manual input on shooter
        if (currAuton != null) {    // Cancel auton if it hasn't ended naturally
            currAuton.cancel();
            currAuton = null;
        }

        // Shooter safety logic. put shooter in manual if it hasn't properly
        // stowed in a previous robot state.
        if (robot.shooter.getPosition()
                < (Constants.Shooter.INTAKE_LIMIT + robot.shooter.m_lowerLim)) {
            state = Shooter.States.HOLD;
            System.out.println("ARM TOO LOW, STOWING ARM");
        } else {
            state = Shooter.States.MANUAL;
            System.out.println("ARM TOO HIGH, MANUAL!");
        }

        // Shooter auto calibrate if it hasn't already been completed.
        if (!robot.shooter.isCalibrated) {
            // shooter autocalibrate mode
            robot.shooter.setState(Shooter.States.TEST);
            robot.shooter.run();
        } // Note: is this really working?

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

        if (driverRight.getRawButton(12) || driverRight.getRawButton(1)) {
            scalar = 1.0;
        } else if (driverRight.getRawButton(1)) {
            scalar = 1.0;
        }

        // Note: Remove this if it isn't being used. I have a feeling it might
        // be buggy if accidentally triggered. Just keep drive in tank
        // drive state.
        if (driverLeft.getRawButton(12)) {
            System.out.println("PID_BRAKE");
            robot.drivetrain.setState(DriveTrain.States.PID_BRAKE);
            //robot.drivetrain.setState(driveState);
        } else {
            robot.drivetrain.setState(DriveTrain.States.TANK_DRIVE);
        }

        robot.drivetrain.setTankDrive(leftIn * scalar, rightIn * scalar);
        robot.drivetrain.run();

        double offset = 0.0;

        // Shooter state change conditional
        if (gamePad.getButton(9)) {
            state = Shooter.States.MANUAL;
        } else if (gamePad.getButton(10)) {
            state = Shooter.States.STAGE;
        } else if (gamePad.getButton(8)
                && robot.intake.state == Constants.Intake.EXTENDED) {

            // Note: consider removing the following block. It hasn't been used
            // since end of build.
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
            robot.shooter.setGoalOffset(offset);    // Note: this too
            state = Shooter.States.SHOOT;
        } else if (gamePad.getButton(1)) {
            state = Shooter.States.SHORT_STAGE;
        } else if (gamePad.getButton(7)
                && robot.intake.state == Constants.Intake.EXTENDED) {
            state = Shooter.States.SHORT_SHOT;
        } else if (robot.intake.state == Constants.Intake.RETRACTED
                && robot.shooter.getOffsetFromBottom() < .75) {
            state = Shooter.States.STOW;
        } else if (robot.shooter.getShootDone()) {
            state = Shooter.States.HOLD;
        //} else if (robot.shooter.wantLiveCal()) {
           // state = Shooter.States.LIVE_CAL;
        } else if (robot.intake.output < 0) {
            state = Shooter.States.HOLD;
            robot.shooter.setGoalOffset(
                    SmartDashboard.getNumber("StageOffset", 0));
        }

        // Smart compressor logic to prevent further battery drain.
        // Stops compressor during any shot states or if the intake is running.
        if (robot.shooter.getState() == Shooter.States.SHOOT
                || robot.shooter.getState() == Shooter.States.SHORT_SHOT
                || intake == 1.0) {
            robot.comp.stop();
        } else {
            robot.comp.start();
        }

        // Manual control logic.
        robot.shooter.setManual(gamePad.getRightY() / 2);

        robot.shooter.setState(state);
        robot.shooter.run();

        // Intake system control.
        if (gamePad.getDPad(GamePad.DPadStates.UP)) {
            intake = 1.0;
            robot.intake.setMotors(intake);
        } else if (gamePad.getDPad(GamePad.DPadStates.DOWN)) {
            intake = 1.0;
            robot.intake.setMotors(-intake);

        } else {
            intake = 0.0;
            robot.intake.setMotors(intake);
        }

        robot.intake.setToggle(gamePad.getButton(5)
                || driverLeft.getRawButton(1)
                && driverRight.getRawButton(1));
        this.updateLcd();
        this.updateSmartDashboard();
    }

    /**
     * Updates driver station LCD with important diagnostic information.
     */
    public void updateLcd() {
        robot.drivetrain.getEncoderDisplacement(false);
        String driveIo = "D: [Le: " + robot.drivetrain.leftE
                + " Re: " + robot.drivetrain.rightE
                + " Y: " + robot.drivetrain.getHeading() + "]";

        String shooterIo = "S:";
        String intakeIo;

        //System.out.println(driveIo);
    }

    /**
     * Updates smartdashboard user interface with diagnostic information on the
     * robot systems.
     */
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("armOffset",
                robot.shooter.getOffsetFromBottom());
        SmartDashboard.putNumber("armPosition", robot.shooter.getPosition());
        SmartDashboard.putString("Current armState",
                Shooter.States.toString(robot.shooter.getState()));
        SmartDashboard.putNumber("Battery voltage",
                DriverStation.getInstance().getBatteryVoltage());
        SmartDashboard.putBoolean("is calibrated", robot.shooter.isCalibrated);
        SmartDashboard.putBoolean("Intake State", robot.intake.state);
        SmartDashboard.putData("auton Chooser", autonChooser);

    }
}
