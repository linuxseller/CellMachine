import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CellMachine{
	int size = 50; // sets filler size of field
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
	
    public short[][] cells = new short[size][size]; // create cells array
    String[] rules = new String[2]; // rules array
    
    public void update_thread(){
        int count; // counter of neighbour cells alive
        short[][] update = new short[size][size];
        //check matrix
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                count = 0;
                // count neighbour cells alive
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
                    // checked aliveness
                    update[x][y] = 1;
                } else if(currCell == 1 && rules[1].contains(neighboursCount)) {
                    // checked saveness
                    update[x][y] = 1;
                } else {
                    update[x][y] = 0;
                }
            }
        }
        // redraw field
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
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
    
    public void setupField(int[][] arr){
    	for(int[] i: arr) {
    		cells[i[0]][i[1]]=1;
        }
    }
    public void printField() {
		for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
            	if(cells[x][y]==1) {
            		System.out.print("■ ");
            	} else {
            		System.out.print("□ ");
            	}
            	
            }
            System.out.println();
        }
		System.out.print("\n------------------------\n");
	}
    public CellMachine(int size, int[][] arr){
        //setting color for each cell
    	this.size = size;
        for(int i=0;i<size; i++){
            for (int j = 0; j < size; j++) {
                cells[j][i]=0;
            }
        }
        setupField(arr);
    }
}
