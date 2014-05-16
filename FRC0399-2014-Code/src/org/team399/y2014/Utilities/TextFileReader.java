/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 * Parses a text file into an array of strings. Useful for filesystem-stored
 * constants, autonomous modes, and other configurations.
 *
 *
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class TextFileReader {

    private Vector lines = null;
    private FileConnection file = null;
    private BufferedReader reader = null;
    private String fileName = null;
    private boolean firstRead = false;
    private boolean exists = false;

    /**
     * Constructor.
     *
     * @param fileName path to file on cRIO filesystem.
     */
    public TextFileReader(String fileName) {
        this.fileName = fileName;
        update(fileName);
    }

    /**
     * Read text from file and store into a vector.
     *
     * @param fileName path to file on cRIO filesystem.
     */
    private void update(String fileName) {
        try {
            file = (FileConnection) Connector.open("file:///" + fileName,
                    Connector.READ);
            reader = new BufferedReader(new InputStreamReader(file.openDataInputStream()));
            lines = new Vector();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.addElement(line);
            }
            exists = true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (lines == null) {
            System.out.println("File empty!!");
        }

        firstRead = true;
    }

    /**
     * Reread text from file.
     */
    public void update() {
        update(this.fileName);
    }

    /**
     * Returns a vector(resizable array) containing Strings representing the
     * lines in the text file.
     *
     * @return
     */
    public Vector getLines() {
        if (!firstRead) {
            update();
        }
        return lines;
    }

    public boolean fileExists() {
        return exists;
    }

}
