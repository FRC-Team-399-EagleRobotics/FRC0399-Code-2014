/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;


/**
 *
 * @author Jeremy
 */
public class Robot {
    private Gyro yaw = null;
    private Encoder leftEnc = null;
    private Encoder rightEnc = null;
    
    public void setSensors(int gyro, int leftEncA, int leftEncB, int rightEncA, int rightEncB) {
        yaw = new Gyro(gyro);
        leftEnc = new Encoder(leftEncA, leftEncB);
        rightEnc = new Encoder(rightEncA, rightEncB);
        leftEnc.start();
        rightEnc.start();
        yaw.reset();

}
    
}