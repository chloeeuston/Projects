package view.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputAdapter;
import model.Cell;
import model.ReadOnlyReversiModel;


/**
 * the panel and contents of the reversi game that goes inside the window frame.
 */
public class ReversiPanel extends AbstractReversiPanel {

  //the set of hexagon images on the panel
  protected ArrayList<Hexagon> hexagons;



  /**
   * constructs the panel by adding the model, adding mouse/key listeners, and setting
   * the background color.
   * @param model reversi game model.
   */
  public ReversiPanel(ReadOnlyReversiModel model) {
    super(model);
    this.hexagons = new ArrayList<>();

    //add mouse listener
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);

  }


  /**
   * paints all the hexagons in the game board of a reversi game onto the panel.
   * @param graphics the <code>Graphics</code> object to protect
   */
  @Override
  public void paintComponent(Graphics graphics) {

    super.paintComponent(graphics);
    Rectangle bounds = this.getBounds();
    double sideLength = this.getHexSideLength(bounds);
    Graphics2D g2d = (Graphics2D) graphics;
    //resets the board every time paint is called
    this.hexagons = new ArrayList<>();
    this.createHexagons(bounds);
    //draws the hexagons onto the panel
    for (Hexagon hex : hexagons) {
      hex.drawHex(g2d, sideLength);
      if (hint && hex.isClicked() && (hex.getHexCell().isEmpty())) {
        addHint(hex, g2d);
      }
    }
  }

  private void addHint(Hexagon hex, Graphics2D graphics) {
    Cell cell = hex.getHexCell();
    int diagonal = cell.getDiagonalPos();
    int row = cell.getRowPos();
    int x = hex.getCenterX();
    int y = hex.getCenterY();
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
   * creates all the Hexagons from the HexagonCells in the reversi game board.
   * @param bounds the bounds of the panel.
   */
  protected void createHexagons(Rectangle bounds) {
    double sideLength = this.getHexSideLength(bounds);
    double hexWidth = (Math.sqrt(3) * sideLength);

    for (ArrayList<Cell> list : model.getGameBoard()) {
      for (int i = 0; i < list.size(); i ++) {
        Cell cell = list.get(i);
        double middleXFrame = bounds.getWidth() / 2;
        //the number of half-widths the starting hexagon center should be from the center
        int numSpacesShift = ((model.getBoardSideLength() - 1) * 2) - (Math.abs(cell.getRowPos()));
        //the x value of the center of the first hexagon in each row
        double start = middleXFrame - ((hexWidth / 2) * numSpacesShift);
        //adds a one hexagon width to the x coordinate for every cell in the list
        int x = (int) (start + (hexWidth * i));

        //the y coordinate of the middle of the board
        double middleYFrame = bounds.getHeight() / 2;
        //adds/subtracts the height of one hexagon to the y coordinate depending on how far the
        //row is from the middle row
        int y = (int) (middleYFrame + (cell.getRowPos() * (sideLength * 1.5)));

        Hexagon hexagon = new Hexagon(cell, x, y);
        hexagons.add(hexagon);
      }
    }
  }


  /**
   * returns what the side length of one hexagon should be so that it fills up the screen
   * either horizontally or vertically.
   * @param bounds the bounds of the screen.
   * @return the side length for one hexagon.
   */
  protected double getHexSideLength(Rectangle bounds) {
    double boardLength = bounds.getHeight();
    //the number of side lengths (of an individual hex cell) that should fit vertically
    int numVerticalSideLengths = (model.getBoardSideLength() * 3) - 1;
    double verticalSideLength = boardLength / numVerticalSideLengths;

    double boardWidth = bounds.getWidth();
    //the number of cells in the greatest horizontal row (the middle row)
    int numMaxHorizontalCells = (model.getBoardSideLength() * 2) - 1;
    double horizontalSideLength = boardWidth / (Math.sqrt(3) * numMaxHorizontalCells);

    //the side length will be the smallest of these values to ensure that the grid fits
    //both horizontally and vertically
    return Math.min(verticalSideLength, horizontalSideLength);
  }


  /**
   * Deselects all hexagons on the board so that they all return grey.
   */
  @Override
  public void deselectAll() {
    for (Hexagon hex : hexagons) {
      if (hex.isClicked()) {
        hex.selectCell();
      }
    }
  }


  /**
   * deselects any hexagons that is not the one being selected.
   * @param selected the hexagon being selected.
   */
  protected void resetFlips(Hexagon selected) {
    for (Hexagon hex : hexagons) {
      if (!hex.equals(selected) && hex.isClicked()) {
        hex.selectCell();
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
    for (Hexagon hex : hexagons) {
      if (this.isPointInCell(mouseX, mouseY, hex.getCenterX(), hex.getCenterY(), sideLength)) {
        insideBoard = true;
      }
    }
    if (!insideBoard) {
      deselectAll();
      repaint();
    }
  }


  /**
   * Checks if a point is inside a hexagon.
   * @param x      X-coordinate of the point.
   * @param y      Y-coordinate of the point.
   * @param cellX   X-coordinate of the hexagon.
   * @param cellY   Y-coordinate of the hexagon.
   * @param length Side length of the hexagon.
   * @return True if the point is inside the hexagon, false otherwise.
   */
  protected boolean isPointInCell(int x, int y, double cellX, double cellY, double length) {
    //checks that the distance from the center to the point is within the hexagon
    double distance = Math.sqrt(Math.pow(x - cellX, 2) + Math.pow(y - cellY, 2));
    return (distance < Math.sqrt(3) * length / 2);
  }


  /**
   * determines if any of the hexagon cells are selected/ highlighted.
   * @return true if there is a hexagon cell that is selected.
   */
  protected boolean isOneSelected() {
    boolean anySelected = false;
    for (Hexagon hex : hexagons) {
      if (hex.isClicked()) {
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
      double sideLength = getHexSideLength(getBounds());
      clickOutsideBoard(mouseX, mouseY, sideLength);

      // Check if the click is within any hexagon bounds

      // Here the view highlights the clicked cell. It will click any cell within the board.
      // We decided to have any cell be able to be highlighted, whether it included a tile or not.
      // This is because the user should be able to highlight any spot, just not be able to make
      // a move there.

      for (Hexagon hex : hexagons) {
        if (isPointInCell(mouseX, mouseY, hex.getCenterX(), hex.getCenterY(), sideLength)) {
          resetFlips(hex);
          hex.selectCell();
          repaint();
        }
      }
    }
  }


}