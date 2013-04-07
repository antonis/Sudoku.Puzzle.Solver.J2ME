/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

class Column extends Cell {
    
    private int count;

    public Column(int label) { super(0, label); count = 0; }

    protected void Plus() { ++count; }

    protected void Minus() { --count; }

    protected void Add(Cell node) {
        Cell end = Up();
        node.Up(end);
        node.Down(this);
        end.Down(node);
        this.Up(node);
        ++count;
    }  
    
}
