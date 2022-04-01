import java.util.Random;

public class SudokuTest {
    private final int rowLength = 9;
    private final int colLength = 9;
    private int[][][] arr = new int[rowLength][colLength][2];
    private int[][][] firstSolution = new int[rowLength][colLength][2];
    private int[][][] cache = new int[rowLength][colLength][2];
    private int[] select = new int[2];
    private boolean isReversing = false;
    private boolean currentIterationFinished = false;

    public SudokuTest(){
        resetTable();
    }

    public boolean checkRow(int row, int numToCheck){
        for(int i=0; i<9; ++i){
            if(i != select[1]) {
                if (arr[row][i][0] == numToCheck)
                    return true;
            }
        }
        return false;
    }

    public boolean checkCol(int col, int numToCheck){
        for(int i=0; i<9; ++i){
            if(i != select[0]) {
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

    public boolean areThereZeroes(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] == 0)
                    return true;
            }
        }
        return false;
    }

    public void setChangeableToZero(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][1] == 0)
                    arr[i][j][0] = 0;
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

    public void setRandomPermanents(int howManyPermanents){
        Random rng = new Random();

        int randCol, randRow, randNum;

        for(int i=0; i<howManyPermanents; ++i){
            randNum = rng.nextInt(9)+1;
            randRow = rng.nextInt(9);
            randCol = rng.nextInt(9);
            select[0] = randRow;
            select[1] = randCol;

            if(arr[randRow][randCol][0] != 0) {
                --i;
            }
            else if( (checkCol(randCol, randNum)) || (checkRow(randRow, randNum)) || checkBox(randRow, randCol, randNum) ){
                --i;
            }
            else {
                setPermanent(randRow, randCol,  randNum);
            }

        }
    }

    public void removeRandomPermanent(int howManyPermanents){
        Random rng = new Random();

        int randCol, randRow;

        for(int i=0; i<howManyPermanents; ++i){
            randRow = rng.nextInt(9);
            randCol = rng.nextInt(9);
            select[0] = randRow;
            select[1] = randCol;

            if(arr[randRow][randCol][1] != 1) {
                --i;
            }
            else {
                removePermanent(randRow, randCol);
            }

        }
    }

    public void saveToCache(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                cache[i][j][0] = arr[i][j][0];
            }
        }
    }

    public void setCacheAsCurrent(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                arr[i][j][0] = cache[i][j][0];
            }
        }
    }

    public void clearCache(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                cache[i][j][0] = 0;
            }
        }
    }

    public void setAllPermanent(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                arr[i][j][1] = 1;
            }
        }
    }

    public void setRandomDiagonal(){
        Random rng = new Random();

        int randNum;

        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(i == j){
                    select[0] = i;
                    select[1] = j;
                    randNum = rng.nextInt(9) + 1;

                    if( (checkBox(i, j, randNum)) ){
                        --i;
                    }
                    else {
                        arr[i][j][0] = randNum;
                        arr[i][j][1] = 1;
                    }
                }
            }
        }
    }

    public void setRandomX(){
        setRandomDiagonal();

        Random rng = new Random();

        int randNum;

        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if( (i + j) == 8){
                    if(i != j) {
                        select[0] = i;
                        select[1] = j;
                        randNum = rng.nextInt(9) + 1;

                        if( (checkCol(j, randNum)) || (checkRow(i, randNum)) || checkBox(i, j, randNum) ){
                            --i;
                        }
                        else {
                            arr[i][j][0] = randNum;
                            arr[i][j][1] = 1;
                        }
                    }
                }
            }
        }
    }

    public void selectNext(){

        if(!(select[1] == 8))
            ++select[1];
        else if(!(select[0] == 8)){
            ++select[0];
            select[1] = 0;
        }
        else{
            currentIterationFinished = true;
        }

    }

    public void selectBefore(){

        if(!(select[1] == 0))
            --select[1];
        else if(!(select[0] == 0)){
            --select[0];
            select[1] = 8;
        }
        else{
            currentIterationFinished = true;
        }

    }

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

    public boolean compareWithFirstSolution(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                if(arr[i][j][0] != firstSolution[i][j][0])
                    return false;
            }
        }
        return true;
    }

    public void setFirstSolution(){
        for(int i=0; i<9; ++i){
            for(int j=0; j<9; ++j){
                firstSolution[i][j][0] = arr[i][j][0];
            }
        }
    }

    private void solveFunction(){
        while (!currentIterationFinished) {
//            printTable();

            if (currentSelectedState() == 0) {
                ++arr[select[0]][select[1]][0];
                isReversing = false;
            }
            else {
                if (!isReversing) {
                    selectNext();
                }
                else {
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
                if (!(checkRow(select[0], i)) && !(checkCol(select[1], i)) && !(checkBox(select[0], select[1], i))) {
                    arr[select[0]][select[1]][0] = i;
                    isReversing = false;
                    break;
                }
            }

            if (!isReversing)
                selectNext();
            else {
                arr[select[0]][select[1]][0] = 0;
                selectBefore();
            }
        }
    }

    public void solveTable(){
        currentIterationFinished = false;
        isReversing = false;
        select[0] = 0;
        select[1] = 0;

        solveFunction();
//        try {
//            Thread.sleep(500);
//            printTable();
//        }
//        catch (InterruptedException e){
//        }


    }

    //ends with either another solution or full of zeroes
    public void findOtherSolution(){
        currentIterationFinished = false;
        isReversing = true;
        select[0] = 8;
        select[1] = 8;

        solveFunction();
    }
//gsdkjfdgjhb
    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//        int x = 0;

        SudokuTest game = new SudokuTest();
//        game.printTable();
//        game.setRandomPermanents(17);
//        game.setRandomDiagonal();
        game.setRandomX();
        game.solveTable();
        game.printTable();
        game.setFirstSolution();
        game.setAllPermanent();

        for(int i=1; i<=81; ++i){
            game.setChangeableToZero();
            game.removeRandomPermanent(1);
//            game.printTable();

            game.solveTable();
//            game.printTable();

            game.findOtherSolution();
//            game.printTable();

            if(!game.compareWithFirstSolution()){
                if(!game.areThereZeroes()){
                    game.setCacheAsCurrent();
//                    game.printTable();
                    System.out.println((81-i+1));
                    break;
                }
            }
        }

        game.printTable();

    }
}