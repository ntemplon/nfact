/*
 * Copyright (C) 2014 Nathan Templon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import settings.Settings;

/**
 * A class for logging events in the program.
 * @author Nathan Templon
 */


public class Logger {
    
    public static final String defaultDir = Settings.defaultDirectory();
    public static final String fileName = "WuCalc.log";
    public static final String defaultPath = defaultDir + "\\" + fileName;
    
    private String dir;
    private String path;
    private FileWriter fw;
    private PrintWriter pw;
    private File file;
    
    private int tabNumber;
    private SimpleDateFormat dateFrmt;
    
    private static Logger defaultLogger = new Logger();
    
    private boolean isOpen = false;
    
    public static Logger getDefaultLogger() {
        
        if (!defaultLogger.isOpen()) {
            try {
                defaultLogger.open();
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The log could not be initialized.");
            }
        }
        
        return defaultLogger;
    }
    
    public Logger() {
        tabNumber = 0;
        dateFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        
        path = defaultPath;
        dir = defaultDir;
        
        try {
            open();
        }
        catch (IOException e) {
            isOpen = false;
        }
    }
    
    public Logger(String dir) {
        this.dir = dir;
        this.path = dir + fileName;
        this.tabNumber = 0;
        this.dateFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }
    
    public void open() throws IOException {
        
        File directory = new File(dir);
        directory.mkdirs();
        file = new File(path);
        
        if (!file.exists()) {
            file.createNewFile();
        }
        
        isOpen = true;
        
        fw = new FileWriter(file);
//        pw = new PrintWriter(fw, true);
        pw = new PrintWriter(fw);
        
        log("Log initiated.");
    }
    
    public void close() {
        
        if (!isOpen) {
            return;
        }
        
        try {
            log("");
            log("Log terminated.");
            pw.close();
            fw.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There was an error while closing the log.");
        }
        
        isOpen = false;
    }
    
    public void log( Object o ) {
        if (!isOpen) {
            try {
                open();
            }
            catch (IOException e) {
                
            }
        }
        
        if (o instanceof Exception) {
            logException((Exception)o);
            return;
        }
        
        String output = "";
        
        output += "[" + (timestamp() + "]:  ");
        
        for (int index = 0; index < tabNumber; index++) {
            output+= "\t";
        }
        
        output += o;
        pw.println(output);
    }
    
    private void logException( Exception e ) {
        log("Encountered an exception: " + e.toString());
        
        for (StackTraceElement element : e.getStackTrace()) {
            log("\t\t" + element.toString());
        }
    }
    
    private String timestamp() {
//        return DateFormat.getDateTimeInstance(MEDIUM, FULL).format(new Date());
        String output = "";
        
        Date date = new Date();
        
        output += dateFrmt.format(date);
        output += "." + String.format("%03d", (date.getTime() % 1000));
        
        return output;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void incIndent() {
        tabNumber++;
    }
    
    public void decIndent() {
        tabNumber--;
    }
    
    public void setIndent( int indent ) {
        tabNumber = indent;
    }
    
}
