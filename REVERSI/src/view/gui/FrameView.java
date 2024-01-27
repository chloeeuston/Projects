package view.gui;


import controller.ViewFeatures;

/**
 * An interface that represents the Frame to the view of a reversi game.
 */
public interface FrameView {


  /**
   * Adds a listener to the view to react to the signals the view sends.
   * @param features the listener (the controller).
   */
  void addFeatureListener(ViewFeatures features);


  /**
   * updates the view to the current state of the model by repainting the panel.
   */
  void refresh();

  /**
   * Adds a message to the view.
   * @param message the written message to be added.
   */
  void showMessageDialog(String message);

  /**
   * Deselects all cells in the game board.
   */
  void deselectAll();


  /**
   * Sets the view to be visible.
   */
  void setVisible();


}
