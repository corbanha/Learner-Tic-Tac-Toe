import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random;

/**
 * Write a description of class Computer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Computer{

    String fileName;
    String piece;

    Board lastBoard;
    ArrayList<Board> badBoards = new ArrayList<Board>();
    Random gen = new Random();

    /**
     * Constructor for objects of class Computer
     * @param file the file to get the badBoard information. If the file does not exist, then a new file will be created.
     */
    public Computer(String file, String piece){
    	this.piece = piece;
        try{
            fileName = file;
            File givenFile = new File(fileName);
            if(!givenFile.exists()){
                try(PrintWriter writeFile = new PrintWriter(fileName);){
                    //Not Writing anything!
                }
            }
            Scanner readFile = new Scanner(givenFile);

            while(readFile.hasNext()){
                Board badBoard = new Board();
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        badBoard.setSpot(i, j, readFile.next());
                    }
                }
                badBoards.add(badBoard);
            }
            readFile.close();
        }catch(FileNotFoundException e){
            System.out.println("Class: Computer\nMethod: Computer(String file)\n" + e);
        }catch(Exception e){
            System.out.println("Class: Computer\nMethod: Computer(String file)\n" + e);
        }
    }

    public void saveBadBoards(){
        if(badBoards.size() >= 1){
            //NEEDS TO BE FIXED!!!
            try(PrintWriter writeFile = new PrintWriter(fileName)){
                for(Board b : badBoards){
                    for(int i = 0; i < b.getHeight(); i++){
                        for(int j = 0; j < b.getWidth(); j++){
                            writeFile.print(" " + b.getSpot(i, j));
                        }
                    }
                    writeFile.println();
                }
            }catch(FileNotFoundException e){
                System.out.println(e);
            }
            if(badBoards.size() % 1000 == 0){
                String backUpFileName = "C://TicTacToeBoards//BackUpFileBoard-" + badBoards.size() + ".txt";
                try(PrintWriter writeFile = new PrintWriter(backUpFileName)){
                    for(Board b : badBoards){
                        for(int i = 0; i < b.getHeight(); i++){
                            for(int j = 0; j < b.getWidth(); j++){
                                writeFile.print(" " + b.getSpot(i, j));
                            }
                        }
                        writeFile.println();
                    }
                }catch(FileNotFoundException e){
                    System.out.println(e);
                }
                String oldFileName = "C://TicTacToeBoards//BackUpFileBoard-" + (badBoards.size() - 1000) + ".txt";
                File oldBackUp = new File(oldFileName);
                if(oldBackUp.exists()){
                    oldBackUp.delete();
                }
            }
        }
    }

    public String printBadBoards(){
        String str = "";
        for(Board b : badBoards){
            str += b.printBoard() + "\n";
        }
        return str;
    }

    public int numBadBoards(){
        return badBoards.size();
    }

    public Board getGuess(Board b){
        //lastBoard = b.getCloneOf();
        Board testingBoard = b.getCloneOf();
        int row;
        int col;
        int numTries = 0;
        boolean deciding = true;
        while(deciding){
            row = gen.nextInt(testingBoard.getHeight());
            col = gen.nextInt(testingBoard.getWidth());
            if(testingBoard.getSpot(row, col).equals("_")){
                numTries++;
                testingBoard.setSpot(row, col, piece);
                boolean isBad = false;
                for(Board bor : badBoards){
                    if(testingBoard.equals(bor)){
                        isBad = true;
                        break;
                    }
                }
                if(!isBad){
                    deciding = false;
                    break;
                }else if(isBad && numTries > 15){
                    deciding = false;
                    break;
                }else{
                    testingBoard = b.getCloneOf();
                }
            }
        }
        lastBoard = testingBoard.getCloneOf();
        return testingBoard;
    }
    
    public int removeDuplicateBadBoards(){
    	 int numBoardsRemoved = 0;
    	 for(int d = 0; d < 5; d++){
    		 for(int i = 0; i < badBoards.size(); i++){
	         	for(int j = i; j < badBoards.size(); j++){
	         		if(badBoards.get(i).equals(badBoards.get(j))){
	         			badBoards.remove(j);
	         			numBoardsRemoved++;
	         			j--;
	         		}
	         	}
	         }
    	 }
         saveBadBoards();
         return numBoardsRemoved;
    }

    public void badGame(){
        badBoards.add(lastBoard);
        //System.out.println("\nBad Boards:");
        //System.out.println(printBadBoards());
        saveBadBoards();
    }
}
