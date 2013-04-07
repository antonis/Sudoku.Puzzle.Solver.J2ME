/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

class Cell {
    
    private Cell left;
    private Cell right;
    private Cell up;
    private Cell down;
    private Column column;
    private int rowNumber;
    private int label;

    public Cell(int rowNumber, int label) {
        this.Left(this);
        this.Right(this);
        this.Up(this);
        this.Down(this);
        this.rowNumber = rowNumber;
        this.label = label;
    }
    
    protected void Left(Cell node) { left = node; }
    protected Cell Left() { return(left); }

    protected void Right(Cell node) { right = node; }
    protected Cell Right() { return(right); }

    protected void Up(Cell node) { up = node; }
    protected Cell Up() { return(up); }

    protected void Down(Cell node) { down = node; }
    protected Cell Down() { return(down); }

    protected void Column(Column node) { column = node; }
    protected Column Column() { return(column); }

    protected int Label() { return(label); }

    protected int RowNumber() { return(rowNumber); }

}
