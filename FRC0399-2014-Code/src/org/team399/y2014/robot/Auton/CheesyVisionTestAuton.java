/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.CheesyVisionTestCommand;

/**
 * Auton command group for testing and calibrating the hot goal image processing
 * algorithm
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class CheesyVisionTestAuton extends CommandGroup {

    public CheesyVisionTestAuton() {
        this.addSequential(new CheesyVisionTestCommand());
    }
}
