package com.gooddata.processor;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Command.
 *
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class Command {

    // Command
    private String command;
    // Command parameters
    private Properties parameters;


    /**
     * Constructor
     * 
     * @param command command name
     * @param params parameters
     */
    public Command(String command, Properties params) {
        this.command = command;
        this.parameters = params;
    }

    /**
     * Constructor
     *
     * @param command command name
     * @param params parameters
     */
    public Command(String command, String params) {
        this.command = command;
        this.parameters = new Properties();
        try {
            this.parameters.load(new StringReader(params.replace(",","\n")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Command getter
     * @return command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Command setter
     * @param command command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Paremeters getter
     * @return parameters
     */
    public Properties getParameters() {
        return parameters;
    }

    /**
     * Parameters setter
     * @param parameters command parameters
     */
    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }

}