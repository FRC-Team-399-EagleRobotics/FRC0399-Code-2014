/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author Jeremy
 */
public class Intake {

    private Talon m_intakeA = null;
    private Solenoid m_solA = null;
    private Solenoid m_solB = null;

    public boolean state = false;

    public Intake(int motor, int sA, int sB) {
        m_intakeA = new Talon(motor);
        m_solA = new Solenoid(sA);
        m_solB = new Solenoid(sB);

    }

    public void setMotors(double input) {
        m_intakeA.set(input);
    }
    private boolean prevState = false;

    public void setActuators(boolean state) {
        this.prevState = this.state;
        this.state = state;
        m_solA.set(this.state);
        m_solB.set(this.state);
    }
    private long startTime = 0;

    public boolean getArmSafety() {
        boolean allowArm = false;
        boolean stateChange = prevState != state;
        long elapsed = System.currentTimeMillis() - startTime;

        if (stateChange) {
            startTime = System.currentTimeMillis();
        }

        if (state == Constants.Intake.EXTENDED) {
            System.out.println("[INTAKE] Deploying...");
            if (elapsed > 500) {
                allowArm = true;
            } else {
                allowArm = false;
            }
        }

        if (state == Constants.Intake.RETRACTED) {
            System.out.println("[INTAKE] Retracting...");
            if (elapsed < 300) {
                allowArm = true;
            } else {
                allowArm = false;
            }
        }

        //System.out.println("[INTAKE] Timer running...  " + (System.currentTimeMillis() - startTime));
        return allowArm;
    }

}
