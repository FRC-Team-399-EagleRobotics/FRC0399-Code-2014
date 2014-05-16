/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import java.util.Vector;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.WaitCommand;

/**
 *
 * Format: COMMAND_NAME ARG1 ARG2 ARG3 ARG4 ARG5 ARG6
 *
 * Example file: Mobility.aut WAIT 1.0 # This is a comment ARCADE_DRIVE 1.0 0.0
 * 3.0 # comments are lines that start with
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class AutonomousFileParser extends TextFileReader {

    private String fileName = null;
    private Vector lines = null;
    private boolean isEmpty = false;
    private CommandGroup out = null;

    private final String SEPARATOR = "    ";

    /**
     * Constructor.
     *
     * @param fileName path to file on cRIO filesystem.
     */
    public AutonomousFileParser(String fileName) {
        super(fileName);
        lines = new Vector();
        lines = super.getLines();
        if (lines == null) {
            System.out.println("File empty!!");
            isEmpty = true;
        }
    }

    /**
     * Update the file and re-parse the lines into commands.
     */
    public void update() {
        super.update();
        lines = super.getLines();
    }

    /**
     * Processes lines from the text file, trimming excess whitespace and
     * ignoring certain lines appropriately.
     *
     * @return
     */
    private Vector parseLines() {
        Vector parsed = new Vector();
        if (!isEmpty) {
            for (int i = 0; i < lines.size(); i++) {
                String in = (String) lines.elementAt(i);
                in = in.toUpperCase();
                in = in.trim();
                if (in.equals("")
                        || !in.startsWith("//")
                        || !in.startsWith("#")) {
                    parsed.addElement(lines.elementAt(i));
                }
            }
        }

        return parsed;
    }

    /**
     * Converts a string(representing a line from the text file) into a command
     * for adding into a commandGroup
     *
     * @param line String line to be parsed into a command.
     * @return a command object
     */
    private Command toCommand(String line) {
        Command cmd = null;

        String[] split = StringUtils.split(line, SEPARATOR);
        String name = split[0].trim();
        Vector args = new Vector();

        for (int i = 1; i < split.length; i++) {
            args.addElement(split[i].trim());
        }

        if (name.equals("ARCADE_DRIVE")) {
            cmd = new ArcadeDriveCommand(Double.parseDouble((String) args.elementAt(0)),
                    Double.parseDouble((String) args.elementAt(1)),
                    Double.parseDouble((String) args.elementAt(2)));
        } else if (name.equals("WAIT")) {
            cmd = new WaitCommand(Double.parseDouble((String) args.elementAt(0)));

        }// TODO: Add more commands here.

        return cmd;
    }

    /**
     * Iterates through the parsed list of lines from the text file and converts
     * the lines into commands for adding into the command group.
     *
     * @return
     */
    public CommandGroup toCommandGroup() {
        Vector parsed = parseLines();
        CommandGroup cg = new CommandGroup();

        for (int i = 0; i < parsed.size(); i++) {
            cg.addSequential(toCommand((String) parsed.elementAt(i)));
        }

        return cg;
    }

}
