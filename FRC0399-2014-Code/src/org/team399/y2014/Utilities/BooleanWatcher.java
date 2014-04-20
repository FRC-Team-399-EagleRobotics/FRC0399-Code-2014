/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

/**
 * Class for creating a boolean that could be read and accessed across multiple
 * objects. Mainly for use in our ConditionalCommand.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class BooleanWatcher {

    private boolean bool = false;

    /**
     * Constructor
     *
     * @param init initial boolean state
     */
    public BooleanWatcher(boolean init) {
        this.bool = init;
    }

    public void set(boolean in) {
        this.bool = in;
    }

    public boolean get() {
        return this.bool;
    }

}
