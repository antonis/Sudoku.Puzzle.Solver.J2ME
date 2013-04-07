/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

import javax.microedition.lcdui.*;

class Arena extends Canvas implements CommandListener {
    
    private final int startX = 2;
    private final int startY = 2;        
    private int user[][], puzzle[][], solution[][];
    private int curX, curY;
    private int cellSize;
    private long execTime;
    private boolean solved;
    private boolean editMode, writeMode;
    private List editList;
    private Command doneCommand, exitCommand, solveCommand, restartCommand;    
    private Command exitEditListCommand, submitEditListCommand;    
    private Graphics graphics;
    private Sudoku caller;
        
    public Arena(Sudoku caller) {
        this.caller = caller;
        get_Arena();
    }
    
    protected void reInitialize(){
        this.curX = 1;
        this.curY = 1;        
        this.solved = false;
        if(caller.level()!=3){
            editMode = false;
            this.puzzle = new Creator().getPuzzle(caller.level());
        } else {
            editMode = true;
            puzzle = null; puzzle = getEmptyTable();
        }
        user = null; user = getEmptyTable();
        get_Arena();
    }
    
    private void get_Arena(){
        removeCommand(get_doneCommand());
        removeCommand(get_solveCommand()); 
        removeCommand(get_exitCommand());
        removeCommand(get_restartCommand());        
        setTitle(caller.title());
        addCommand(get_exitCommand());
        if(editMode)addCommand(get_doneCommand());
        else addCommand(get_solveCommand());
        setCommandListener(this);        
        if(caller.hints()) cellSize = Math.min((getWidth()-2*startX)/10,(getHeight()-2*startY)/9);
        else cellSize = Math.min((getWidth()-2*startX)/9,(getHeight()-2*startY)/9);          
    }
        
    /*
     * Painting
     */
    
    protected void paint(Graphics graphics) {
        this.graphics = graphics;       
        background(this);
        arena();
        if(solved){
            drawSolution();
            if(caller.hints()) title(); 
        } else {            
            mark(curX,curY,writeMode);
            drawUser();
            if(caller.hints()) drawHints();
        }
        drawPuzzle();
    }
    
    private void drawPuzzle(){
        for(int x=0;x<9;x++) for(int y=0;y<9;y++){
            if(puzzle[x][y]!=0) given(x+1,y+1,puzzle[x][y]);
        }
    }
    
    private void drawUser(){
        for(int x=0;x<9;x++) for(int y=0;y<9;y++){
            if(user[x][y]!=0) input(x+1,y+1,user[x][y]);
        }
    }
    
    private void drawSolution(){
        for(int x=0;x<9;x++) for(int y=0;y<9;y++){
            if(puzzle[x][y]==0) 
                if(user[x][y]!=0&&user[x][y]!=solution[x][y])
                    red(x+1,y+1,solution[x][y]);
                else input(x+1,y+1,solution[x][y]);
        }
    }       
                
    private void drawHints(){
        int x = curX-1; int y = curY-1;
        hints();
        if(puzzle[x][y]!=0&&!editMode) return;
        boolean found;
        int curCell = user[x][y]; user[x][y]=0; 
        int curGiven = 0; if(editMode){ curGiven = puzzle[x][y]; puzzle[x][y]=0; }
        int _x,_y;
        for(int hint=1;hint<10;hint++){
            found = false;
            for(int X=0;X<9;X++)
                if((!found)&&(X!=x)&&((puzzle[X][y]==hint)||(user[X][y]==hint)))
                    found = true;
            for(int Y=0;Y<9;Y++)
                if((!found)&&(Y!=y)&&((puzzle[x][Y]==hint)||(user[x][Y]==hint)))
                    found = true;
            if(!found){
                if(0<x+1&&x+1<4&&0<y+1&&y+1<4){_x=0;_y=0;} 
                else if(3<x+1&&x+1<7&&0<y+1&&y+1<4){_x=3;_y=0;} 
                else if(6<x+1&&x+1<10&&0<y+1&&y+1<4){_x=6;_y=0;} 
                else if(0<x+1&&x+1<4&&3<y+1&&y+1<7){_x=0;_y=3;} 
                else if(3<x+1&&x+1<7&&3<y+1&&y+1<7){_x=3;_y=3;} 
                else if(6<x+1&&x+1<10&&3<y+1&&y+1<7){_x=6;_y=3;} 
                else if(0<x+1&&x+1<4&&6<y+1&&y+1<10){_x=0;_y=6;} 
                else if(3<x+1&&x+1<7&&6<y+1&&y+1<10){_x=3;_y=6;} 
                else{_x=6;_y=6;}
                if (puzzle[0+_x][0+_y]==hint||puzzle[1+_x][0+_y]==hint||puzzle[2+_x][0+_y]==hint||
                    puzzle[0+_x][1+_y]==hint||puzzle[1+_x][1+_y]==hint||puzzle[2+_x][1+_y]==hint||
                    puzzle[0+_x][2+_y]==hint||puzzle[1+_x][2+_y]==hint||puzzle[2+_x][2+_y]==hint)
                    found = true;
                if(!found&&(
                        user[0+_x][0+_y]==hint||user[1+_x][0+_y]==hint||user[2+_x][0+_y]==hint||
                        user[0+_x][1+_y]==hint||user[1+_x][1+_y]==hint||user[2+_x][1+_y]==hint||
                        user[0+_x][2+_y]==hint||user[1+_x][2+_y]==hint||user[2+_x][2+_y]==hint))
                    found = true;
            }
            if(!found&&user[x][y]!=hint) hint(hint);
        }
        user[x][y] = curCell;
        if(editMode) puzzle[x][y] = curGiven;
    }

    /*
     * Command Actions
     */

    public void commandAction(Command command, Displayable displayable) {
        if (command == exitCommand) caller.getDisplay().setCurrent(caller.get_AntSudoku());
        else if (command == get_solveCommand()) Solve();
        else if (command == get_restartCommand()) Restart();
        else if (command == get_doneCommand()) doneEditing();
        else if (command == get_exitEditListCommand()) caller.getDisplay().setCurrent(this);
        else if (command == get_submitEditListCommand()){
            if(getEditList().getSelectedIndex()==1){ puzzle = null; puzzle = getEmptyTable(); curX = 1; curY=1; }
            caller.getDisplay().setCurrent(this);
        }        
    }
    
    protected void keyPressed(int keyCode) { 
        boolean sortFlag = false;
        if(solved) return;
        if(writeMode) switch (keyCode) {
            case KEY_NUM9:
                editCell(9);
                repaint(); sortFlag = true; break; 
            case KEY_NUM7:
                editCell(7);
                repaint(); sortFlag = true; break; 
            case KEY_NUM3:
                editCell(3);
                repaint(); sortFlag = true; break; 
            case KEY_NUM1:
                editCell(1);
                repaint(); sortFlag = true; break; 
            case KEY_NUM0:
                editCell(0);
                repaint(); sortFlag = true; break;                
        }
        if(!sortFlag) switch (getGameAction(keyCode)) {
            case UP:
                if(writeMode) editCell(2);
                else if(curY>1) --curY; else curY = 9;
                repaint(); break;
            case DOWN:
                if(writeMode) editCell(8);
                else if(curY<9) ++curY; else curY = 1;
                repaint(); break;
            case RIGHT:
                if(writeMode) editCell(6);
                else if(curX<9) ++curX; else curX = 1;
                repaint(); break;
            case LEFT:
                if(writeMode) editCell(4);
                else if(curX>1) --curX; else curX = 9;
                repaint(); break;
            case FIRE:
                if(writeMode) editCell(5);    
                else if(puzzle[curX-1][curY-1]==0||editMode) writeMode=true;
                repaint(); break;
        }
    }
    
    private void editCell(int number){
        if(editMode) puzzle[curX-1][curY-1]=number;
        else if(puzzle[curX-1][curY-1]==0) user[curX-1][curY-1]=number;
        writeMode = false;
    }
   
    private void doneEditing(){        
        if(Creator.validate(puzzle)){
            editMode = false;
            removeCommand(get_doneCommand());
            addCommand(get_solveCommand());        
        } else caller.getDisplay().setCurrent(getEditList());
    }
    
    private void Restart(){
        solved = false;
        user = null; user = getEmptyTable();
        puzzle = null; puzzle = new Creator().getPuzzle(caller.level());
        curX = 1; curY = 1;
        setTitle(caller.title());
        removeCommand(get_restartCommand());
        addCommand(get_solveCommand());        
        repaint();
    }    

    private void Solve(){
        setTitle("Solving...");
        removeCommand(get_solveCommand());            
        execTime = System.currentTimeMillis();    
        Solver Puzzle = new Solver(puzzle, this);
        Puzzle.solveit();
    }    
    
    /*
     * Called Back
     */
    
    public void showSolution(int solution[][]){
        this.solution = solution;
        solved = true;
        execTime = System.currentTimeMillis() - execTime;
        setTitle(execTime+"msec");
        addCommand(get_restartCommand());
        repaint();
    }    
    
    /*
     * Initializers
     */
    
    private List getEditList() {
        if(editList == null){
            editList = new List("Invalid Sudoku", Choice.EXCLUSIVE);
            editList.append("Continue editing", null);
            editList.append("Edit new puzzle", null);
            editList.addCommand(get_exitEditListCommand());
            editList.addCommand(get_submitEditListCommand());
            editList.setCommandListener(this);
        }
        return editList;
    }
    
    private int[][] getEmptyTable(){
        int table[][] = new int[9][9];
        for(int x=0;x<9;x++) for(int y=0;y<9;y++) table[x][y]=0;
        return table;
    }        
                
    /*
     * Command Initializers
     */    
    
    private Command get_doneCommand() {
        if (doneCommand == null) doneCommand = new Command("Done", Command.OK, 1);
        return doneCommand;
    }      
    
    private Command get_exitCommand() {
        if (exitCommand == null) exitCommand = new Command("Quit", Command.BACK, 1);
        return exitCommand;
    }
    
    private Command get_solveCommand() {
        if (solveCommand == null) solveCommand = new Command("Solve", Command.OK, 1);
        return solveCommand;
    }
    
    private Command get_restartCommand() {
        if (restartCommand == null) restartCommand = new Command("New", Command.OK, 1);
        return restartCommand;
    }
    
    private Command get_exitEditListCommand() {
        if (exitEditListCommand == null) exitEditListCommand = new Command("Back", Command.BACK, 1);
        return exitEditListCommand;
    }
    
    private Command get_submitEditListCommand() {
        if (submitEditListCommand == null) submitEditListCommand = new Command("Ok", Command.OK, 1);
        return submitEditListCommand;
    }     
    
    /*
     * Background elements
     */

    private void background(Canvas canvas){
        graphics.setColor(255,255,255);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setColor(0,0,0);
    }

    private void hints(){
        graphics.setColor(255,0,0);
        for(int i=1;i<10;i++) drawHint(i);
        graphics.setColor(0,0,0);        
    }
    
    private void title(){
        graphics.setColor(255,0,0); drawLetter(1,'A');
        graphics.setColor(0,255,0); drawLetter(2,'n');
        graphics.setColor(255,255,0); drawLetter(3,'t');
        graphics.setColor(255,0,0); drawLetter(4,'S'); drawLetter(5,'u');
        graphics.setColor(0,255,0); drawLetter(6,'d'); drawLetter(7,'o');
        graphics.setColor(255,255,0); drawLetter(8,'k'); drawLetter(9,'u');
        graphics.setColor(0,0,0);
    }
    
    private void arena(){
        graphics.setColor(0,0,0);
        graphics.setStrokeStyle(Graphics.SOLID);
        graphics.drawRect(startX, startY, 9*cellSize, 9*cellSize);
        graphics.drawLine(3*cellSize + startX, startY, 3*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(6*cellSize + startX, startY, 6*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(startX, 3*cellSize + startY, 9*cellSize + startX, 3*cellSize + startY);
        graphics.drawLine(startX, 6*cellSize + startY, 9*cellSize + startX, 6*cellSize + startY);
        graphics.setStrokeStyle(Graphics.DOTTED);
        graphics.drawLine(1*cellSize + startX, startY, 1*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(2*cellSize + startX, startY, 2*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(4*cellSize + startX, startY, 4*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(5*cellSize + startX, startY, 5*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(7*cellSize + startX, startY, 7*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(8*cellSize + startX, startY, 8*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(startX, 1*cellSize + startY, 9*cellSize + startX, 1*cellSize + startY);
        graphics.drawLine(startX, 2*cellSize + startY, 9*cellSize + startX, 2*cellSize + startY);
        graphics.drawLine(startX, 4*cellSize + startY, 9*cellSize + startX, 4*cellSize + startY);
        graphics.drawLine(startX, 5*cellSize + startY, 9*cellSize + startX, 5*cellSize + startY);
        graphics.drawLine(startX, 7*cellSize + startY, 9*cellSize + startX, 7*cellSize + startY);
        graphics.drawLine(startX, 8*cellSize + startY, 9*cellSize + startX, 8*cellSize + startY);
        graphics.setStrokeStyle(Graphics.SOLID);
    }    
    
    /*
     * Cells
     */    
    
    private void input(int x,int y,int Number){
        graphics.setColor(0,0,255);
        drawCell(x,y,Number);
        graphics.setColor(0,0,0);
    }    
   
    private void given(int x,int y,int Number){
        graphics.setColor(0,0,0);
        drawCell(x,y,Number);
        graphics.setColor(0,0,0);
    }    
    
    private void red(int x,int y,int Number){
        graphics.setColor(255,0,0);
        drawCell(x,y,Number);
        graphics.setColor(0,0,0);
    }      
    
    /*
     * Selected elements
     */
        
    private void mark(int x, int y, boolean writeMode){       
        graphics.setColor(0,255,0);
        graphics.setStrokeStyle(Graphics.SOLID);
        graphics.drawLine((x-1)*cellSize + startX, startY, (x-1)*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(x*cellSize + startX, startY, x*cellSize + startX, 9*cellSize + startY);
        graphics.drawLine(startX, (y-1)*cellSize + startY, 9*cellSize + startX, (y-1)*cellSize + startY);
        graphics.drawLine(startX, y*cellSize + startY, 9*cellSize + startX, y*cellSize + startY);
        graphics.drawLine(startX+(x-1)*cellSize,startY,startX+x*cellSize,startY);
        graphics.drawLine(startX+(x-1)*cellSize,startY+9*cellSize,startX+x*cellSize,startY+9*cellSize);
        graphics.drawLine(startX,startY+(y-1)*cellSize,startX,startY+y*cellSize);
        graphics.drawLine(startX+9*cellSize,startY+(y-1)*cellSize,startX+9*cellSize,startY+y*cellSize);
        if(writeMode){
            graphics.setColor(255,255,0);
            graphics.fillRect(startX+(x-1)*cellSize,startY+(y-1)*cellSize,cellSize,cellSize);
        }
        graphics.setColor(255,0,0);
        if(0<x&&x<4&&0<y&&y<4) graphics.drawRect(startX,startY,3*cellSize,3*cellSize);
        if(3<x&&x<7&&0<y&&y<4) graphics.drawRect(startX+3*cellSize,startY,3*cellSize,3*cellSize);
        if(6<x&&x<10&&0<y&&y<4) graphics.drawRect(startX+6*cellSize,startY,3*cellSize,3*cellSize);
        if(0<x&&x<4&&3<y&&y<7) graphics.drawRect(startX,startY+3*cellSize,3*cellSize,3*cellSize);
        if(3<x&&x<7&&3<y&&y<7) graphics.drawRect(startX+3*cellSize,startY+3*cellSize,3*cellSize,3*cellSize);
        if(6<x&&x<10&&3<y&&y<7) graphics.drawRect(startX+6*cellSize,startY+3*cellSize,3*cellSize,3*cellSize);
        if(0<x&&x<4&&6<y&&y<10) graphics.drawRect(startX,startY+6*cellSize,3*cellSize,3*cellSize);
        if(3<x&&x<7&&6<y&&y<10) graphics.drawRect(startX+3*cellSize,startY+6*cellSize,3*cellSize,3*cellSize);
        if(6<x&&x<10&&6<y&&y<10) graphics.drawRect(startX+6*cellSize,startY+6*cellSize,3*cellSize,3*cellSize);
        graphics.setColor(0,0,255);
        graphics.drawRect(startX+(x-1)*cellSize,startY+(y-1)*cellSize,cellSize,cellSize);
        graphics.setColor(0,0,0);               
    }
             
    private void hint(int hint){
        graphics.setColor(0,255,0);
        drawHint(hint);
        graphics.setColor(0,0,0);
    }
    
    /*
     * Drawing
     */
    
    private void drawCell(int x,int y,int Number){
        graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        int added = 0;
        if(cellSize!=cellSize/2) added = 1;
        graphics.drawString(String.valueOf(Number), startX+((x-1)*cellSize)+(cellSize/2)+added,
                startY+(y*cellSize)-1, Graphics.HCENTER|Graphics.BASELINE);        
    }    
    
    private void drawHint(int hint){
        graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        graphics.drawString(String.valueOf(hint), startX+9*cellSize+cellSize/2, startY+(hint*cellSize)-1,
                Graphics.HCENTER|Graphics.BASELINE);        
    }
    
    private void drawLetter(int pos,char letter){
        graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        graphics.drawString(String.valueOf(letter), startX+9*cellSize+cellSize/2, startY+(pos*cellSize)-1,
                Graphics.HCENTER|Graphics.BASELINE);        
    }
                    
}
