/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;


import org.team399.y2014.robot.Config.Ports;


/**
 *
 * @author Jeremy
 */
public class Robot {
    
    public Shooter shooter;
    public Intake intake;
    public DriveTrain drivetrain;
    
    private Robot(){
        shooter = new Shooter(Ports.LEFT_SHOOTER, Ports.RIGHT_SHOOTER, Ports.ARM_POT);
        intake = new Intake(Ports.INTAKE);
    }
    

}
    
