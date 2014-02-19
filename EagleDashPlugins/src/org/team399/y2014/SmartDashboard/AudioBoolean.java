/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.SmartDashboard;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.StringProperty;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JLabel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class AudioBoolean extends Widget {

    public static final DataType[] TYPES = {DataType.BOOLEAN};

    public boolean value = false;
    public boolean prevValue = false;

    StringProperty path = new StringProperty(this, "path");
    String m_path = null;

    JLabel label = null;

    @Override
    public void setValue(Object value) {
        this.prevValue = this.value;
        this.value = ((Boolean) value).booleanValue();
    }

    @Override
    public void init() {
        label = new JLabel(this.getFieldName());
        add(label);
    }

    InputStream in = null;
    AudioStream as = null;

    boolean fileFound = false;

    @Override
    public void propertyChanged(Property property) {
        if (property == path) {
            m_path = path.getValue();
            in = null;
            try {
                in = new FileInputStream(m_path);
                as = new AudioStream(in);
                fileFound = true;
            } catch (Exception e) {
                fileFound = false;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (fileFound && (this.prevValue != this.value) && this.value) {
            AudioPlayer.player.start(as);
        } else if (fileFound && !this.value) {
            AudioPlayer.player.stop(as);
        }
    }
}
