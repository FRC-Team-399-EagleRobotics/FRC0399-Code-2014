/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Jeremy
 */
public class Intake {
    private Talon m_intakeA = null;
    private Solenoid m_solA = null;
    private Solenoid m_solB = null;
   
    public Intake(int motor, int sA, int sB){
        m_intakeA = new Talon(motor);
        m_solA = new Solenoid(sA);
        m_solB = new Solenoid(sB);
        
    }
    
    public void setMotors(double input) {
        m_intakeA.set(input);
    }
    
    public void setActuators(boolean state) {
        m_solA.set(state);
        m_solB.set(state);
    }

}
