package view.gui;

import controller.ViewFeatures;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.ReadOnlyReversiModel;
import view.ReversiView;


/**
 * a class that represents the frame of the window for a reversi game.
 */
public class ReversiFrame extends JFrame implements FrameView, ReversiView {
  //the panel that goes inside the frame in a reversi game
  private final AbstractReversiPanel panel;


  /**
   * constructs the frame for the view and adds the panel to the frame.
   * @param model reversi game model.
   */
  public ReversiFrame(ReadOnlyReversiModel model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (!model.isSquareReversi()) {
      this.panel = new ReversiPanel(model);
    }
    else {
      this.panel = new SquareReversiPanel(model);
    }
    this.add(panel);
    this.pack();
  }


  /**
   * Adds a message to the view.
   * @param message the written message to be added.
   */
  @Override
  public void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }


  /**
   * Adds a listener to the view to react to the signals the view sends.
   * @param features the listener (the controller).
   */
  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.panel.addFeatureListener(features);
  }

  /**
   * Deselects all cells in the game board.
   */
  @Override
  public void deselectAll() {
    this.panel.deselectAll();
  }


  /**
   * updates the view to the current state of the model by repainting the panel.
   */
  @Override
  public void refresh() {
    this.panel.repaint();
  }


  /**
   * Sets the view to be visible.
   */
  @Override
  public void setVisible() {
    this.setVisible(true);
  }


}
