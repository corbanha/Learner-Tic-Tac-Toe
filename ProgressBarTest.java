import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.beans.*;
import java.util.Random;

import javax.swing.text.DefaultCaret;

public class ProgressBarTest extends JPanel implements ActionListener, ItemListener, PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
    private JProgressBar subProgressBar;
    private JCheckBox checkBox; 
    private JButton startButton;
    private JButton stopButton;
    private JTextArea taskOutput;
    private Task task;
    private static JFrame frame;
    public static int numPlays = 50;
    private int plays = 0;
    private long timeElapsed;
    
    private int previousNum = 500;
    private int numBoardsRemoved = 0;
    private int numBoardsAdded = 0;

    private boolean showTicTacToe = false;
    private boolean stopComputing = false;

    class Task extends SwingWorker<Void, Void> {
        String reportStart;
        String reportComputer;
        String reportRandom;
        String reportCat;

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            /*
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
            //Sleep for up to one second.
            try {
            Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException ignore) {}
            //Make random progress.
            progress += random.nextInt(10);
            setProgress(Math.min(progress, 100));
            }
             */
            setProgress(0);
            Computer comp = new Computer("C://TicTacToeBoards//badBoards.txt", "X");
            Board gameBoard = new Board();

            Random gen = new Random();

            plays = 0;
            int randomWins = 0;
            int computerWins = 0;
            int catsGames = 0;

            long startTime = System.currentTimeMillis();
            do{
                //System.out.println("Computer Iterations: " + comp.numBadBoards() + " PLAY NUMBER: " + plays);
                gameBoard.setEntireBoard("_");
                //System.out.println(gameBoard.printBoard());
                int progress = (int) Math.round((((double) plays / numPlays)) * 100);
                setProgress(progress);
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
                plays++;
                if(!showTicTacToe){
                    int playsToShow = plays + 1;
                    if(playsToShow > numPlays){
                        playsToShow = numPlays;
                    }
                    taskOutput.setText("Computing " + playsToShow + "/" + numPlays + String.format(" (%.02f", ((playsToShow * 100.0) / numPlays)) + "%)" +
                    		"\nTime Elapsed: " + millsToString(System.currentTimeMillis() - startTime) + 
                    		"\nEstimated Time Left: " + millsToStringOnlyTopString((long) (((System.currentTimeMillis() - startTime) / (plays * 1.0)) * numPlays) - 
                    				(System.currentTimeMillis() - startTime)) + "\nPlease Be Patient.");
                }

                if(winner == null){
                    //System.out.println("Cats game!");
                    catsGames++;
                    if(showTicTacToe){	
                        taskOutput.append("Game " + plays + "\n" + gameBoard.printBoard() + "Cats Game\n\n");
                    }
                }else{
                    //System.out.println(winner + " won!");
                    if(winner.equals("O")){
                        randomWins++;
                        if(showTicTacToe){	
                            taskOutput.append("Game " + plays + "\n" + gameBoard.printBoard() + "Random Win\n\n");
                        }
                        comp.badGame();
                        numBoardsAdded++;
                    }else{
                        if(showTicTacToe){	
                            taskOutput.append("Game " + plays + "\n" + gameBoard.printBoard() + "Computer Win\n\n");
                        }
                        computerWins++;
                    }
                }

            }while(plays < numPlays && !stopComputing);
            //             report = String.format("%-25s  %6d\n%-25s%6d%10.2f%%\n%-25s%6d%10.2f%%\n%-25s   %6d%10.2f%%","Num Plays:", numPlays, 
            //                     "Computer Wins:", computerWins, ((double) computerWins / numPlays) * 100, "Random Wins:", randomWins,
            //                     ((double) randomWins / numPlays) * 100, "Cat Wins:", 
            //                     catsGames, (((double) catsGames / numPlays)) * 100);

            numBoardsRemoved = comp.removeDuplicateBadBoards();
            
            reportStart = String.format("%-25s%6d", "Num Plays:", plays);
            reportComputer = String.format("\n%-25s%6d%10.2f%%","Computer Wins:", computerWins, ((double) computerWins / plays) * 100);
            reportRandom = String.format("\n%-25s%6d%10.2f%%","Random Wins:", randomWins, ((double) randomWins / plays) * 100);
            reportCat = String.format("\n%-25s%6d%10.2f%%", "Cats Games:", catsGames, ((double) catsGames / plays) * 100);
            timeElapsed = System.currentTimeMillis() - startTime;
            //System.out.println(report);
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            setProgress(100);
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            checkBox.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            if(!showTicTacToe){
            	taskOutput.setText("");
            }else{
            	taskOutput.append("x : Computer\nO : Random\n\n");
            }
            taskOutput.append("Time Taken: " + millsToString(timeElapsed) + "\n");
            taskOutput.append(reportStart);
            taskOutput.append(reportComputer);
            taskOutput.append(reportRandom);
            taskOutput.append(reportCat);
            taskOutput.append("\nNum Boards Learned: " + (numBoardsAdded - numBoardsRemoved));
            if(showTicTacToe) taskOutput.append("\n\nScroll up to see games\n\n\n\n\n\n");
        }
    }

    public ProgressBarTest() {
        super(new BorderLayout());

        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        
        stopButton = new JButton("Stop");
        stopButton.setActionCommand("stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setIndeterminate(false);

        subProgressBar = new JProgressBar(0, 100);
        subProgressBar.setValue(0);
        subProgressBar.setStringPainted(false);

        checkBox = new JCheckBox("Show Boards Being Played");

        checkBox.setSelected(false);
        checkBox.addItemListener(this);
        //UIManager.put("ProgressBar.background", Color.orange);
        //UIManager.put("ProgressBar.foreground", Color.blue);
        //UIManager.put("ProgressBar.selectionBackground", Color.red);
        //UIManager.put("ProgressBar.selectionForeground", Color.green);

        taskOutput = new JTextArea(15, 50);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        taskOutput.setLineWrap(true);
        taskOutput.setText("This Program runs random tic tac toe games\nagainst a computer.\nThis program was designed in 2014 by " + 
            "Corban A.\nPress the start button to begin.\nVersion: 0.9.8");

        DefaultCaret caret = (DefaultCaret)taskOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton, BorderLayout.CENTER);
        buttonPanel.add(stopButton, BorderLayout.CENTER);
        buttonPanel.add(progressBar, BorderLayout.NORTH);

        add(buttonPanel, BorderLayout.NORTH);
        add(checkBox, BorderLayout.SOUTH);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public void itemStateChanged(ItemEvent e){
        Object source = e.getItemSelectable();
        if(source == checkBox){
            showTicTacToe = !showTicTacToe;
        }
    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
    	if(evt.getSource() == startButton){
    		startButton.setEnabled(false);
            stopButton.setEnabled(true);
            checkBox.setEnabled(false);
            progressBar.setValue(0);
            stopComputing = false;
            try{
                String input = JOptionPane.showInputDialog(frame, "How many games should be tested?", "" + previousNum);
                if(!(Integer.parseInt(input) == JOptionPane.CANCEL_OPTION)){
                    numPlays = Integer.parseInt(input);
                    previousNum = numPlays;
                    if(numPlays > 0){
                        task = new Task();
                        task.addPropertyChangeListener(this);
                        task.execute();
                        if(!showTicTacToe){
                            taskOutput.setText("Computing " + numPlays + " Tic Tac Toe Games.\nPlease Be Patient.");
                        }else{
                            taskOutput.setText("");
                        }
                    }else{
                        JOptionPane.showMessageDialog(frame, "Please enter a number greater than 0", "Valid Number Required", JOptionPane.PLAIN_MESSAGE);
                        startButton.setEnabled(true);
                        startButton.setSelected(true);
                        stopButton.setEnabled(false);
                        checkBox.setEnabled(true);
                    }
                }else{//pressed cancel
                    startButton.setEnabled(true);
                    startButton.setSelected(true);
                    stopButton.setEnabled(false);
                    checkBox.setEnabled(true);
                }
            }catch(Exception e){
                //JOptionPane.showMessageDialog(frame, "Please enter a number greater than 0", "Valid Number Required", JOptionPane.PLAIN_MESSAGE);
                startButton.setEnabled(true);
                startButton.setSelected(true);
                stopButton.setEnabled(false);
                checkBox.setEnabled(true);
            }
    	}else if(evt.getSource() == stopButton){
    		stopComputing = true;
    	}
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            //taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        } 
    }

    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Auto Tic Tac Toe Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBarTest();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Class not found!"); 
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }
    
    public static String millsToString(long mills){
    	long sec = (mills / 1000) % 60;
		long min = (mills / (1000 * 60)) % 60;
		long hour = (mills / (1000 * 60 * 60)) % 24;
		long day = (mills / (1000 * 60 * 60 * 24));
		if(day != 0) return String.format("%01d:%02d:%02d:%02d.%03d", day, hour, min, sec, mills % 1000);
		if(hour != 0) return String.format("%02d:%02d:%02d.%03d", hour, min, sec, mills % 1000);
		if(min != 0) return String.format("%02d:%02d.%03d", min, sec, mills % 1000);
		return String.format("%02d.%03d", sec, mills % 1000);
    }
    
    public static String millsToStringOnlyTopString(long mills){
    	long sec = (mills / 1000) % 60;
		long min = (mills / (1000 * 60)) % 60;
		long hour = (mills / (1000 * 60 * 60)) % 24;
		long day = (mills / (1000 * 60 * 60 * 24));
		if(day != 0){
			if(day == 1) return String.format("~%d day", day, hour, min, sec, mills % 1000);
			return String.format("~%d days", day, hour, min, sec, mills % 1000);
		}
		if(hour != 0){
			if(hour == 1) return String.format("~%d hour", hour, min, sec, mills % 1000);
			return String.format("~%d hours", hour, min, sec, mills % 1000);
		}
		if(min != 0){
			if(min == 1) return String.format("~%d minute", min, sec, mills % 1000);
			return String.format("~%d minutes", min, sec, mills % 1000);
		}
		if(sec != 0){
			if(sec == 1) return String.format("~%d second", sec, mills % 1000);
			return String.format("~%d seconds", sec, mills % 1000);
		}
		return String.format("Less than 1 second", sec, mills % 1000);
    }
}