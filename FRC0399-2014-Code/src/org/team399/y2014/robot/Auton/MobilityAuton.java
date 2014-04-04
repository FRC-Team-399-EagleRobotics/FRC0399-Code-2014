/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.CalibrateCommand;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class MobilityAuton extends CommandGroup {
    
    public MobilityAuton(){
     System.out.println("Mobility Auton!");   
     this.addSequential(new CalibrateCommand(.500));
     this.addSequential(new ArcadeDriveCommand(.65, 0, 1.5));
    }

}
