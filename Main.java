import java.util.Scanner;
/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main{
    /**
     * Constructor for objects of class Main
     */
    public Main(){
        /*
         * X = Computer
         * O = Human
         */

        Computer comp = new Computer("C://badBoards.txt", "X");
        
        Board gameBoard = new Board();
        Scanner scanner = new Scanner(System.in);

        boolean userWantsToPlay = true;

        do{
            System.out.println("Computer Iterations: " + comp.numBadBoards());
            System.out.println(gameBoard.printBoard());

            boolean playersTurn = true;
            boolean spotsStill = true;
            do{
                if(playersTurn){
                    boolean gettingUserAnswer = true;
                    do{
                        System.out.print("Row:");
                        String row = scanner.next();
                        System.out.print("Col:");
                        String col = scanner.next();
                        try{
                            int numRow = Integer.parseInt(row);
                            int numCol = Integer.parseInt(col);
                            if(numRow > gameBoard.getHeight() || numCol > gameBoard.getWidth()){
                                System.out.println("Sorry, that's out of bounds.");
                            }else{
                                if(!gameBoard.getSpot(numRow - 1, numCol -1).equals("_")){
                                    System.out.println("Sorry, that space is taken.");
                                }else{
                                    gameBoard.setSpot(numRow - 1, numCol - 1, "O");
                                    gettingUserAnswer = false;
                                }
                            }
                        }catch(Exception e){
                            System.out.println("Sorry, that won't work.");
                        }
                    }while(gettingUserAnswer);
                    playersTurn = false;
                }else{
                    System.out.println("Computers' Turn:");
                    gameBoard = comp.getGuess(gameBoard);
                    playersTurn = true;
                }
                System.out.println(gameBoard.printBoard());

                spotsStill = false;
                for(int i = 0; i < gameBoard.getHeight(); i++){
                    for(int j = 0; j < gameBoard.getWidth(); j++){
                        if(gameBoard.getSpot(i, j).equals("_")){
                            spotsStill = true;
                            break;
                        }
                    }
                }

            }while((gameBoard.calculateWinner() == null) && spotsStill);
            String winner = gameBoard.calculateWinner();
            if(winner == null){
                System.out.println("Cats game!");
            }else{
                System.out.println(winner + " won!");
                if(winner.equals("O")){
                    comp.badGame();
                }
            }

            System.out.print("\nDo you still want to play? (y/n):");
            if(scanner.next().equals("n")){
                userWantsToPlay = false;
            }else{
                System.out.println("Starting another round!\n");
                gameBoard.setEntireBoard("_");
            }

        }while(userWantsToPlay);
        comp.saveBadBoards();
        scanner.close();
        System.exit(0);
    }
}
