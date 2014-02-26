/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems.Vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author ivansalazar003@gmail.com (Ivan Salazar)
 */
public class Vision {

    private static Vision instance = null;

    private Vision() {

    }

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }

        return instance;
    }

    public void setHsv(double hLow, double hHigh,
            double sLow, double sHigh,
            double vLow, double vHigh) {
        SmartDashboard.putNumber("h_low", hLow);
        SmartDashboard.putNumber("h_high", hHigh);
        SmartDashboard.putNumber("s_low", sLow);
        SmartDashboard.putNumber("s_high", sHigh);
        SmartDashboard.putNumber("v_low", vHigh);
        SmartDashboard.putNumber("v_high", vHigh);

    }

    public Target getTarget() {
        return new Target(SmartDashboard.getNumber("TargetX", 0.0),
                SmartDashboard.getNumber("TargetY", 0.0),
                SmartDashboard.getNumber("TargetHeight", 0.0),
                SmartDashboard.getNumber("TargetWidth", 0.0));
    }

}
