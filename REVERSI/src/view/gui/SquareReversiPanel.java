package view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.MouseInputAdapter;
import model.Cell;
import model.ReadOnlyReversiModel;


/**
 * the panel and contents of the square reversi game that goes inside the window frame.
 */
public class SquareReversiPanel extends AbstractReversiPanel {

  //the set of square images on the panel
  private List<Square> squares;



  /**
   * constructs the panel by adding the model, adding mouse/key listeners, and setting
   * the background color.
   * @param model reversi game model.
   */
  public SquareReversiPanel(ReadOnlyReversiModel model) {
    super(model);
    this.squares = new ArrayList<>();

    //add mouse listener
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
  }


  /**
   * paints all the squares in the game board of a reversi game onto the panel.
   * @param graphics the <code>Graphics</code> object to protect
   */
  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g2d = (Graphics2D)graphics;
    Rectangle bounds = this.getBounds();
    double sideLength = this.getSquareSideLength(bounds);

    //resets the board every time paint is called
    this.squares = new ArrayList<>();
    this.createSquares(bounds);
    //draws the squares onto the panel
    for (Square sq : squares) {
      sq.drawSquare(g2d, sideLength);
      if (hint && sq.isClicked() && (sq.getSquareCell().isEmpty())) {
        addHint(sq, g2d);
      }
    }
  }


  /**
   * Adds a string image to the posn of the highlighted square cell.
   * @param sq the square that is highlighted.
   * @param graphics the graphics for the string to be printed on.
   */
  private void addHint(Square sq, Graphics2D graphics) {
    Cell cell = sq.getSquareCell();
    int diagonal = cell.getDiagonalPos();
    int row = cell.getRowPos();
    int x = sq.getCenterX();
    int y = sq.getCenterY();
    int hintNumber = model.countPiecesGained(diagonal, row);

    Font originalFont = graphics.getFont();
    Color originalColor = graphics.getColor();

    graphics.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust the font as needed
    graphics.setColor(Color.BLACK);

    graphics.drawString(Integer.toString(hintNumber), x, y);

    graphics.setFont(originalFont);
    graphics.setColor(originalColor);
  }



  /**
   * creates all the squares from the SquareCells in the reversi game board.
   * @param bounds the bounds of the panel.
   */
  private void createSquares(Rectangle bounds) {
    double sideLength = this.getSquareSideLength(bounds);
    int boardSide = model.getBoardSideLength();

    for (ArrayList<Cell> list : model.getGameBoard()) {
      for (int i = 0; i < list.size(); i ++) {
        Cell cell = list.get(i);
        double middleXFrame = bounds.getWidth() / 2;
        double halfSideLength = sideLength / 2;
        int halfBoard = boardSide / 2;
        double leftStartingPoint = middleXFrame - (sideLength * halfBoard) + halfSideLength;
        int x = (int) (leftStartingPoint + (sideLength * i));

        double middleYFrame = bounds.getHeight() / 2;
        double topStartingPoint = middleYFrame - (sideLength * halfBoard) + halfSideLength;
        int y = (int) (topStartingPoint + (sideLength * cell.getRowPos()));

        Square square = new Square(cell, x, y);
        squares.add(square);
      }
    }
  }


  /**
   * returns what the side length of one square should be so that it fills up the screen
   * either horizontally or vertically.
   * @param bounds the bounds of the screen.
   * @return the side length for one square.
   */
  private double getSquareSideLength(Rectangle bounds) {
    double boardLength = bounds.getHeight();
    //the number of side lengths (of an individual square cell) that should fit vertically
    int numVerticalSideLengths = model.getBoardSideLength() + 2;
    double verticalSideLength = boardLength / numVerticalSideLengths;

    double boardWidth = bounds.getWidth();
    //the number of cells in the greatest horizontal row (the middle row)
    int numMaxHorizontalCells = model.getBoardSideLength() + 2;
    double horizontalSideLength = boardWidth / numMaxHorizontalCells;

    //the side length will be the smallest of these values to ensure that the grid fits
    //both horizontally and vertically
    return Math.min(verticalSideLength, horizontalSideLength);
  }


  /**
   * Deselects all squares on the board so that they all return grey.
   */
  @Override
  public void deselectAll() {
    for (Square sq : squares) {
      if (sq.isClicked()) {
        sq.selectCell();
      }
    }
  }


  /**
   * deselects any cells that is not the one being selected.
   * @param selected the square being selected.
   */
  protected void resetFlips(Square selected) {
    for (Square sq : squares) {
      if (!sq.equals(selected) && sq.isClicked()) {
        sq.selectCell();
      }
    }
  }


  /**
   * Is this click outside the board? check.
   * @param mouseX represents the X of click.
   * @param mouseY represents the Y of click.
   * @param sideLength represents the side boundary.
   */
  protected void clickOutsideBoard(int mouseX, int mouseY, double sideLength) {
    boolean insideBoard = false;
    for (Square sq : squares) {
      if (this.isPointInCell(mouseX, mouseY, sq.getCenterX(), sq.getCenterY(), sideLength)) {
        insideBoard = true;
      }
    }
    if (!insideBoard) {
      deselectAll();
      repaint();
    }
  }


  /**
   * Checks if a point is inside a cell.
   * @param x      X-coordinate of the point.
   * @param y      Y-coordinate of the point.
   * @param cellX   X-coordinate of the square.
   * @param cellY   Y-coordinate of the square.
   * @param length Side length of the square.
   * @return True if the point is inside the square, false otherwise.
   */
  protected boolean isPointInCell(int x, int y, double cellX, double cellY, double length) {
    //checks that the distance from the center to the point is within the square
    double distance = Math.sqrt(Math.pow(x - cellX, 2) + Math.pow(y - cellY, 2));
    return (distance < Math.sqrt(3) * (length * 0.6) / 2);
  }


  /**
   * determines if any of the square cells are selected/ highlighted.
   * @return true if there is a square cell that is selected.
   */
  protected boolean isOneSelected() {
    boolean anySelected = false;
    for (Square sq : squares) {
      if (sq.isClicked()) {
        anySelected = true;
        break;
      }
    }
    return anySelected;
  }


  /**
   * A class that handles mouse events and holds methods that carry out actions based on
   * specific mouse events.
   */
  class MouseEventsListener extends MouseInputAdapter {

    /**
     * selects (highlights) a cell if the mouse clicks on it, unselects all cells if the mouse
     * clicks anywhere outside the board.
     * @param e the event to be processed.
     */
    @Override
    public void mousePressed(MouseEvent e) {
      // Get the mouse click coordinates
      int mouseX = e.getX();
      int mouseY = e.getY();
      double sideLength = getSquareSideLength(getBounds());
      clickOutsideBoard(mouseX, mouseY, sideLength);

      // Check if the click is within any square bounds

      // Here the view highlights the clicked cell. It will click any cell within the board.
      // We decided to have any cell be able to be highlighted, whether it included a tile or not.
      // This is because the user should be able to highlight any spot, just not be able to make
      // a move there.

      for (Square sq : squares) {
        if (isPointInCell(mouseX, mouseY, sq.getCenterX(), sq.getCenterY(), sideLength)) {
          resetFlips(sq);
          sq.selectCell();
          repaint();
        }
      }
    }
  }


}
