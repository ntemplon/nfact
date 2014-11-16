/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
