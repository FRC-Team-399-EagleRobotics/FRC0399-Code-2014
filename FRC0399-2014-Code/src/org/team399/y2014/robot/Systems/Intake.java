/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Jeremy
 */
public class Intake {
    private Talon m_intakeA = null;
    private Talon m_intakeB = null;
   
    public void Intake(int a, int b){
        m_intakeA = new Talon(a);
        m_intakeB = new Talon(b);
    
}

}
