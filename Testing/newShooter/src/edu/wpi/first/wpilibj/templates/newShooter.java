/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class newShooter extends IterativeRobot {
    
    Joystick gamePad = new Joystick(1);

    Victor motorA = new Victor(1);//shooter motors
    Victor motorB = new Victor(2);
    Victor motorC = new Victor(3);
    Victor motorD = new Victor(4);
    Victor motorE = new Victor(5);//intake motor
    
    AnalogChannel armPot = new AnalogChannel(1);//pot analog pin
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
     public void disabledPeriodic() {

         System.out.println(armPot.getVoltage());// prints arm pot voltage when disabled
      
    }
     
    public void robotInit() {
        
       
        SmartDashboard.putNumber("kP", kP);//puts values in smartDashboard
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
        SmartDashboard.putNumber("lowStow", lowStow);
        

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
         double leftJoy = gamePad.getRawAxis(2);//gets values of shooter joystick
         double rightJoy = gamePad.getRawAxis(4);//gets values of intake joystick
         SmartDashboard.putNumber("pos", armPot.getVoltage());//puts current position on smartDashboard
         double lowStow = SmartDashboard.getNumber("lowStow", 4.7);//gets lowStow value from smartDashboard
         
         motorE.set(rightJoy);//sets intake motor to rightJoy
         
         
         
         
         if (gamePad.getRawButton(1)) {
            shootState = true;//sets shootstate to true to call shootball function
        }
         if (shootState) {
            shootBall();//calls function to shoot ball
        }
         else if(gamePad.getRawButton(2)){
             
            armMotor(changePosition(lowStow));//sets arm to lowstow setpoint
            System.out.println("output" + changePosition(lowStow));

        }else{
            armMotor(leftJoy / 2);//manual joystick control for shooter
        }
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    } 
    
    //shooter motors function
    void armMotor(double input) {
        motorA.set(input);
        motorB.set(input);
        motorC.set(input);
        motorD.set(input);
    }
    
    
    //variables for positioning
    double lowStow = 4.7;
    double kP = 0.8;
    double kI = 0.0;
    double kD = 0.0;
    double intError = 0;
    double prevPosition = 0;
    
    
    
    //setpoint pid loop
    public double changePosition(double setPoint) {
        double currentPosition = armPot.getVoltage();
        double error = currentPosition - setPoint;
        intError += error;
        if(intError > .5) intError = .5;
        else if(intError < -.5) intError = -.5;
        double dOut = SmartDashboard.getNumber("kD", kD) * (currentPosition - prevPosition);
        prevPosition = currentPosition;
        SmartDashboard.putNumber("error", error);
        System.out.println("error" + error);
        double output = (-error) * SmartDashboard.getNumber("kP", kP) + SmartDashboard.getNumber("kI", kI) * intError + dOut;
        if (output > .25) {
            output = .4;
        } else if (output < -.4) {
            output = -.25;
        }
        return output;
    }
       
    
    
    //variables for shooting
    double shootVoltage = 3.8;//end position voltage
    double startShoot = 3.45;//slow start max position
    boolean shootState = false;
    long startTime = 0;
    
    
     
  
    //
     void shootBall() {
        
        if (armPot.getVoltage() < shootVoltage && shootState) {
            if(armPot.getVoltage() < startShoot){
                armMotor(.4);
            }else{
            armMotor(1);
            startTime = System.currentTimeMillis();
            }
           
        } else {
           
                shootState = false;

            }
    
}
}
