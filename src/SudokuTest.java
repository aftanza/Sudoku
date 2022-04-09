import java.util.Random;

public class SudokuTest {
    private final int rowLength = 9;
    private final int colLength = 9;
    private final int[][][] arr = new int[rowLength][colLength][2]; //main Sudoku table
    private final int[][][] firstSolution = new int[rowLength][colLength][2]; //first solution to Sudoku
    private final int[][][] cache = new int[rowLength][colLength][2]; //Previous state of Sudoku table before deleting any permanent tiles
    private final int[] select = new int[2]; //Using a "Select" to select specific tiles in a 9x9 Sudoku. Mainly used with selectNext() and selectBefore()
    private boolean isReversing = false;
    private boolean currentIterationFinished = false;
    private int howManyHints = 0;

    private int testCount = 0;

    public SudokuTest(){
        resetTable();
    }

    /*Check row/col/3x3 box for conflicting numbers*/
    public boolean checkRow(int row, int col, int numToCheck){
        for(int i=0; i<9; ++i){
            if(i != col) {
                if (arr[row][i][0] == numToCheck)
                    return true;
            }
        }
        return false;
    }
    public boolean checkCol(int row, int col, int numToCheck){
        for(int i=0; i<9; ++i){
            if(i != row) {
                if (arr[i][col][0] == numToCheck)
                    return true;
            }
        }
        return false;
    }
    public boolean checkBox(int row, int col, int numToCheck){
        for(int i=(row/3)*3; i<(row/3)*3+3; ++i){
            for(int j=(col/3)*3; j<(col/3)*3+3; ++j){
                if(i != row && j != col){
                    if(arr[i][j][0] == numToCheck){
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    Check if there are zeroes in the table
    public boolean areThereZeroes(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] == 0)
                    return true;
            }
        }
        return false;
    }

//    Set all the changeable (arr[x][x][1] == 0) to zero
    public void setChangeableToZero(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][1] == 0)
                    arr[i][j][0] = 0;
            }
        }
    }

//    set all to 0 and changeable
    public void resetTable(){
        for(int i=0; i<rowLength; ++i){
            for(int j=0; j<colLength; ++j)
                arr[i][j][0] = arr[i][j][1] = 0;
        }
    }

    public void setPermanent(int row, int col, int x){
        arr[row][col][0] = x;
        arr[row][col][1] = 1;
    }
    public void removePermanent(int row, int col){
        saveToCache();
        arr[row][col][0] = 0;
        arr[row][col][1] = 0;
    }

    public void setTile(int row, int col, int x){
        if(arr[row][col][1] == 0)
            arr[row][col][0] = x;
    }

//    set all to non-changeable
    public void setAllNonZeroPermanent(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] != 0)
                    arr[i][j][1] = 1;
            }
        }
    }

    public void printTable(){
        for(int i=0; i<rowLength; ++i){
            for(int j=0; j<colLength; ++j){
                    System.out.printf("%3d ", arr[i][j][0]);
                if((j+1)%3 == 0)
                    System.out.print("| ");
                if(j == colLength-1)
                    System.out.println();
            }
            if((i+1)%3 == 0){
                System.out.print("-----------------------------------------\n");
            }
        }
        System.out.println();
        System.out.println();
    }

    /*Randomizer functions*/
    public void setRandomPermanents(int howManyPermanents) {
        Random rng = new Random();

        int randCol, randRow, randNum;

        for (int i = 0; i < howManyPermanents; ++i) {
            randNum = rng.nextInt(9) + 1;
            randRow = rng.nextInt(9);
            randCol = rng.nextInt(9);
            select[0] = randRow;
            select[1] = randCol;

            if (arr[randRow][randCol][0] != 0) {
                --i;
            } else if ((checkCol(randRow, randCol, randNum)) || (checkRow(randRow, randCol, randNum)) || checkBox(randRow, randCol, randNum)) {
                --i;
            } else {
                setPermanent(randRow, randCol, randNum);
            }

        }
    }
    public void setRandomDiagonal() {
        Random rng = new Random();

        int randNum;

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (i == j) {
                    select[0] = i;
                    select[1] = j;
                    randNum = rng.nextInt(9) + 1;

                    if ((checkBox(i, j, randNum))) {
                        --i;
                    } else {
                        arr[i][j][0] = randNum;
                        arr[i][j][1] = 1;
                    }
                }
            }
        }
    }
    public void setRandomX() {
        setRandomDiagonal();

        Random rng = new Random();

        int randNum;

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if ((i + j) == 8) {
                    if (i != j) {
                        select[0] = i;
                        select[1] = j;
                        randNum = rng.nextInt(9) + 1;

                        if ((checkCol(i, j, randNum)) || (checkRow(i, j, randNum)) || checkBox(i, j, randNum)) {
                            --i;
                        } else {
                            arr[i][j][0] = randNum;
                            arr[i][j][1] = 1;
                        }
                    }
                }
            }
        }
    }
    public void removeRandomPermanent(int howManyPermanents) {
        Random rng = new Random();

        int randCol, randRow;

        for (int i = 0; i < howManyPermanents; ++i) {
            randRow = rng.nextInt(9);
            randCol = rng.nextInt(9);
            select[0] = randRow;
            select[1] = randCol;

            if (arr[randRow][randCol][1] != 1) {
                --i;
            } else {
                removePermanent(randRow, randCol);
            }

        }
    }

    /*Cache functions*/
//    save current table to an array called cache
    public void saveToCache(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                cache[i][j][0] = arr[i][j][0];
            }
        }
    }
//    set array in cache as current table
    public void setCacheAsCurrent(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                arr[i][j][0] = cache[i][j][0];
            }
        }
    }
//    reset the cache
    public void clearCache(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                cache[i][j][0] = 0;
            }
        }
    }

    /*Select functions*/
    public void selectNext() {

        if (!(select[1] == 8)) {
            ++select[1];
        } else if (!(select[0] == 8)) {
            ++select[0];
            select[1] = 0;
        } else {
            currentIterationFinished = true;
        }

    }
    public void selectBefore() {

        if (!(select[1] == 0)) {
            --select[1];
        } else if (!(select[0] == 0)) {
            --select[0];
            select[1] = 8;
        } else {
            currentIterationFinished = true;
        }

    }

    /*Get current selected state*/
    public int currentSelectedNum(){
        return arr[select[0]][select[1]][0];
    }
    public int currentSelectedState(){
        return arr[select[0]][select[1]][1];
    }

    public boolean compare(SudokuTest another){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] != another.arr[i][j][0])
                    return false;
            }
        }
        return true;
    }

//    compare current table with first solution
    public boolean compareWithFirstSolution(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] != firstSolution[i][j][0])
                    return false;
            }
        }
        return true;
    }

    /*Solving functions*/
//    general solving algo
    public void solveFunction() {
        while (!currentIterationFinished) {
//            try {
//                Thread.sleep(500);
//                printTable();
//            }
//            catch (InterruptedException e){
//            }
            if (currentSelectedState() == 0) {
                ++arr[select[0]][select[1]][0];
                isReversing = false;
            } else {
                if (!isReversing) {
                    selectNext();
                } else {
                    selectBefore();
                }
                continue;
            }

            if (currentSelectedState() == 10) {
                arr[select[0]][select[1]][0] = 0;
                isReversing = true;
                selectBefore();
                continue;
            }

            isReversing = true;
            for (int i = currentSelectedNum(); i <= 9; ++i) {
                if (!(checkRow(select[0], select[1], i)) && !(checkCol(select[0], select[1], i)) && !(checkBox(select[0], select[1], i))) {
                    arr[select[0]][select[1]][0] = i;
                    isReversing = false;
                    break;
                }
            }

            if (!isReversing) {
                selectNext();
            } else {
                arr[select[0]][select[1]][0] = 0;
                selectBefore();
            }
        }
    }
//    main solving func to get first solution
    public void solveTable(){
        currentIterationFinished = false;
        isReversing = false;
        select[0] = 0;
        select[1] = 0;

        solveFunction();
    }
//    ends with either another solution or full of zeroes
    public void findOtherSolution(){
        currentIterationFinished = false;
        isReversing = true;
        select[0] = 8;
        select[1] = 8;

        solveFunction();
    }
//    save the first solution
    public void setFirstSolution(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                firstSolution[i][j][0] = arr[i][j][0];
            }
        }
    }

    public void setUpSudoku(int howManyHintsMin, int howManyHintsMax){
        boolean isDifficultEnough = false;
        howManyHints = 0;

        while (!isDifficultEnough) {
            resetTable();
            setRandomX();

            solveTable();
            setFirstSolution();
            setAllNonZeroPermanent();

            removeRandomPermanent(9);
            for (int i = 10; i <= 81; ++i) {
                setChangeableToZero();
                removeRandomPermanent(1);

                solveTable();
                if (!compareWithFirstSolution()) {
                    if (!areThereZeroes()) {
                        setCacheAsCurrent();
                        howManyHints = 81 - i + 1;
                        break;
                    }
                }

                findOtherSolution();
                if (!compareWithFirstSolution()) {
                    if (!areThereZeroes()) {
                        setCacheAsCurrent();
                        howManyHints = 81 - i + 1;
                        break;
                    }
                }
            }
            if(howManyHints <= howManyHintsMax && howManyHints >= howManyHintsMin)
                isDifficultEnough = true;


            if(++testCount == 1000){
                System.out.println("Error! Sudoku takes too long to find! Try something easier");
                break;
            }
        }
    }

    /*----------Multiplayer Part----------*/

    private boolean multiplayer = false;

    public static void main(String[] args) {
        SudokuTest game = new SudokuTest();

        game.setUpSudoku(0, 35);
        System.out.println(game.howManyHints);
        game.printTable();
    }
}