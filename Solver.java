/* AntSudoku v0.5
 * Copyright(C)2006 Antonios Lilis. All rights reserved.
 * Contact: antonis.lilis@gmail.com
 */

import java.util.*;

class Solver {
    
    private Column firstColumn;
    private Cell solution[];
    private int rowCount;
    private int initialSolutionIndex;
    private Arena caller;
    
    public Solver(int puzzle[][], Arena caller) {
        boolean isGiven;
        int boxrow, boxcol;
        int rowData[] = new int[4];
        int labels[] = new int[324]; for(int i=0;i<324;i++) labels[i] = i + 1;
        Column columns[] = new Column[labels.length];
        Vector givenVector = new Vector();
        for (int i = 0; i < labels.length; i++) {
            Assert(labels[i] > 0);
            columns[i] = new Column(labels[i]);
            columns[i].Right(null);
            if (i > 0) {
                columns[i].Left(columns[i - 1]);
                columns[i - 1].Right(columns[i]);
            }
        }
        firstColumn = new Column(0);
        columns[0].Left(firstColumn);
        firstColumn.Right(columns[0]);
        columns[labels.length - 1].Right(firstColumn);
        firstColumn.Left(columns[labels.length - 1]);
        solution = new Cell[labels.length];
        initialSolutionIndex = 0;
        for (int row=0;row<9;row++) for (int column=0;column<9;column++)
            for (int digit=0;digit<9;digit++) {
            Cell newRow;
            isGiven = (puzzle[row][column] == (digit + 1));
            rowData[0] = 1 + (row*9 + column);
            rowData[1] = 1 + 81 + (row*9 + digit);
            rowData[2] = 1 + 81 + 81 + (column*9 + digit);
            boxrow = row/3;
            boxcol = column/3;
            rowData[3] = 1 + 81 + 81 + 81 + ((boxrow*3 + boxcol)*9 + digit);
            newRow = addInitialRow(rowData);
            if (isGiven) givenVector.addElement(newRow);
            }
        removeInitialSolutionSet(givenVector);
        this.caller = caller;
    }
    
    public void solveit() { solveRecurse(initialSolutionIndex); }
    
    void solveRecurse(int solutionIndex) {
        Column nextColumn = (Column) firstColumn.Right();
        
        if (nextColumn != firstColumn) {
            Cell row = nextColumn.Down();
            while (row != nextColumn) {
                removeRow(row);
                solution[solutionIndex] = row;
                solveRecurse(solutionIndex + 1);
                reinsertRow(row);
                row = row.Down();
            }
        } else {
            int solutionRowNumbers[] = new int[solutionIndex];
            for (int i = 0; i < solutionIndex; i++)
                solutionRowNumbers[i] = solution[i].RowNumber() - 1;
            int solution[][] = new int[9][9];
            int p = 0;
            for(int i=0;i<81;i++) {
                int value = solutionRowNumbers[i];
                int digit, row, column;
                digit = value%9;
                value = value/9;
                column = value%9;
                value = value/9;
                row = value%9;
                solution[row][column] = digit + 1;
            }
            caller.showSolution(solution);
        }
    }
    
    void removeColumn(Column columnHead) {
        Cell scanner = columnHead.Down();
        while (scanner != columnHead) {
            Cell rowTraveller = scanner.Right();
            while (rowTraveller != scanner) {
                rowTraveller.Up().Down(rowTraveller.Down());
                rowTraveller.Down().Up(rowTraveller.Up());
                rowTraveller.Column().Minus();
                rowTraveller = rowTraveller.Right();
            }
            scanner = scanner.Down();
        }
        columnHead.Left().Right(columnHead.Right());
        columnHead.Right().Left(columnHead.Left());
    }
    
    void reinsertColumn(Column columnNode) {
        Cell scanner = columnNode.Up();
        while (scanner != columnNode) {
            Cell rowTraveller = scanner.Left();
            while (rowTraveller != scanner) {
                rowTraveller.Up().Down(rowTraveller);
                rowTraveller.Down().Up(rowTraveller);
                rowTraveller.Column().Plus();
                rowTraveller = rowTraveller.Left();
            }
            scanner = scanner.Up();
        }
        columnNode.Left().Right(columnNode);
        columnNode.Right().Left(columnNode);
    }
    
    void removeRow(Cell rowHead) {
        Cell scanner = rowHead;
        do {
            Cell next = scanner.Right();
            removeColumn(scanner.Column());
            scanner = next;
        } while (scanner != rowHead);
    }
    
    void reinsertRow(Cell rowHead) {
        Cell scanner = rowHead;
        do {
            scanner = scanner.Left();
            reinsertColumn(scanner.Column());
        } while (scanner != rowHead);
    }
    
    Cell addInitialRow(int labels[]) {
        Cell result = null;
        if (labels.length != 0) {
            Cell prev = null;
            Cell first = null;
            boolean found = false;
            rowCount = rowCount + 1;
            for (int i = 0; i < labels.length; i++) {
                Cell node = new Cell(rowCount, labels[i]);
                Column searcher;
                Assert(labels[i] > 0);
                node.Left(prev);
                node.Right(null);
                if (prev != null)
                    prev.Right(node);
                else
                    first = node;
                searcher = firstColumn;
                do {
                    if (searcher.Label() == labels[i]) {
                        node.Column(searcher);
                        searcher.Add(node);
                        found = true;
                    }
                    searcher = (Column) searcher.Right();
                } while (searcher != firstColumn);
//                if (found == false) {
//                    System.out.println("Can't find header for "+searcher.Label());
//                    Assert(found);
//                }
                prev = node;
            }
            first.Left(prev);
            prev.Right(first);
            result = first;
        }
        return(result);
    }
    
    void removeInitialSolutionSet(Vector solutions) {
        Enumeration vectorIterator = solutions.elements();
        while (vectorIterator.hasMoreElements()) {
            Cell row = (Cell) vectorIterator.nextElement();
            removeRow(row);
            solution[initialSolutionIndex++] = row;
        }
    }
    
    private void Assert(boolean b) { if(!b) System.err.println("AssertionError"); }
    
}
