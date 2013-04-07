/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Sudoku extends MIDlet implements CommandListener{
    
    private final String title = "AntSudoku v0.5";
    private final String HelpLicence =
            "Fill all cells with numbers. Each row, column, and region must contain each number only once.\n\n"+
            "[2]UP\n"+
            "[8]DOWN\n"+
            "[4]LEFT\n"+
            "[6]RIGHT\n"+
            "[5]EDIT\n"+
            "[0]CLEAR\n\n"+
            "Copyright(C)2006\n"+
            "Antonis Lilis.\n"+
            "All rights reserved.\n"+
            "AntSudoku is free to install and use. Your service provider may charge for network traffic.\n"+
            "There is absolutely no warranty for using this program.\n"+
            "antonis.lilis@gmail.com";
    private Form AntSudoku;
    private Arena arena;
    private List level, hints;
    private Command exitCommand, startCommand;
    private Command exitLevelCommand, submitLevelCommand;
    private Command exitHintsCommand, submitHintsCommand;
    
    public void startApp() { initialize(); }
    
    public void pauseApp() {}
    
    public void destroyApp(boolean unconditional) {}
    
    private void initialize() { getDisplay().setCurrent(get_AntSudoku()); }
    
    protected Display getDisplay() { return Display.getDisplay(this); }
    
    protected String title() { return title; }
    
    protected boolean hints() { return (hints.getSelectedIndex()==0)?true:false; }
    
    protected int level() { return level.getSelectedIndex(); }    
    
    /*
     * Command Actions
     */
    
    public void commandAction(Command command, Displayable displayable) {
        if(command == exitCommand) exitMIDlet();
        else if (command == startCommand) getDisplay().setCurrent(get_Level());
        else if (command == exitLevelCommand) getDisplay().setCurrent(get_AntSudoku());
        else if (command == submitLevelCommand) getDisplay().setCurrent(get_Hints());
        else if (command == exitHintsCommand) getDisplay().setCurrent(get_Level());
        else if (command == submitHintsCommand) getDisplay().setCurrent(get_Arena());
    }
    
    protected void exitMIDlet() { 
        getDisplay().setCurrent(null);
        destroyApp(true); notifyDestroyed();
    }
    
    /*
     * Screen Initializers
     */
    
    protected Form get_AntSudoku() {
        if (AntSudoku == null) {
            AntSudoku = new Form(title);
            AntSudoku.append(HelpLicence);
            AntSudoku.addCommand(get_exitCommand());
            AntSudoku.addCommand(get_startCommand());
            AntSudoku.setCommandListener(this);
        }   return AntSudoku;
    }
    
    private List get_Level() {
        if(level == null){
            level = new List("Level", Choice.EXCLUSIVE);
            level.append("Easy", null);
            level.append("Medioum", null);
            level.append("Hard", null);
            level.append("Edit mine", null);
            level.addCommand(get_exitLevelCommand());
            level.addCommand(get_submitLevelCommand());
            level.setCommandListener(this);
        }   return level;
    }
    
    private List get_Hints() {
        if(hints == null){
            hints = new List("Hints", Choice.EXCLUSIVE);
            hints.append("On", null);
            hints.append("Off", null);
            hints.addCommand(get_exitHintsCommand());
            hints.addCommand(get_submitHintsCommand());
            hints.setCommandListener(this);
        }   return hints;
    }
    
    private Arena get_Arena() {
        if(arena==null) arena = new Arena(this);
        arena.reInitialize();
        return arena;
    }
    
    /*
     * Command Initializers
     */
    
    private Command get_exitCommand() {
        if (exitCommand == null) exitCommand = new Command("Exit", Command.EXIT, 1);
        return exitCommand;
    }
    
    private Command get_startCommand() {
        if (startCommand == null) startCommand = new Command("Start", Command.OK, 1);
        return startCommand;
    }
    
    private Command get_exitLevelCommand() {
        if (exitLevelCommand == null) exitLevelCommand = new Command("Back", Command.BACK, 1);
        return exitLevelCommand;
    }
    
    private Command get_submitLevelCommand() {
        if (submitLevelCommand == null) submitLevelCommand = new Command("Ok", Command.OK, 1);
        return submitLevelCommand;
    }
    
    private Command get_exitHintsCommand() {
        if (exitHintsCommand == null) exitHintsCommand = new Command("Back", Command.BACK, 1);
        return exitHintsCommand;
    }
    
    private Command get_submitHintsCommand() {
        if (submitHintsCommand == null) submitHintsCommand = new Command("Ok", Command.OK, 1);
        return submitHintsCommand;
    }
    
}
