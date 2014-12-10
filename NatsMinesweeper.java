// Nat Mayer
// Final Project
// My own version of minesweeper
// M755b
// Version 1.0
// 24 May 2012

import java.util.*; //For Scanner, ArrayList, and Timer
import java.awt.*; //For Color, Graphics, GridLayout, and Font
import java.awt.event.*;//For MouseAdapter
import javax.swing.*; //For JFrame, JPanel, and other miscellaneous graphics classes

public class NatsMinesweeper extends JFrame
{
	/**The display keeping track of the number of mines to be found. */
   private MineCounter mineCounter;
      
	/**The button used to reset the game. */    
	private ResetButton resetButton;
      
   /**The display showing the time elapsed so far this game. */
   private TimerDisplay timerDisplay;
      
   /**The grid of squares forming the game board */
   private GameSquare[][] grid;
      
   /**The total number of mines on the game board (constant defined by the mode). */ 
   private int numMines;
      
  	/**The number of squares revealed so far this game. */
   private int numRevealed;
      
	/**Whether or not the game has been won. Cannot be true if gameLost is true. */
   private boolean gameWon;
      
  	/**Whether or not the game has been lost. Cannot be true if gameWon is true. */
   private boolean gameLost;
      
  	/**The number of correctly flagged mines so far. Used to end the game when all have been found */
   private int minesFound;
   
   /**Defines the three preset difficulty modes of the game. */
   private enum Mode
   {
      EASY (9, 9, 10),
      INTERMEDIATE (16, 16, 40),
      HARD (16, 30, 99);
      
   	/**The height of the game board in this mode. */
      private final int height;
         
    	/**The width of the game board in this mode. */
      private final int width;
         
      /**The number of mines to be placed on the game board in this mode. */
      private final int mines;
      
      /**Constructs a Mode constant with the desired constants. Called once for each Enum constant above.
     	 *
       *@param height The height of the game board in this mode
       *@param width The width of the game board in this mode
       *@param mines The number of mines to be placed on the game board in this mode
       */
      Mode(int height, int width, int mines)
      {
         this.height = height;
         this.width = width;
         this.mines = mines;
      }
      	
      /**Returns the height for this mode.
       *
       *@return the height for this mode.
       */
      int getHeight() {return height;}
            
      /**Returns the width for this mode.
        *
        *@return the width for this mode.
        */
      int getWidth() {return width;}
         
      /**Returns the number of mines for this mode.
        *
        *@return the number of mines for this mode.
        */
      int getMines() {return mines;}
		
		/**Returns the name of this mode, with only the first letter capitalized.
		  *
		  *@return the name of this mode, with only the first letter capitalized.
		  */
		public String toString() {return name().charAt(0) + name().substring(1).toLowerCase();}
	}
   	
 	//For convenience, all constants related to graphical display for any of the objects are here in the outer class.
  	//All sizes are measured in pixels.
   	
  	/**The width of the margins of the game window. */
   private static final int MARGIN_SIZE = 5;
      
  	/**The side length of the GameSquares. */
   private static final int SQUARE_SIZE = 15;
      
  	/**The height of the two displays (MineCounter and TimerDisplay). */
   private static final int H = 20;
      
  	/**The width of the two displays. */
   private static final int W = 40;
      
   /**The height of the built in title bar of the window. */
   private static final int TITLE_BAR_HEIGHT = 22;
      
   /**The distance text should be shifted to the right so it doesn't begin precisely at the left edge of a panel. */
   private static final int SHIFT = 3;	
      
   /**The number of digits shown	on the displays. */
   private static final int DIGITS = 3;
      
   /**The font size for the numbers on the displays. */
   private static final int FONT_SIZE = 18;
      
   /**The starting angle for the smile (or frown) on the ResetButton, in degrees. */
   private static final int SMILE_START = 30;
      
   /**The angle length for the smile (or frown) on the ResetButton, in degrees. */
   private static final int SMILE_LENGTH = 120;
      
   /**The diameter of the eyes of the smile on the ResetButton. */
   private static final int EYE_SIZE = 3;
      
   /**The width of the 'x' shown as an eye on the ResetButton after the game is lost. */
   private static final int DEAD_EYE_SIZE = 4;
      
   /**The width of the oval lenses of the sunglasses on the game-won ResetButton. */
   private static final int GLASSES_WIDTH = 6;
      
   /**The height of the oval lenses of the sunglasses on the game-won ResetButton. */
   private static final int GLASSES_HEIGHT = 4;
      
   /**The x-coordinates of the triangle at the base of the flag icon, relative to the origin of the GameSquare. */
   private static final int[] FLAG_BASE_X = {1, SQUARE_SIZE - 1, SQUARE_SIZE/2 + 1};
      
   /**The y-coordinates of the triangle at the base of the flag icon. */
   private static final int[] FLAG_BASE_Y = {SQUARE_SIZE - 2, SQUARE_SIZE - 2, SQUARE_SIZE - 5};
      
   /**The length of the flag pole on the flag icon. */
   private static final int POLE_LENGTH = 8;
      
   /**The x-coordinates of the actual flag on the flag icon. */
   private static final int[] FLAG_X = {FLAG_BASE_X[2], FLAG_BASE_X[2], FLAG_BASE_X[0]};
      
   /**The y-coordinates of the actual flag on the flag icon. */
   private static final int[] FLAG_Y = {FLAG_BASE_Y[2] - POLE_LENGTH, 
         										FLAG_BASE_Y[2] - POLE_LENGTH/3, 
         										FLAG_BASE_Y[2] - 2*POLE_LENGTH/3};
         								
   /**The colors of the different numbers. The number n will appear in color COLORS[n-1]. */
   private static final Color[] COLORS = {Color.BLUE, Color.GREEN, Color.RED, 
         											Color.BLUE.darker().darker(), Color.RED.darker().darker(), 
														Color.CYAN, Color.MAGENTA.darker(), Color.BLACK};
	
	/**The x and y coordinates of the spot of glare on the mine icon, relative to the origin of the GameSquare. */
	private static final int GLARE_LOC = 5;
	
	/**The length and width of the spot of glare on the mine icon. */
	private static final int GLARE_SIZE = 2;
   
   /**This is where it all begins. Takes user input to determine the size of the board and
     *the number of mines to be placed, then creates a NatsMinesweper object with the correct
     *settings. All other setup takes place in the constructor.
     */
   public static void main(String[] args)
   {
		Mode m = Mode.values()[JOptionPane.showOptionDialog(null, 
												"Which mode would you like to play?", "Difficulty Selection",
												JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
												null, Mode.values(), Mode.HARD)];
		NatsMinesweeper game = new NatsMinesweeper(m.getHeight(), m.getWidth(), m.getMines());
   }
   	
   /**Takes integer input in a sepcified inclusive range from the console, 
     *reprompting if the input is invalid.
     *
     *@param console The Scanner used to take the input
     *@param min The minimum allowed value for the input
     *@param max The maximimum allowed value for the input
     *@return The input integer
     */
   public static int getIntInput(Scanner console, int min, int max)
   {
      if (console.hasNextInt())
      {
         int num = console.nextInt();
         console.nextLine();
         if (min <= num && num <= max)
         {
            return num;
         }
      }
      console.nextLine();
      System.out.print("Invalid input. Please enter an integer between " + min + " and " + max +": ");
      return getIntInput(console, min, max);
   }
   
   /**Constructs a NatsMinesweeper object, creating all of the other objects needed to play
	  *the game. The locations of the mines are not set until the game begins, to ensure that
	  *the first click is safe.
	  *
	  *@param height The height of the game board
	  *@param width The width of the game board
	  *@param numMines The number of mines to be placed on the game board
	  */
   public NatsMinesweeper(int height, int width, int numMines)
   {
      //Create and set up the window.
      super("Nat's Minesweeper");
      setSize(2*MARGIN_SIZE + width*SQUARE_SIZE, 
         	TITLE_BAR_HEIGHT + 3*MARGIN_SIZE + H + height*SQUARE_SIZE);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setResizable(false);
      gameWon = false;
      gameLost = false;
   	
   	//Create and set up a blank panel to fill the window, on which all other panels will be placed.
      JPanel panel = new JPanel();
      panel.setSize(getWidth(), getHeight());
      panel.setBackground(Color.WHITE);
      panel.setLayout(null);
      add(panel);
   
   	//Create and set up a MineCounter in the top left of the window to keep track of the mines left.
      this.numMines = numMines;
      minesFound = 0;
      mineCounter = new MineCounter(numMines);
      panel.add(mineCounter);
      mineCounter.setLocation(MARGIN_SIZE, MARGIN_SIZE);
   
   	//Create and set up a TimerDisplay in the top right of the window to display the elapsed time.
      timerDisplay = new TimerDisplay();
      panel.add(timerDisplay);
      timerDisplay.setLocation(panel.getWidth() - MARGIN_SIZE - W, MARGIN_SIZE);
   
   	//Create and set up a ResetButton to reset the game.
      resetButton = new ResetButton();
      panel.add(resetButton);
      resetButton.setLocation(panel.getWidth()/2 - H/2, MARGIN_SIZE);
   
   	//Create a board panel to hold all the game squares in a grid pattern, 
   	//then fill the board (and grid) with GameSquares.
      JPanel board = new JPanel(new GridLayout(height, width));
      board.setSize(width*SQUARE_SIZE, height*SQUARE_SIZE);
      panel.add(board);
      board.setLocation(MARGIN_SIZE, H + 2*MARGIN_SIZE);
      grid = new GameSquare[height][width];
      for (int r = 0; r < height; r++)
      {
         for (int c = 0; c < width; c++)
         {
            grid[r][c] = new GameSquare(r, c);
            board.add(grid[r][c]);
         }
      }
   
      setVisible(true); //Finally, display the window, allowing the game to begin.
   }
	
	/**Called when the game ends.
	  *
	  *@param victory Whether or not the game ended in a win.
	  */
   public void endGame(boolean victory)
   {
      timerDisplay.stop();
      if (victory)
      {
         gameWon = true;
      }
      else
      {
         gameLost = true;
      }
      resetButton.repaint();
      for (GameSquare[] row : grid)
      {
         for (GameSquare square : row)
         {
            square.repaint();
         }
      }
   }

	/**Defines the shared properties for the MineCounter and TimerDisplay.
	  */
   private class Display extends JPanel
   {
   	/**The number to be shown on the display. */
      protected int numShown;
   
   	/**Constructs a new Display object with the correct dimensions (defined as constants
   	  *in the NatsMinesweeper class) and a black background.
   	  */
      public Display()
      {
         setSize(W, H);
         setBackground(Color.BLACK);
      }
   	
		/**Paints this Display.
		  *
		  *@param g A graphics object for this object, with origin matched to this panel's origin.
		  */
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
         g.setColor(Color.RED);
         String str = "" + numShown;
         while (str.length() < DIGITS) //Make sure that the display always shows three filled digits
         {										//(or negative two digits).
            if (str.startsWith("-")) //Only for a single digit negative number
            {
               str = "-0" + str.substring(1);
            }
            else
            {
               str = "0" + str;
            }
         }
         Font f = g.getFont();
         g.setFont(new Font(f.getName(), f.getStyle(), FONT_SIZE)); //Keep the old font and style, but with a new size.
         g.drawString(str, SHIFT, H - SHIFT);
      }
   }
	
	/**A Display designed to print the number of mines left to be flagged on the board.
	  */
   private class MineCounter extends Display
   {
		/**The number of mines left to be flagged on the board.
		 *This assumes the flags already down are correct, thus adding a flag always decreases this number, whether
		 *the flag was correctly or incorrectly placed.
		 */
      private int minesLeft;
   
		/**Constructs a new MineCounter object with the correct starting number of mines.
		 */
      public MineCounter(int numMines)
      {
         super();
         minesLeft = numMines;
      }
   	
		/**Paints this panel.
		 *
		 *@param g A graphics object associated with this panel.
		 */
      public void paintComponent(Graphics g)
      {
         numShown = minesLeft;
         super.paintComponent(g);
      }
   	
		/**Resets the MineCounter.
		  */
      public void reset()
      {
         minesLeft = numMines;
         repaint();
      } 
   	
		/**Reduces the current count by 1, checking to see if the game has been won.
		  */
      public void reduceCount()
      {
         minesLeft--;
         repaint();
         if (minesLeft == 0 && minesFound == numMines)
         {
            endGame(true);
         }
      }
   
		/**Increases the current count by 1, checking to see if the game has been won.
		  */
      public void increaseCount()
      {
         minesLeft++;
         repaint();
         if (minesLeft == 0 && minesFound == numMines)
         {
            endGame(true); //This could only trigger if the player had flagged too many squares,
         }						//then went back and removed the erroneous ones.
      }
   }
	
	/**A Display to show the amount of time elapsed so far this game.
	  */
   private class TimerDisplay extends Display
   {
		/**The amount of time elapsed so far, in seconds. */
      private int timeElapsed;
		
		/**A Timer to do the actual timing from the computer's internal clock. */
      private java.util.Timer clock;
   	
		/**Constructs a TimerDisplay object. */
      public TimerDisplay()
      {
         timeElapsed = 0;
      }
   
		/**Paints this panel.
		 *
		 *@param g A graphics object associated with this panel.
		 */
      public void paintComponent(Graphics g)
      {
         numShown = timeElapsed;
         super.paintComponent(g);
      }
   	
		/**Starts the timer. Creates a new clock and schedules it to increment timeElapsed each second.
		  */
      public void start()
      {
         clock = new java.util.Timer();
         clock.schedule(
               new TimerTask() //This is really a new anonymous class, extending TimerTask.
               {					 //Since it has only one method, it seemed easiest to create it in line here.
                  public void run() //Called by the Timer when scheduled.
                  {
                     timeElapsed++;
                     if (timeElapsed > 999) //999 is the highest 3 digit integer, the highest number that can
                     {							  //be shown on the displays. Beyond that, the player automatically loses.
                        endGame(false);
                        return;
                     }
                     repaint();
                  }
               }
            			, 1000, 1000); //1000 milliseconds, so a repeated 1 second delay.
      }
   	
		/**Stops the clock. */
      public void stop()
      {
         clock.cancel();
      }
   	
		/**Resets the clock. */
      public void reset()
      {
         timeElapsed = 0;
         repaint();
      }
   }
	
	/**A panel clicked on to reset the game. */
   private class ResetButton extends JPanel
   {
		/**Constructs a new ResetButton. */
      public ResetButton()
      {
         setBackground(Color.WHITE);
         setSize(H, H);
         addMouseListener(
               new MouseAdapter() //Another anonymous class, extending MouseAdapter, created in line.
               {
                  public void mouseClicked(MouseEvent e) //Called whenever this panel is clicked on.
                  {
                     gameLost = false;
                     gameWon = false;
                     repaint();
                     mineCounter.reset();
                     timerDisplay.stop();
                     timerDisplay.reset();
                     for (GameSquare[] row : grid)
                     {
                        for (GameSquare square : row)
                        {
                           square.reset();
                        }
                     }
                     numRevealed = 0;
							minesFound = 0;
                  }
               });
      }
   	
		/**Paints this panel. Typically, the button shows a smiley face. If the game has been won, the
		 *smiley face gets sunglasses. If the game has been lost, it becomes a frowny face with crossed out eyes.
		 *
		 *@param g A graphics object associated with this panel.
		 */
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
         g.setColor(Color.YELLOW);
         g.fillOval(0, 0, H-1, H-1);
         g.setColor(Color.BLACK);
         g.drawOval(0, 0, H-1, H-1);
         if (gameLost) //Frowny face
         {
            g.drawArc(H/4, 3*H/5, H/2, H/2, SMILE_START, SMILE_LENGTH);
            Font f = g.getFont();
            g.setFont(new Font(f.getName(), f.getStyle(), 2*f.getSize()/3));
            g.drawString("x", H/4 - 1, H/4 + DEAD_EYE_SIZE);
            g.drawString("x", 3*H/4 - DEAD_EYE_SIZE + 1, H/4 + DEAD_EYE_SIZE);
            g.setFont(f);
         }
         else //Smiley face
         {
            g.drawArc(H/4, H/4, H/2, H/2, -SMILE_START, -SMILE_LENGTH);
            if (gameWon) //with sunglasses
            {
               g.fillOval(H/4 - 1, H/4, GLASSES_WIDTH, GLASSES_HEIGHT);
               g.fillOval(3*H/4 - GLASSES_WIDTH + 1, H/4, GLASSES_WIDTH, GLASSES_HEIGHT);
               g.drawLine(H/4 - 1, H/4 + GLASSES_HEIGHT/2, 1, H/4);
               g.drawLine(3*H/4 + 1, H/4 + GLASSES_HEIGHT/2, H-2, H/4);
            }
            else //without sunglasses
            {
               g.fillOval(H/4, H/4, EYE_SIZE, EYE_SIZE);
               g.fillOval(3*H/4 - EYE_SIZE, H/4, EYE_SIZE, EYE_SIZE);
            }
         }
      }
   }
	
	/**A single square of the game board. */
   private class GameSquare extends JPanel
   {
		/**The number of mines adjacent to this GameSquare.
		  *This number may not be accurate for a square that is itself a mine, but that should never be needed.
		  */
      private int adjacentMines;
		
		/**Whether or not this square is a mine. */
      private boolean mine;
		
		/**Whether or not this square has been revealed. */
      private boolean revealed;
		
		/**Whether or not this square has been flagged.
		  *Should never be true if revealed is true (and vice versa).
		  */
      private boolean flagged;
		
		/**The row of this square in the grid. */
      private int row;
		
		/**The column of this square in the grid. */
      private int col;
   	
		/**Constructs a new GameSquare.
		  *
		  *@param r The square's row
		  *@param c The square's column
		  */
      public GameSquare(int r, int c)
      {
         super();
         setBorder(BorderFactory.createLineBorder(Color.BLACK));
         adjacentMines = 0;
         mine = false;
         revealed = false;
         flagged = false;
         this.row = r;
         this.col = c;
         addMouseListener(
               new MouseAdapter() //Yet another anonymous class, extending MouseAdapter, created in line here.
               {
                  public void mousePressed(MouseEvent e) //Called whenever this square is clicked on.
                  {
                     if (gameWon || gameLost) {return;} //Clicking squares does nothing if the game is over.
                     if (revealed) //Double clicking a revealed square, if it is already touching the correct
                     {				  //number of flags, will reveal everything else around it.
                        if (e.getClickCount() >= 2 && countAdjacentFlags() == adjacentMines)
                        {
                           for (GameSquare square : getAdjacentSquares())
                           {
                              square.reveal();
                           }
                        }
                     }
                     else if (e.isControlDown() || SwingUtilities.isRightMouseButton(e)) //right click
                     {
                        if (numRevealed == 0) {return;} //Game has not begun yet.
                        flagged = !flagged;
                        if (flagged) //just placed a flag
                        {
                           if (mine)
                           {	
                              minesFound++;
                           }
                           mineCounter.reduceCount();
                        }
                        else //just removed a flag
                        {
                           if (mine)
                           {
                              minesFound--;
                           }
                           mineCounter.increaseCount();
                        }
                        repaint();
                     }
                     else //left click
                     {
                        if (numRevealed == 0) //The game begins here.
                        {
                           ArrayList<Integer> squares = new ArrayList<Integer>();
                           for (int ii = 0; ii < grid.length * grid[0].length; ii++)
                           {
                              squares.add(ii); //This list contains an integer representing each square in the board.
                           }
                           squares.remove(row * grid[0].length + col); //This protects the first click from being a mine.
                           for (int ii = 0; ii < numMines; ii++)
                           {
										//Randomly select a mine, numMines times.
                              Integer choice = squares.remove((int) (squares.size() * Math.random()));
                              grid[choice / grid[0].length][choice % grid[0].length].setMine();
                           }
                           for (Integer notMine : squares)
                           {
										//All non-mines must know how many mines they are touching.
                              grid[notMine / grid[0].length][notMine % grid[0].length].countAdjacentMines();
                           }
                           countAdjacentMines(); //Including the one originally clicked.
                           timerDisplay.start();
                        }
                        if (!flagged)
                        {
                           reveal();
                        }
                     }
                  }
               });				
      }
   	
		/**Resets the square to its pre-game state. */
      public void reset()
      {
         mine = false;
         revealed = false;
         flagged = false;
         repaint();
      }
   
		/**Reveals this square (assuming it can be revealed). */
      public void reveal()
      {
         if (revealed || flagged) {return;}
         revealed = true;
         repaint();
         if (mine)
         {
            endGame(false);
         }
         else
         {
            numRevealed++;
            if (numRevealed == grid.length * grid[0].length - numMines)
            {
               endGame(true); //Called if the player has revealed ever non-mine square, even if he hasn't flagged mines.
            }
         
            ArrayList<GameSquare> revealThese = null;
            if (adjacentMines == 0) //Blank squares automatically reveal everything around them.
            {
               revealThese = getAdjacentSquares();
            }
            else //All (non-mine) squares reveal any blank squares around them.
            {
               revealThese = getAdjacentEmptySquares();
            }
            for (GameSquare square : revealThese)
            {
               square.reveal();
            }
         }
      }
   
		/**Generates a list of all squares adjacent to this one.
		  *@return a list of all squares adjacent to this one
		  */
      private ArrayList<GameSquare> getAdjacentSquares()
      {
         ArrayList<GameSquare> list = new ArrayList<GameSquare>();
         for (int r = row - 1; r <= row + 1; r++)
         {
            if (r >= 0 && r < grid.length)
            {
               for (int c = col - 1; c <= col + 1; c++)
               {
                  if (c >= 0 && c < grid[0].length)
                  {
                     list.add(grid[r][c]);
                  }
               }
            }
         }
         return list;
      }
   
		/**Generates a list of any empty squares adjacent to this one.
		  *@return a list of any empty squares adjacent to this one.
		  */
      private ArrayList<GameSquare> getAdjacentEmptySquares()
      {
         ArrayList<GameSquare> list = new ArrayList<GameSquare>();
         for (GameSquare square : getAdjacentSquares())
         {
            if (!square.mine && square.adjacentMines == 0) //Must check to see if square is a mine, because mines
            {															  //may not have the correct value for adjacentMines.
               list.add(square);
            }
         }
         return list;
      }
   	
		/**Counts the number of mines adjacent to this square. */
      public void countAdjacentMines()
      {
         adjacentMines = 0;
         for (GameSquare square : getAdjacentSquares())
         {
            if (square.mine)
            {
               adjacentMines++;
            }
         }
      }
   	
		/**Counts the number of flagged squares adjacent to this square.
		  *Used only for the feature of double-clicking to reveal everything around an already revealed
		  *square, provided that the square is already touching its correct number of flags.
		  *
		  *@return the number of adjacent squares which are flagged
		  */
      public int countAdjacentFlags()
      {
         int adjacentFlags = 0;
         for (GameSquare square : getAdjacentSquares())
         {
            if(square.flagged)
            {
               adjacentFlags++;
            }
         }
         return adjacentFlags;
      }
   	
		/**Sets this square to be a mine. */
      public void setMine()
      {
         mine = true;
      }
   
		/**Paints this panel. During the game, an unrevealed panel appears green. If flagged, a flag icon
		  *appears on top of the green background. A revealed square appears white. Non-blank revealed squares
		  *display the number of adjacent mines in the appropriate color. When a mine is clicked on, that square
		  *turns red and shows a mine icon. All unidentified mines then turn white and show a mine icon.
		  *Incorrectly flagged squares then turn white and show a flag icon crossed out by a red X.
		  *
		  *@param g A graphics object associated with this panel.
		  */
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
			//This if statement isolates all conditions in which a square would appear white
			//(and one condition which would make it red). 
         if (revealed || (gameLost && ((mine && !flagged) || (!mine && flagged))))
         {
            setBackground(Color.WHITE);
            if (mine || flagged) //All conditions which would show a mine icon.
            {							//Any flagged square that fit the first if statement must be a falsely flagged
               if (revealed)		//non-mine after the game is lost, so it does fit this category.
               {
                  setBackground(Color.RED); //Just for the specific mine that was clicked on to lose the game.
               }
               g.fillOval(SHIFT - 1, SHIFT - 1, 
                  SQUARE_SIZE - 2*SHIFT + 1, SQUARE_SIZE - 2*SHIFT + 1);
               g.drawLine(SQUARE_SIZE/2, 0, SQUARE_SIZE/2, SQUARE_SIZE);
               g.drawLine(0, SQUARE_SIZE/2, SQUARE_SIZE, SQUARE_SIZE/2);
               g.drawLine(SHIFT, SQUARE_SIZE - SHIFT - 1, SQUARE_SIZE - SHIFT - 1, SHIFT);
               g.drawLine(SHIFT, SHIFT, SQUARE_SIZE - SHIFT - 1, SQUARE_SIZE - SHIFT - 1);
               g.setColor(Color.WHITE);
               g.fillRect(GLARE_LOC, GLARE_LOC, GLARE_SIZE, GLARE_SIZE);
               if (flagged) //Just mistakenly flagged non-mines after the game is lost.
               {
                  g.setColor(Color.RED);
                  g.drawLine(0, 0, SQUARE_SIZE, SQUARE_SIZE);
                  g.drawLine(1, 0, SQUARE_SIZE, SQUARE_SIZE - 1);
                  g.drawLine(0, 1, SQUARE_SIZE - 1, SQUARE_SIZE);
                  g.drawLine(0, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 0);
                  g.drawLine(1, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 1);
                  g.drawLine(0, SQUARE_SIZE - 2, SQUARE_SIZE - 2, 0);
               }
            }
            else //Revealed non-mines
            {
               if (adjacentMines != 0)
               {
                  g.setColor(COLORS[adjacentMines - 1]);
                  Font f = g.getFont();
                  g.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
                  g.drawString("" + adjacentMines, 
                     		SHIFT, SQUARE_SIZE - SHIFT);
               }
            }
         }
         else //Unrevealed
         {
            setBackground(Color.GREEN);
            if (flagged)
            {
               g.fillPolygon(FLAG_BASE_X, FLAG_BASE_Y, FLAG_BASE_X.length);
               g.drawLine(FLAG_BASE_X[2], FLAG_BASE_Y[2], 
                  		FLAG_BASE_X[2], FLAG_BASE_Y[2] - POLE_LENGTH);
               g.setColor(Color.RED);
               g.fillPolygon(FLAG_X, FLAG_Y, FLAG_X.length);
            }
         }
      }
   }
}