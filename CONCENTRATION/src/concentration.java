//HOMEWORK 9 - Concentration 

/*DIRECTIONS:
Click on any card to turn it over. You can turn two card over per turn and if 
they are the same number and color, they are a match and will be removed. If they
are not a match, click anywhere on the screen to turn both cards back over. You have a
limited number of attempts to find all of the matches in the deck. Note that when you 
successfully find a match, it will not take away from your remaining attempts.
*/


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;


// constants in the game 
interface Constants {
  int CARD_WIDTH = 50;
  int CARD_HEIGHT = 100;
  int BOARD_WIDTH = 910;
  int BOARD_HEIGHT = 580;
  int TEXT_SIZE = 15;
  IDeck ideck = new IDeck();
  int NUM_ATTEMPTS = 140;
}


// a class that holds methods on ArrayList<Card>
class IDeck implements Constants {

  // shuffles the deck
  // EFFECT: randomizes the order of Cards in the ArrayList<Card>
  public void shuffleDeck(ArrayList<Card> deck) {
    Collections.shuffle(deck);
  }

  // counts the number of cards that are turned over
  public int countTurned(ArrayList<Card> deck) {
    int counter = 0;
    for (Card c : deck) {
      if (c.isClicked) {
        counter = counter + 1;
      }
      else {
        counter = counter + 0;
      }
    }
    return counter;
  }

  // returns a list of the cards in a deck that are turned over
  public ArrayList<Card> getTurned(ArrayList<Card> deck) {
    ArrayList<Card> arlist = new ArrayList<Card>();
    for (Card c : deck) {
      if (c.isClicked) {
        arlist.add(c);
      }
    }
    return arlist;
  }

  // turns all cards over
  // EFFECT: all of the isClicked fields of the cards are turned false
  public void turnAllOver(ArrayList<Card> deck) {
    for (Card c : deck) {
      c.isClicked = false;
    }
  }

  // makes all cards active
  // EFFECT: changes the isActive field to true for all cards
  public void makeAllActive(ArrayList<Card> deck) {
    for (Card c : deck) {
      c.isActive = true;
    }
  }

  // turns over a card at a given index
  public void turnOneOver(ArrayList<Card> deck, int index) {
    for (Card c : deck) {
      if (c.sameCard(deck.get(index))) {
        c.turnOver();
      }
    }
  }

  // adds a list of cards of length 52 to a given worldscene
  public WorldScene addCards(ArrayList<Card> deck, WorldScene scene) {

    for (int i = 0; i < 13; i++) {
      deck.get(i).addCard(scene, (150 + ((CARD_WIDTH + 2) * i)), CARD_HEIGHT + 2);
    }
    for (int i = 13; i < 26; i++) {
      deck.get(i).addCard(scene, (150 + ((CARD_WIDTH + 2) * (i - 13))), 2 * (CARD_HEIGHT + 2));
    }
    for (int i = 26; i < 39; i++) {
      deck.get(i).addCard(scene, (150 + ((CARD_WIDTH + 2) * (i - 26))), 3 * (CARD_HEIGHT + 2));
    }
    for (int i = 39; i < 52; i++) {
      deck.get(i).addCard(scene, (150 + ((CARD_WIDTH + 2) * (i - 39))), 4 * (CARD_HEIGHT + 2));
    }
    return scene;
  }

  // removes any cards from the first array list that are in the second array list
  // by making them not active
  public void eliminateCards(ArrayList<Card> deck, ArrayList<Card> removables) {
    for (int j = 0; j < removables.size(); j++) {
      for (Card c : deck) {
        if (c.sameCard(removables.get(j))) {
          c.eliminate();
        }
      }
    }
  }

}

// a class to represent an individual card
class Card implements Constants {
  int rank;
  String suit;
  boolean isClicked; // if the card is turned over
  boolean isActive; // if the card is still in the game

  //convenience constructor
  Card(int rank, String suit) {
    this.rank = rank;
    this.suit = suit;
    this.isClicked = false;
    this.isActive = true;
  }

  Card(int rank, String suit, boolean isClicked, boolean isActive) {
    this.rank = rank;
    this.suit = suit;
    this.isClicked = isClicked;
    this.isActive = isActive;
  }

  // turns a card over
  // EFFECT: alters the isClicked field to be true
  public void turnOver() {
    if (this.isActive) {
      this.isClicked = true;
    }
  }

  // makes a card inactive
  // EFFECT: alters the isActive field to be false
  public void eliminate() {
    this.isActive = false;
    this.isClicked = false;
  }

  // checks if two cards are a matching suit (diamonds and hearts, clubs and
  // spaids)
  public boolean matchingSuit(Card c) {
    return (this.suit.equals("♥") && c.suit.equals("♦"))
        || (this.suit.equals("♦") && c.suit.equals("♥"))
        || (this.suit.equals("♠") && c.suit.equals("♣"))
        || (this.suit.equals("♣") && c.suit.equals("♠"));
  }

  // checks if two cards are a match
  public boolean isAMatch(Card c) {
    return (this.rank == c.rank) && this.matchingSuit(c);
  }

  // checks if two card are the same
  public boolean sameCard(Card c) {
    return (this.rank == c.rank) && this.suit.equals(c.suit);
  }

  // generates a text image of a card
  public TextImage cardText() {
    String txt;
    if (this.rank == 1) {
      txt = this.suit + "A";
    }
    else {
      if (this.rank == 11) {
        txt = this.suit + "J";
      }
      else {
        if (this.rank == 12) {
          txt = this.suit + "Q";
        }
        else {
          if (this.rank == 13) {
            txt = this.suit + "K";
          }
          else {
            txt = "" + this.suit + this.rank;
          }
        }
      }
    }

    Color color;
    if (this.suit.equals("♥") || this.suit.equals("♦")) {
      color = Color.RED;
    }
    else {
      color = Color.BLACK;
    }

    return new TextImage(txt, TEXT_SIZE, color);
  }

  // generates an image at a single card
  public WorldImage drawCard() {

    if (this.isClicked && this.isActive) {
      return new OverlayImage(this.cardText(),
          new RectangleImage(CARD_WIDTH, CARD_HEIGHT, "outline", Color.RED));
    }
    else {
      if (!this.isClicked && this.isActive) {
        return new RectangleImage(CARD_WIDTH, CARD_HEIGHT, "solid", Color.RED);

      }
      else {
        return new RectangleImage(CARD_WIDTH, CARD_HEIGHT, "solid", Color.WHITE);
      }
    }

  }

  // adds a single image of a card to a WorldScene
  public WorldScene addCard(WorldScene scene, int x, int y) {
    scene.placeImageXY(this.drawCard(), x, y);
    return scene;
  }

}


class Time {
  int mins;
  int secs;
  
  Time(int mins, int secs) {
    this.mins = mins;
    this.secs = secs;
  }
  
  // adds one second to the current time
  // EFFECT: modifies the secs field to be one greater, if seconds is at 60,
  // adds one to the mins field
  public void addSecond() {
    if (this.secs == 59) {
      this.mins = this.mins + 1;
      this.secs = 0;
    }
    else {
      this.secs = this.secs + 1;
    }
  }
  
  // displays the time
  public TextImage displayTime() {
    String displayMins;
    String displaySecs;
    if (this.mins < 10) {
      displayMins = "0" + this.mins;
    }
    else {
      displayMins = "" + this.mins;
    }
    
    if (this.secs < 10) {
      displaySecs = "0" + this.secs;
    }
    else {
      displaySecs = "" + this.secs;
    }
    
    return new TextImage(
        "Time Elapsed: " + displayMins + ":" + displaySecs, 25, Color.BLACK);
  }
  
}



// a class to represent the state of the world
class ConcentrationWorld extends World implements Constants {
  ArrayList<Card> deck;
  int score;
  int counter;
  boolean gameOver;
  Time time;

  ConcentrationWorld(ArrayList<Card> deck) {
    this.deck = deck;
    this.score = 26;
    this.counter = 0;
    this.gameOver = false;
    this.time = new Time(0, 0);
  }

  // draws the currect state of a world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(BOARD_WIDTH, BOARD_HEIGHT);
    WorldScene mtworld = new WorldScene(BOARD_WIDTH, BOARD_HEIGHT);
    TextImage scoreBoard = new TextImage("Score: " + this.score, 25, Color.BLUE);
    TextImage attemptBoard = new TextImage("Attempts Remaining: " + (NUM_ATTEMPTS - this.counter),
        25, Color.darkGray);
    TextImage youWin = new TextImage(("You Win!"), 25, Color.GREEN);
    WorldScene winScene = new WorldScene(BOARD_WIDTH, BOARD_HEIGHT);
    winScene.placeImageXY(youWin, BOARD_WIDTH / 2, BOARD_HEIGHT / 2);

    if (this.gameOver) {
      mtworld.placeImageXY(new TextImage("game over", 20, Color.RED), BOARD_WIDTH / 2,
          BOARD_HEIGHT / 2);
      return mtworld;
    }
    else {
      if (this.score == 0) {
        return winScene;
      }
      else {
        scene.placeImageXY(scoreBoard, BOARD_WIDTH / 2, BOARD_HEIGHT - 90);
        scene.placeImageXY(attemptBoard, BOARD_WIDTH / 2, BOARD_HEIGHT - 60);
        scene.placeImageXY(this.time.displayTime(), BOARD_WIDTH / 2, BOARD_HEIGHT - 30);
        return ideck.addCards(this.deck, scene);
      }
    }
  }

  // the next state of the world after one tick
  // EFFECT: ends the game if the attempts are out
  // EFFECT: removes two cards if they are turned over and a match
  public void nextWorld() {
    if (this.counter == NUM_ATTEMPTS) {
      this.gameOver = true;
    }
    if (ideck.countTurned(this.deck) == 2) {
      if (ideck.getTurned(this.deck).get(0).isAMatch(ideck.getTurned(this.deck).get(1))) {
        ideck.eliminateCards(this.deck, ideck.getTurned(this.deck));
        this.score = this.score - 1;
      }
    }
  }

  // advances the program to the next world at each tick
  public void onTick() {
    this.nextWorld();
    this.time.addSecond();
  }

  // turns over a card if the mouse is clicked on it
  // if two cards are already turned over, turns over all cards by clicking
  // anywhere
  public void onMouseClicked(Posn posn) {
    if (ideck.countTurned(this.deck) < 2) {
      for (int i = 0; i < (this.deck.size() / 4); i++) {
        if (posn.x >= (2 + ((CARD_WIDTH + 2) * i)) + (150 - (CARD_WIDTH / 2))
            && posn.x <= ((2 + CARD_WIDTH) * (i + 1)) + (150 - (CARD_WIDTH / 2))
            && posn.y >= 2 + (CARD_HEIGHT / 2) && posn.y <= 2 + CARD_HEIGHT + (CARD_HEIGHT / 2)) {
          ideck.turnOneOver(this.deck, i);
        }
      }
      for (int i = (this.deck.size() / 4); i < (this.deck.size() / 2); i++) {
        if (posn.x >= (2 + ((CARD_WIDTH + 2) * (i - deck.size() / 4)))
            + (150 - (CARD_WIDTH / 2) )
            && posn.x <= ((2 + CARD_WIDTH) * ((i - deck.size() / 4) + 1))
                + (150 - (CARD_WIDTH / 2) )
            && posn.y >= 2 + (CARD_HEIGHT + 2) + (CARD_HEIGHT / 2)
            && posn.y <= 2 * (2 + CARD_HEIGHT) + (CARD_HEIGHT / 2)) {
          ideck.turnOneOver(this.deck, i);
        }
      }
      for (int i = (this.deck.size() / 2); i < (this.deck.size() * 0.75); i++) {
        if (posn.x >= (2 + ((CARD_WIDTH + 2) * (i - deck.size() / 2)))
            + (150 - (CARD_WIDTH / 2) )
            && posn.x <= ((2 + CARD_WIDTH) * ((i - deck.size() / 2) + 1))
                + (150 - (CARD_WIDTH / 2) )
            && posn.y >= 2 + ((CARD_HEIGHT + 2) * 2) + (CARD_HEIGHT / 2)
            && posn.y <= 3 * (2 + CARD_HEIGHT) + (CARD_HEIGHT / 2)) {
          ideck.turnOneOver(this.deck, i);
        }
      }
      for (int i = ((this.deck.size() * 3) / 4); i < this.deck.size(); i++) {
        if (posn.x >= (2 + ((CARD_WIDTH + 2) * (i - deck.size() * 0.75)))
            + (150 - (CARD_WIDTH / 2) )
            && posn.x <= ((2 + CARD_WIDTH) * ((i - deck.size() * 0.75) + 1))
                + (150 - (CARD_WIDTH / 2) )
            && posn.y >= 2 + ((CARD_HEIGHT + 2) * 3) + (CARD_HEIGHT / 2)
            && posn.y <= 4 * (2 + CARD_HEIGHT) + (CARD_HEIGHT / 2)) {
          ideck.turnOneOver(this.deck, i);
        }
      }
    }
    else {
      if (posn.x > 0 && posn.x < BOARD_WIDTH && posn.y > 0 && posn.y < BOARD_HEIGHT) {
        ideck.turnAllOver(this.deck);
        this.counter = counter + 1;
      }
    }

  }

  // resets the world if the key "r" is pressed
  public void onKeyEvent(String s) {
    if (s.equals("r")) {
      ideck.makeAllActive(this.deck);
      ideck.turnAllOver(this.deck);
      ideck.shuffleDeck(this.deck);
      this.gameOver = false;
      this.counter = 0;
      this.score = 26;
      this.time = new Time(0, 0);
    }
  }
}

class ExamplesConcentration implements Constants {

  Card hearts1;
  Card hearts2;
  Card hearts3;
  Card hearts4;
  Card hearts5;
  Card hearts6;
  Card hearts7;
  Card hearts8;
  Card hearts9;
  Card hearts10;
  Card hearts11;
  Card hearts12;
  Card hearts13;
  Card diamonds1;
  Card diamonds2;
  Card diamonds3;
  Card diamonds4;
  Card diamonds5;
  Card diamonds6;
  Card diamonds7;
  Card diamonds8;
  Card diamonds9;
  Card diamonds10;
  Card diamonds11;
  Card diamonds12;
  Card diamonds13;
  Card clubs1;
  Card clubs2;
  Card clubs3;
  Card clubs4;
  Card clubs5;
  Card clubs6;
  Card clubs7;
  Card clubs8;
  Card clubs9;
  Card clubs10;
  Card clubs11;
  Card clubs12;
  Card clubs13;
  Card spaids1;
  Card spaids2;
  Card spaids3;
  Card spaids4;
  Card spaids5;
  Card spaids6;
  Card spaids7;
  Card spaids8;
  Card spaids9;
  Card spaids10;
  Card spaids11;
  Card spaids12;
  Card spaids13;
  ArrayList<Card> deck = new ArrayList<Card>();
  IDeck ideck1;

  // initialize data
  void initiData() {
    hearts1 = new Card(1, "♥");
    hearts2 = new Card(2, "♥");
    hearts3 = new Card(3, "♥");
    hearts4 = new Card(4, "♥");
    hearts5 = new Card(5, "♥");
    hearts6 = new Card(6, "♥");
    hearts7 = new Card(7, "♥");
    hearts8 = new Card(8, "♥");
    hearts9 = new Card(9, "♥");
    hearts10 = new Card(10, "♥");
    hearts11 = new Card(11, "♥");
    hearts12 = new Card(12, "♥");
    hearts13 = new Card(13, "♥");
    diamonds1 = new Card(1, "♦");
    diamonds2 = new Card(2, "♦");
    diamonds3 = new Card(3, "♦");
    diamonds4 = new Card(4, "♦");
    diamonds5 = new Card(5, "♦");
    diamonds6 = new Card(6, "♦");
    diamonds7 = new Card(7, "♦");
    diamonds8 = new Card(8, "♦");
    diamonds9 = new Card(9, "♦");
    diamonds10 = new Card(10, "♦");
    diamonds11 = new Card(11, "♦");
    diamonds12 = new Card(12, "♦");
    diamonds13 = new Card(13, "♦");
    clubs1 = new Card(1, "♣");
    clubs2 = new Card(2, "♣");
    clubs3 = new Card(3, "♣");
    clubs4 = new Card(4, "♣");
    clubs5 = new Card(5, "♣");
    clubs6 = new Card(6, "♣");
    clubs7 = new Card(7, "♣");
    clubs8 = new Card(8, "♣");
    clubs9 = new Card(9, "♣");
    clubs10 = new Card(10, "♣");
    clubs11 = new Card(11, "♣");
    clubs12 = new Card(12, "♣");
    clubs13 = new Card(13, "♣");
    spaids1 = new Card(1, "♠");
    spaids2 = new Card(2, "♠");
    spaids3 = new Card(3, "♠");
    spaids4 = new Card(4, "♠");
    spaids5 = new Card(5, "♠");
    spaids6 = new Card(6, "♠");
    spaids7 = new Card(7, "♠");
    spaids8 = new Card(8, "♠");
    spaids9 = new Card(9, "♠");
    spaids10 = new Card(10, "♠");
    spaids11 = new Card(11, "♠");
    spaids12 = new Card(12, "♠");
    spaids13 = new Card(13, "♠");
    ideck1 = new IDeck();
    deck = new ArrayList<Card>(Arrays.asList(hearts1, hearts2, hearts3, hearts4, hearts5, hearts6,
        hearts7, hearts8, hearts9, hearts10, hearts11, hearts12, hearts13, diamonds1, diamonds2,
        diamonds3, diamonds4, diamonds5, diamonds6, diamonds7, diamonds8, diamonds9, diamonds10,
        diamonds11, diamonds12, diamonds13, clubs1, clubs2, clubs3, clubs4, clubs5, clubs6, clubs7,
        clubs8, clubs9, clubs10, clubs11, clubs12, clubs13, spaids1, spaids2, spaids3, spaids4,
        spaids5, spaids6, spaids7, spaids8, spaids9, spaids10, spaids11, spaids12, spaids13));
  }

  void testTurnOver(Tester t) {
    this.initiData();
    t.checkExpect(hearts1.isClicked, false);
    hearts1.turnOver();
    t.checkExpect(hearts1.isClicked, true);
    t.checkExpect(spaids4.isClicked, false);
    spaids4.turnOver();
    t.checkExpect(spaids4.isClicked, true);
    t.checkExpect(diamonds11.isClicked, false);
    diamonds11.eliminate();
    diamonds11.turnOver();
    t.checkExpect(diamonds11.isClicked, false);
  }

  void testCountTurned(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts12, this.clubs4, this.clubs8, this.diamonds6));
    t.checkExpect(ideck1.countTurned(deckA), 0);
    this.hearts12.turnOver();
    t.checkExpect(ideck1.countTurned(deckA), 1);
    this.clubs4.turnOver();
    t.checkExpect(ideck1.countTurned(deckA), 2);
  }

  void testMatchingSuit(Tester t) {
    this.initiData();
    t.checkExpect(hearts7.matchingSuit(diamonds1), true);
    t.checkExpect(spaids3.matchingSuit(diamonds1), false);
    t.checkExpect(spaids5.matchingSuit(clubs1), true);
  }

  void testIsAMatch(Tester t) {
    this.initiData();
    t.checkExpect(diamonds7.isAMatch(hearts7), true);
    t.checkExpect(clubs1.isAMatch(diamonds10), false);
    t.checkExpect(clubs1.isAMatch(spaids1), true);
    t.checkExpect(diamonds8.isAMatch(spaids8), false);
  }

  void testGetTurned(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts12, this.clubs4, this.clubs8, this.diamonds6));
    t.checkExpect(ideck1.getTurned(deckA), new ArrayList<Card>());
    this.hearts12.turnOver();
    this.diamonds6.turnOver();
    ArrayList<Card> deckB = new ArrayList<Card>(Arrays.asList(this.hearts12, this.diamonds6));
    t.checkExpect(ideck1.getTurned(deckA), deckB);
  }

  void testTurnAllOver(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts12, this.clubs4, this.clubs8, this.diamonds6));
    this.spaids7.turnOver();
    t.checkExpect(spaids7.isClicked, true);
    this.clubs8.turnOver();
    t.checkExpect(clubs8.isClicked, true);
    ideck1.turnAllOver(deckA);
    t.checkExpect(spaids7.isClicked, false);
    t.checkExpect(clubs8.isClicked, false);
  }

  void testSameCard(Tester t) {
    this.initiData();
    t.checkExpect(this.spaids11.sameCard(this.spaids10), false);
    t.checkExpect(this.diamonds3.sameCard(this.hearts3), false);
    t.checkExpect(this.clubs2.sameCard(this.clubs2), true);
  }
  
  void testMakeAllActive(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts6, this.clubs4, this.clubs8, this.diamonds6));
    t.checkExpect(hearts6.isActive, true);
    t.checkExpect(clubs8.isActive, true);
    hearts6.eliminate();
    clubs8.eliminate();
    t.checkExpect(hearts6.isActive, false);
    t.checkExpect(clubs8.isActive, false);
    ideck.makeAllActive(deckA);
    t.checkExpect(hearts6.isActive, true);
    t.checkExpect(clubs8.isActive, true);
  }
  
  void testEliminate(Tester t) {
    this.initiData();
    t.checkExpect(clubs6.isActive, true);
    clubs6.eliminate();
    t.checkExpect(clubs6.isActive, false);
    clubs6.eliminate();
    t.checkExpect(clubs6.isActive, false);
    t.checkExpect(spaids13.isActive, true);
    spaids13.eliminate();
    t.checkExpect(spaids13.isActive, false);
  }
  

  void testCardText(Tester t) {
    this.initiData();
    t.checkExpect(hearts1.cardText(), new TextImage("♥A", 15, Color.RED));
    t.checkExpect(clubs11.cardText(), new TextImage("♣J", 15, Color.BLACK));
    t.checkExpect(diamonds12.cardText(), new TextImage("♦Q", 15, Color.RED));
    t.checkExpect(spaids13.cardText(), new TextImage("♠K", 15, Color.BLACK));
    t.checkExpect(spaids3.cardText(), new TextImage("♠3", 15, Color.BLACK));
    t.checkExpect(hearts10.cardText(), new TextImage("♥10", 15, Color.RED));
  }

  void testEliminateCards(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts12, this.clubs4, this.clubs8, this.diamonds6));
    ArrayList<Card> deckB = new ArrayList<Card>(Arrays.asList(this.spaids7, this.clubs8));
    ideck1.eliminateCards(deckA, deckB);
    t.checkExpect(spaids7.isActive, false);
    t.checkExpect(hearts12.isActive, true);
    t.checkExpect(clubs4.isActive, true);
    t.checkExpect(clubs8.isActive, false);
    t.checkExpect(diamonds6.isActive, true);
  }

  void testTurnOneOver(Tester t) {
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts12, this.clubs4, this.clubs8, this.diamonds6));
    t.checkExpect(clubs4.isClicked, false);
    ideck.turnOneOver(deckA, 2);
    t.checkExpect(clubs4.isClicked, true);
  }

  void testNextWorld(Tester t) {
    // next world when there is a match
    this.initiData();
    ArrayList<Card> deckA = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts6, this.clubs4, this.clubs8, this.diamonds6));
    hearts6.turnOver();
    diamonds6.turnOver();
    ArrayList<Card> deckB = new ArrayList<Card>(Arrays.asList(hearts6, diamonds6));
    t.checkExpect(ideck.getTurned(deckA), deckB);
    ConcentrationWorld worldA = new ConcentrationWorld(deckA);
    worldA.nextWorld();
    t.checkExpect(hearts6.isActive, false);
    // next world when nothing happens
    this.initiData();
    ArrayList<Card> deckC = new ArrayList<Card>(
        Arrays.asList(this.spaids7, this.hearts6, this.clubs4, this.clubs8, this.diamonds6));
    this.spaids7.turnOver();
    ConcentrationWorld worldB = new ConcentrationWorld(deckC);
    worldB.nextWorld();
    t.checkExpect(worldB, worldB);
    this.hearts6.turnOver();
    worldB.nextWorld();
    t.checkExpect(worldB, worldB);
  }
  
  void testAddSecond(Tester t) {
    Time time1 = new Time(0,0);
    time1.addSecond();
    t.checkExpect(time1.secs, 1);
    Time time2 = new Time(0, 59);
    time2.addSecond();
    t.checkExpect(time2.mins, 1);
    t.checkExpect(time2.secs, 0);
  }
  

  void testBigBang(Tester t) {
    this.initiData();
    ideck.shuffleDeck(deck);
    ConcentrationWorld initialState = new ConcentrationWorld(deck);

    World w = initialState;
    int worldWidth = 1000;
    int worldHeight = 1000;
    double tickRate = 1.0;
    w.bigBang(worldWidth, worldHeight, tickRate);
  }

}