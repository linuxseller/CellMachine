import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CellMashine{
	public static final int SIZE = 50; // задаёт размер квадратного массива
	public static final int[][] NEIGHBOIRS = {
												{-1, -1},
									            {-1,  0},
									            {-1, +1},
									            { 0, -1},
									            { 0, +1},
									            {+1, -1},
									            {+1,  0},
									            {+1, +1}
									         };
	
    public short[][] cells = new short[SIZE][SIZE]; // создание массива клеток
    String[] rules = new String[2];
    
    //int[][] neighbours2;
    
    public void update_thread(){
        // массив относительных координат соседних клеток
        int count; // счетчик живых клеток рядом с текущей
        short[][] update = new short[SIZE][SIZE];
        //перебор матрицы
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                count = 0;
                // проверим цвет соседних клеток и посчитаем живые
                for(int inc = 0; inc < NEIGHBOIRS.length; inc++){
                    int xx = x + NEIGHBOIRS[inc][0];
                    int yy = y + NEIGHBOIRS[inc][1];
                    try{
                        if (cells[xx][yy] == 1) {
                            count++;
                        }
                    }
                    catch (Exception exception){
                        continue;
                    }
                }
                String neighboursCount = Integer.toString(count);
                int currCell = cells[x][y]; 
                if(currCell == 0 && rules[0].contains(neighboursCount)) {
                    // проверили условие появления новой жизни
                    update[x][y] = 1;
                } else if(currCell == 1 && rules[1].contains(neighboursCount)) {
                    // проверили условие выживания клетки
                    update[x][y] = 1;
                } else {
                    update[x][y] = 0;
                }
            }
        }
        // перерисуем поле
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[x][y] = update[x][y];
            }
        }
    }
    
    public void parseRule(String text){
    	System.out.println(text);
        text = text.toLowerCase(Locale.ROOT);
        Matcher matchBorn = Pattern.compile("b.+?/").matcher(text);
        matchBorn.find();
        rules[0] = text.substring(matchBorn.start(), matchBorn.end());
        
        Matcher matchSurvive = Pattern.compile("s.+?/").matcher(text);
        matchSurvive.find();
        rules[1] = text.substring(matchSurvive.start(), matchSurvive.end());
    }
    
    public void clearField(){
        for(short[] i: cells) {
            Arrays.fill(i, (short) 0);
        }
    }
    
    public void setupField(){
        // зададим исходное состояние
//        //glider
//        cells[10][20] = 1;
//        cells[10][20-1] = 1;
//        cells[10][20-2] = 1;
//        cells[10-1][20] = 1;
//        cells[10-2][20-1] = 1;
//        //blinker
//        cells[1][1] = 1;
//        cells[1][0] = 1;
//        cells[1][2] = 1;
//        //toad
//        cells[10][10] = 1;
//        cells[9][10] = 1;
//        cells[8][10] = 1;
//        cells[9][11] = 1;
//        cells[8][11] = 1;
//        cells[7][11] = 1;
//        //diehard
//        cells[15][15] = 1;
//        cells[14][15] = 1;
//        cells[15][16] = 1;
//        cells[19][16] = 1;
//        cells[20][16] = 1;
//        cells[21][16] = 1;
//        cells[20][14] = 1;
        cells[cells.length/2][cells.length/2]=1;
    }
    
    public CellMashine(){
        //setting color for each cell
        for(int i=0;i<SIZE; i++){
            for (int j = 0; j < SIZE; j++) {
                cells[j][i]=0;
            }
        }
        setupField();
    }
}
