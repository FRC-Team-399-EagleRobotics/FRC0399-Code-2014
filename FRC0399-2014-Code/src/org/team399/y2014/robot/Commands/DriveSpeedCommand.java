/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public abstract class DriveSpeedCommand extends Command {

    private double speed = 0.0;
    private double timeout = 0.0;

    public DriveSpeedCommand(double speed, double timeout) {
        this.speed = speed;
        this.timeout = timeout;
    }

    public void initialize() {

    }

}
