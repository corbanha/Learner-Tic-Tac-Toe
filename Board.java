
/**
 * This class will emulate a simple TicTacToeBoard
 * 
 * @author Corban Anderson
 * @version 4/14/14
 */
public class Board implements Cloneable{
    private String board [] [];
    private int width, height;
    /**
     * Constructor for objects of class Board
     */
    public Board(){
        width = 3;
        height = 3;
        board = new String[width] [height]; 
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board[1].length; j++){
                board[i][j] = "_";
            }
        }
    }

    public void setEntireBoard(String set){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board[1].length; j++){
                board[i][j] = set;
            }
        }
    }
    
    public String calculateWinner(){
        String start = "";
        //Horizantally
        boolean equalsHorizantal = true;
        for(int i = 0; i < board[0].length; i++){
            start = board [i] [0];
            equalsHorizantal = true;
            if(!start.equals("_")){
                for(int j = 1; j < board[1].length; j++){
                    if(!board [i] [j].equals(start)){
                        equalsHorizantal = false;
                        break;
                    }
                }
            }else{
                equalsHorizantal = false;
            }
            if(equalsHorizantal){
                //System.out.println("Horizantal win!");
                return start;
            }
        }

        //Vertically
        boolean equalsVertical = true;
        String startVertical = "";
        for(int j = 0; j < board[1].length; j++){
            startVertical = board [0] [j];
            equalsVertical = true;
            if(!startVertical.equals("_")){
                for(int i = 1; i < board[0].length; i++){
                    if(!board [i] [j].equals(startVertical)){
                        equalsVertical = false;
                        break;
                    }
                }
            }else{
                equalsVertical = false;
            }
            if(equalsVertical){
                //System.out.println("Vertical win!");
                return startVertical;
            }
        }
        //Diaganally
        boolean equalsDiagonalLeft = true;
        String startDiagonalLeft = board[0][0];
        if(!startDiagonalLeft.equals("_")){
            for(int a = 1; a < board[0].length; a++){
                if(!board[a][a].equals(startDiagonalLeft)){
                    equalsDiagonalLeft = false;
                    break;
                }
            }
        }else{
            equalsDiagonalLeft = false;
        }
        if(equalsDiagonalLeft){
            //System.out.println("Left Diagonal win!");
            return startDiagonalLeft;
        }
        
        boolean equalsDiagonalRight = true;
        String startDiagonalRight = board[0][board[1].length - 1];//far right corner
        if(!startDiagonalRight.equals("_")){
            for(int a = board[1].length - 2, b = 1; a >= 0; a--, b++){
                if(!board[b][a].equals(startDiagonalRight)){
                    equalsDiagonalRight = false;
                    break;
                }
            }
        }else{
            equalsDiagonalRight = false;
        }
        if(equalsDiagonalRight){
            //System.out.println("Right Diagonal win!");
            return startDiagonalRight;
        }

        return null;
    }

    public void setSpot(int row, int col, String set){
        board [row] [col] = set;
    }

    public String printBoard(){
        String printBoard = "";
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board[1].length; j++){
                printBoard += " " + board[i] [j];
            }
            printBoard += "\n";
        }
        return printBoard;
    }

    public boolean equals(Board otherBoard){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board[1].length; j++){
                if(!board[i] [j].equals(otherBoard.getSpot(i, j))){
                    return false;
                }
            }
        }
        return true;
    }

    public String getSpot(int row, int col){
        return board[row] [col];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
    
    public Board getCloneOf(){
        Board sendBoard = new Board();
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board[1].length; j++){
                sendBoard.setSpot(i, j, board[i][j]);
            }
        }
        return sendBoard;
    }
}
