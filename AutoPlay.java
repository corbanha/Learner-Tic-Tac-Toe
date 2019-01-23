import java.util.Random;
/**
 * Write a description of class AutoPlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AutoPlay{

    public AutoPlay(){
        Computer comp = new Computer("C://TicTacToeBoards//badBoards.txt", "X");
        Board gameBoard = new Board();

        Random gen = new Random();

        int plays = 0;
        int randomWins = 0;
        int computerWins = 0;
        int catsGames = 0;
        int numPlays = 100000;

        do{
            //System.out.println("Computer Iterations: " + comp.numBadBoards() + " PLAY NUMBER: " + plays);
            gameBoard.setEntireBoard("_");
            //System.out.println(gameBoard.printBoard());
            //System.out.println(String.format("%.2f%%",((double) plays / numPlays)) * 100);
            boolean playersTurn = true;
            boolean spotsStill = true;
            do{
                if(playersTurn){
                    boolean gotGoodAnswer = false;
                    do{
                        int numRow = gen.nextInt(3);
                        int numCol = gen.nextInt(3);
                        gotGoodAnswer = true;
                        if(!gameBoard.getSpot(numRow, numCol).equals("_")){
                            gotGoodAnswer = false;
                        }else{
                            gameBoard.setSpot(numRow, numCol, "O");
                        }
                    }while(!gotGoodAnswer);
                    playersTurn = false;
                }else{
                    //System.out.println("Computers' Turn:");
                    gameBoard = comp.getGuess(gameBoard);
                    playersTurn = true;
                }
                //System.out.println(gameBoard.printBoard());

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
                //System.out.println("Cats game!");
                catsGames++;
            }else{
                //System.out.println(winner + " won!");
                if(winner.equals("O")){
                    randomWins++;
                    comp.badGame();
                }else{
                    computerWins++;
                }
            }

            plays++;
        }while(plays < numPlays);

        String report = String.format(">>>BEGIN REPORT<<<\n%-20s%6d\n%-20s%6d%10.2f%%\n%-20s%6d%10.2f%%\n%-20s%6d%10.2f%%\n>>>END REPORT<<<","Num Plays:", numPlays, 
                "Computer Wins:", computerWins, ((double) computerWins / numPlays) * 100, "Random Wins: ", randomWins, ((double) randomWins / numPlays) * 100, "Cat Wins:", 
                catsGames, (((double) catsGames / numPlays)) * 100);

        System.out.println(report);
        /*Just a fancy way to do this:
        >>>BEGIN REPORT<<<
        Num Plays:               5
        Computer Wins:           4     80.00%
        Random Wins:             0      0.00%
        Cat Wins:                1     20.00%
        >>>END REPORT<<<
         */

    }
}
