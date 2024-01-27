package view.gui;

import controller.ViewFeatures;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;
import model.Cell;
import model.ReadOnlyReversiModel;


/**
 * A class that represents a panel in any type of game of reversi. Contains methods
 * that are the same in both ReversiPanel and SquareReversiPanel.
 */
public abstract class AbstractReversiPanel extends JPanel implements PanelView {

  //the model of the reversi game
  protected ReadOnlyReversiModel model;
  protected final List<ViewFeatures> featuresListener;
  protected boolean hint;


  /**
   * constructs the panel by adding the model, adding mouse/key listeners, and setting
   * the background color.
   * @param model reversi game model.
   */
  public AbstractReversiPanel(ReadOnlyReversiModel model) {
    this.model = Objects.requireNonNull(model);
    this.featuresListener = new ArrayList<>();
    this.hint = false;
    setBackground(Color.DARK_GRAY);

    //add key listener
    setFocusable(true);
    requestFocusInWindow();
    ReversiPanel.KeyEventsListener keyListener = new ReversiPanel.KeyEventsListener();
    this.addKeyListener(keyListener);
  }


  /**
   * paints all the hexagons in the game board of a reversi game onto the panel.
   * @param graphics the <code>Graphics</code> object to protect
   */
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
  }


  /**
   * Adds a listener to the view to react to the signals the view sends.
   * @param features the listener (the controller).
   */
  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.featuresListener.add(Objects.requireNonNull(features));
  }



  /**
   * This method tells Swing what the "natural" size should be
   * for this panel.  Here, we set it to 500x500 pixels.
   * @return  Our preferred *physical* size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500, 500);
  }


  /**
   * Deselects all hexagons on the board so that they all return grey.
   */
  public abstract void deselectAll();



  /**
   * Is this click outside the board? check.
   * @param mouseX represents the X of click.
   * @param mouseY represents the Y of click.
   * @param sideLength represents the side boundary.
   */
  protected abstract void clickOutsideBoard(int mouseX, int mouseY, double sideLength);


  /**
   * Checks if a point is inside a hexagon.
   * @param x      X-coordinate of the point.
   * @param y      Y-coordinate of the point.
   * @param cellX   X-coordinate of the hexagon.
   * @param cellY   Y-coordinate of the hexagon.
   * @param length Side length of the hexagon.
   * @return True if the point is inside the hexagon, false otherwise.
   */
  protected abstract boolean isPointInCell(int x, int y, double cellX, double cellY, double length);


  /**
   * determines if any of the hexagon cells are selected/ highlighted.
   * @return true if there is a hexagon cell that is selected.
   */
  protected abstract boolean isOneSelected();



  /**
   * returns the hexagon cell in the board that is selected.
   * @return the HexagonCell that is selected, or null if there is none.
   */
  protected Cell getSelected() {
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Cell selected = null;
    for (ArrayList<Cell> list : board) {
      for (Cell cell : list) {
        if (cell.selected()) {
          selected = cell;
        }
      }
    }
    return selected;
  }

  public void toggleHintMode() {
    hint = !hint;
    repaint();
  }




  /**
   * A class that handles key events and holds methods to carry out specific actions based
   * on key events.
   */
  protected class KeyEventsListener extends KeyAdapter {

    /**
     * Indicates that a player wants to make a move to the highlighted cell is the key "m"
     * is pressed, and indicates that a cell must be selected if there is no highlighted cell.
     * Indicates that a player wants to pass if the key "p" is pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
      Character key = e.getKeyChar();

      // resets the highlight but does and indicates a move to be made
      if (key.equals('m') || key.equals('\n') || key.equals('M') || key.equals(' ')) {
        if (isOneSelected()) {
          Cell hex = getSelected();
          int diagonalPos = hex.getDiagonalPos();
          int rowPos = hex.getRowPos();

          for (ViewFeatures features : featuresListener) {
            features.playerMove(diagonalPos, rowPos);
          }
          deselectAll();
        }
      }

      if (key.equals('p') || key.equals('P')) {

        for (ViewFeatures features : featuresListener) {
          features.playerPass();
        }
        deselectAll();
      }

      if (key.equals('h') || key.equals('H')) {
        toggleHintMode();
      }

    }
  }

  @Override
  public ReadOnlyReversiModel getModel() {
    return this.model;
  }


}
