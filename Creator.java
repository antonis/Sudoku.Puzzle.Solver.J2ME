/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

class Creator {
    
    private final static int minimumGiven = 17;
    private final int Easy = 0;
    private final int Medioum = 1;
    private final int Hard = 2;
      
    public Creator() {}
    
    protected int[][] getPuzzle(int level){
        int puzzle1[][] = {
            {0, 8, 0, 2, 0, 0, 4, 0, 0},
            {6, 0, 0, 7, 0, 1, 0, 0, 0},
            {0, 7, 3, 0, 9, 6, 0, 0, 0},
            {0, 2, 5, 0, 0, 0, 0, 1, 0},
            {4, 0, 0, 0, 0, 0, 0, 0, 9},
            {0, 9, 0, 0, 0, 0, 5, 4, 0},
            {0, 0, 0, 9, 7, 0, 1, 5, 0},
            {0, 0, 0, 6, 0, 4, 0, 0, 8},
            {0, 0, 6, 0, 0, 5, 0, 7, 0}};
        int puzzle2[][] = {
            {1, 0, 0, 9, 0, 7, 0, 0, 3},
            {0, 8, 0, 0, 0, 0, 0, 7, 0},
            {0, 0, 9, 0, 0, 0, 6, 0, 0},
            {0, 0, 7, 2, 0, 9, 4, 0, 0},
            {4, 1, 0, 0, 0, 0, 0, 9, 5},
            {0, 0, 8, 5, 0, 4, 3, 0, 0},
            {0, 0, 3, 0, 0, 0, 7, 0, 0},
            {0, 5, 0, 0, 0, 0, 0, 4, 0},
            {2, 0, 0, 8, 0, 6, 0, 0, 9}};
        int puzzle3[][] = {
            {0, 0, 0, 0, 3, 0, 7, 9, 0},
            {3, 0, 0, 0, 0, 0, 0, 0, 5},
            {0, 0, 0, 4, 0, 7, 3, 0, 6},
            {0, 5, 3, 0, 9, 4, 0, 7, 0},
            {0, 0, 0, 0, 7, 0, 0, 0, 0},
            {0, 1, 0, 8, 2, 0, 6, 4, 0},
            {7, 0, 1, 9, 0, 8, 0, 0, 0},
            {8, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 9, 4, 0, 1, 0, 0, 0, 0}};                
        if(level==Easy) return puzzle1;
        else if(level==Medioum) return puzzle2;
        else return puzzle3;
    }
    
    protected static boolean validate(int puzzle[][]){
        int countGiven = 0;
        for(int x=0;x<9;x++) for (int y=0;y<9;y++) if(puzzle[x][y]!=0) ++countGiven;
        if(countGiven<minimumGiven) return false;
        else{
            return true;
        }
    }    
    
}
