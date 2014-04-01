/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

import edu.wpi.first.wpilibj.KinectStick;

/**
 * Kinect controller for autonomous hot goal indication.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class KinectController {

    KinectStick m_kinL, m_kinR;

    private static KinectController instance;

    private KinectController() {
        m_kinL = new KinectStick(1);
        m_kinR = new KinectStick(2);
    }

    public static KinectController getInstance() {
        if (instance == null) {
            instance = new KinectController();
        }

        return instance;
    }

    /**
     * Get human signal on status of hot goal.
     *
     * @return
     */
    public boolean getRightHot() {
        double threshold = .65;
        return (m_kinL.getY() > threshold)
                && (m_kinR.getY() > threshold);
    }

}
