//Homework 3 : word lists

import java.util.Random;
import java.awt.Color;
import javalib.funworld.WorldScene;
import javalib.funworld.World;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

interface Constants {
  int BG_HEIGHT = 600;
  int BG_WIDTH = 400;
  String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
  int TEXT_SIZE = 20;
}

class ZTypeWorld extends World implements Constants {
  ILoWord words;
  int count;
  Random random = new Random();

  // constructor
  ZTypeWorld(ILoWord words, int count) {
    this.words = words;
    this.count = count;
  }

  /*
   * FIELDS OF ZTYPEWORLD: 
   * this.words ... ILoWord 
   * this.count ... int 
   * this.random ... Random
   * 
   * METHODS: 
   * this.makeScene() ... WorldScene 
   * this.onKeyEvent(String) ... ZTypeWorld 
   * this.nextWorld() ... ZTypeWorld 
   * this.lastScene(String) ... WorldScene 
   * this.onTick() ... ZTypeWorld 
   * this.addRandomWord() ... ZTypeWord
   * this.getCharacter(int) ... String 
   * this.randomString(String, int) ... String
   * 
   * METHODS OF FIELDS: 
   * this.words.checkAndReduce(String)... ILoWord
   * this.words.checkFirst(String) ... IWord 
   * this.words.filterOutEmpties()... ILoWord 
   * this.words.draw()... WorldScene 
   * this.words.wordsFall() ... ILoWord
   * this.words.anyOnGround() ... boolean 
   * this.words.anyActive() ... IWord
   * this.words.inactives() ... ILoWord 
   * this.words.doNotReduce(String) ... ILoWord
   * this.words.addToEnd(IWord) ... ILoWord
   * 
   */

  // draws the current state of a world scene
  public WorldScene makeScene() {
    return this.words.draw();
  }

  // on key
  public ZTypeWorld onKeyEvent(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    if (this.words.anyActive().emptyWord()) {
      if (this.words.checkFirst(s).emptyWord()) {
        return this;
      }
      else {
        return new ZTypeWorld(new ConsLoWord(this.words.checkFirst(s).reduce().makeActive(),
            this.words.doNotReduce(s)), this.count);
      }
    }
    else {
      if (this.words.anyActive().firstLetter().equals(s)) {
        return new ZTypeWorld(
            new ConsLoWord(this.words.anyActive().reduce(), this.words.inactives()), this.count);
      }
      else {
        return this;
      }
    }
  }

  // next world
  ZTypeWorld nextWorld() {
    if (this.words.anyOnGround()) {
      return (ZTypeWorld) this.endOfWorld("game over");
    }
    else {
      return new ZTypeWorld(this.words.wordsFall().filterOutEmpties(), this.count + 1);
    }
  }

  // the ending scene
  public WorldScene lastScene(String msg) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    return new WorldScene(BG_WIDTH, BG_HEIGHT)
        .placeImageXY(new TextImage("game over", 20, Color.RED), BG_WIDTH / 2, BG_HEIGHT / 2);
  }

  // on tick
  public ZTypeWorld onTick() {
    return this.nextWorld().addRandomWord();
  }

  // adds a new word to an existing world
  ZTypeWorld addRandomWord() {
    if (this.count % 10 == 0) {
      return new ZTypeWorld(
          this.words.addToEnd(
              new InactiveWord(randomString("", 6), random.nextInt(BG_WIDTH - 50) + 25, 0)),
          this.count);
    }
    else {
      return this;
    }
  }

  // pulls out a character of the alphabet at a given number
  String getCharacter(int start) {
    /*
     * FIELDS: none
     *
     * METHODS: none
     *
     * METHODS FOR FIELDS: none
     */
    return CHARACTERS.substring(start, start + 1);
  }

  // generates a random string of a given length
  String randomString(String s, int length) {
    /*
     * FIELDS: none
     *
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    if (s.length() == length) {
      return s;
    }
    else {
      return randomString(s + getCharacter(random.nextInt(25)), length);
    }
  }

}

//represents a list of words
interface ILoWord extends Constants {

  // removes the first letter of words in a list that start with
  // a given letter
  ILoWord checkAndReduce(String s);

  // returns a word in a list that starts with a given letter
  IWord checkFirst(String s);

  // filters out an empty words in a list of words
  ILoWord filterOutEmpties();

  // draws the words in a list of worlds onto a given WorldScene
  WorldScene draw();

  // subtracts one from the y-coordinates of all the words in a list of words
  ILoWord wordsFall();

  // checks if any words have a y-coordinate of zero
  boolean anyOnGround();

  // checks if any words are active
  IWord anyActive();

  // returns only the inactive words in a list
  ILoWord inactives();

  // returns a list of words excluding
  // the first word that starts with a given letter
  ILoWord doNotReduce(String s);

  // adds a given word to the end of a list of words
  ILoWord addToEnd(IWord word);

}

//represents an empty list of words
class MtLoWord implements ILoWord {
  /*
   * FIELDS: none
   * 
   * METHODS: 
   * this.checkAndReduce(String)... ILoWord 
   * this.checkFirst(String) ... IWord 
   * this.filterOutEmpties()... ILoWord 
   * this.draw()... WorldScene
   * this.wordsFall() ... ILoWord 
   * this.anyOnGround() ... boolean 
   * this.anyActive() ... IWord 
   * this.inactives() ... ILoWord 
   * this.doNotReduce(String) ... ILoWord
   * this.addToEnd(IWord) ... ILoWord
   *
   * METHODS FOR FIELDS: none
   */

  // removes the first letter of words in a list that start with
  // a given letter
  public ILoWord checkAndReduce(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    return this;
  }

  // returns a word in a list that starts with a given letter
  public IWord checkFirst(String s) {
    /*
     * FIELDS: none
     *
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    return new InactiveWord("", 0, 0);
  }

  // filters out an empty words in a list of words
  public ILoWord filterOutEmpties() {
    return this;
  }

  // draws the words in a list of worlds onto a given WorldScene
  public WorldScene draw() {
    return new WorldScene(BG_WIDTH, BG_HEIGHT);
  }

  // subtracts one from the y-coordinates of all the words in a list of words
  public ILoWord wordsFall() {
    return this;
  }

  // checks if any words have a y-coordinate of zero
  public boolean anyOnGround() {
    return false;
  }

  // checks if the list contains an active word
  public IWord anyActive() {
    return new InactiveWord("", 0, 0);
  }

  // returns only the inactive words in a list
  public ILoWord inactives() {
    return this;
  }

  // returns a list of words excluding
  // the first word that starts with a given letter
  public ILoWord doNotReduce(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    return this;
  }

  // adds a given word to the end of a list of words
  public ILoWord addToEnd(IWord word) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * word.firstLetter()... String 
     * word.checkFirstLetter(String)... boolean 
     * word.emptyWord()... boolean 
     * word.reduce()... IWord word.makeText()... WorldImage 
     * word.drawHelper(WorldScene)... WorldScene 
     * word.textFall() ... IWord 
     * word.onGround() ... boolean 
     * word.isActive() ... boolean
     * word.makeActive() ... ActiveWord
     *
     * METHODS FOR FIELDS: none
     */
    return new ConsLoWord(word, new MtLoWord());
  }

}

class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * FIELDS: 
   * this.first ... IWord 
   * this.rest ... ILoWord
   * 
   * METHODS: 
   * this.checkAndReduce(String)... ILoWord 
   * this.checkFirst(String) ... IWord 
   * this.filterOutEmpties()... ILoWord 
   * this.draw()... WorldScene
   * this.wordsFall() ... ILoWord 
   * this.anyOnGround() ... boolean 
   * this.anyActive() ... IWord 
   * this.inactives() ... ILoWord 
   * this.doNotReduce(String) ... ILoWord
   * this.addToEnd(IWord) ... ILoWord
   *
   * METHODS FOR FIELDS: 
   * this.rest.checkAndReduce(String)... ILoWord
   * this.rest.checkFirst(String) ... IWord 
   * this.rest.filterOutEmpties()... ILoWord 
   * this.rest.draw()... WorldScene 
   * this.rest.wordsFall() ... ILoWord
   * this.rest.anyOnGround() ... boolean 
   * this.rest.anyActive() ... IWord
   * this.rest.inactives() ... ILoWord 
   * this.rest.doNotReduce(String) ... ILoWord
   * this.rest.addToEnd(IWord) ... ILoWord 
   * this.first.firstLetter()... String
   * this.first.checkFirstLetter(String)... boolean 
   * this.first.emptyWord()... boolean 
   * this.first.reduce()... IWord 
   * this.first.makeText()... WorldImage
   * this.first.drawHelper(WorldScene)... WorldScene 
   * this.first.textFall() ...
   * IWord this.first.onGround() ... boolean 
   * this.first.isActive() ... boolean
   * this.first.makeActive() ... ActiveWord
   * 
   */

  // removes the first letter of words in a list that start with
  // a given letter
  public ILoWord checkAndReduce(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    if (this.first.checkFirstLetter(s)) {
      return new ConsLoWord(this.first.reduce(), this.rest.checkAndReduce(s));
    }
    else {
      return new ConsLoWord(this.first, this.rest.checkAndReduce(s));
    }
  }

  // returns a word in a list that starts with a given letter
  public IWord checkFirst(String s) {
    /*
     * FIELDS: none
     *
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    if (this.first.checkFirstLetter(s)) {
      return this.first;
    }
    else {
      return this.rest.checkFirst(s);
    }
  }

  // filters out an empty words in a list of words
  public ILoWord filterOutEmpties() {
    if (this.first.emptyWord()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // draws the words in a list of worlds onto a given WorldScene
  public WorldScene draw() {
    return (this.first.drawHelper(this.rest.draw()));
  }

  // subtracts one from the y-coordinates of all the words in a list of words
  public ILoWord wordsFall() {
    return new ConsLoWord(this.first.textFall(), this.rest.wordsFall());
  }

  // checks if any words have a y-coordinate of zero
  public boolean anyOnGround() {
    return this.first.onGround() || this.rest.anyOnGround();
  }

  // checks if any words are active
  public IWord anyActive() {
    if (this.first.isActive()) {
      return this.first.makeActive();
    }
    else {
      return this.rest.anyActive();
    }
  }

  // returns only the inactive words in a list
  public ILoWord inactives() {
    if (!this.first.isActive()) {
      return new ConsLoWord(this.first, this.rest.inactives());
    }
    else {
      return this.rest.inactives();
    }
  }

  // returns a list of words excluding
  // the first word that starts with a given letter
  public ILoWord doNotReduce(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     *
     * METHODS FOR FIELDS: none
     */
    if (this.first.checkFirstLetter(s)) {
      return this.rest;
    }
    else {
      return new ConsLoWord(this.first, this.rest.doNotReduce(s));
    }
  }

  // adds a given word to the end of a list of words
  public ILoWord addToEnd(IWord word) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * word.firstLetter()... String 
     * word.checkFirstLetter(String)... boolean 
     * word.emptyWord()... boolean 
     * word.reduce()... IWord 
     * word.makeText()... WorldImage 
     * word.drawHelper(WorldScene)... WorldScene 
     * word.textFall() ... IWord 
     * word.onGround() ... boolean 
     * word.isActive() ... boolean
     * word.makeActive() ... ActiveWord
     *
     * METHODS FOR FIELDS: none
     */
    return new ConsLoWord(this.first, this.rest.addToEnd(word));
  }

}

//represents a word in the ZType game
interface IWord extends Constants {

  // checks if the first letter in a word is the same as a given letter
  boolean checkFirstLetter(String s);

  // takes the first letter off of a word
  IWord reduce();

  // returns a given character in a string
  String firstLetter();

  // checks if a word is empty
  boolean emptyWord();

  // creates a text image of a given word
  WorldImage makeText();

  // places a text onto a world scene
  WorldScene drawHelper(WorldScene world);

  // subtracts one from the y coordinate of an IWord
  IWord textFall();

  // checks if the y coordinate of a word is zero
  boolean onGround();

  // checks if a word is an active word
  boolean isActive();

  // returns any word as an active word
  ActiveWord makeActive();
}

//represents a word
abstract class AWord implements IWord {
  String word;
  int x;
  int y;

  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  /*
   * FIELDS: 
   * this.word... String 
   * this.x... int 
   * this.y... int
   * 
   * METHODS: 
   * this.firstLetter()... String 
   * this.checkFirstLetter(String)... boolean 
   * this.emptyWord()... boolean 
   * this.reduce()... IWord 
   * this.makeText()... WorldImage 
   * this.drawHelper(WorldScene)... WorldScene 
   * this.textFall() ... IWord 
   * this.onGround() ... boolean 
   * this.isActive() ... boolean
   * this.makeActive() ... ActiveWord
   *
   * METHODS FOR FIELDS: 
   * this.word.equals(String) ... boolean
   * 
   */

  // returns the first character in a string
  public String firstLetter() {
    return this.word.substring(0, 1);
  }

  // checks if the first letter in a word is the same as a given letter
  public boolean checkFirstLetter(String s) {
    /*
     * FIELDS OF s: none
     * 
     * METHODS: 
     * s.equals(String) ... boolean
     * 
     * METHODS FOR FIELDS: none
     */
    if (this.word.equals("")) {
      return false;
    }
    else {
      return this.firstLetter().equals(s);
    }
  }

  // checks if a word is empty
  public boolean emptyWord() {
    return (this.word.equals(""));
  }

  // takes the first letter off of a word
  public abstract IWord reduce();

  // creates a text image of a given word
  abstract public WorldImage makeText();

  // subtracts one from the y coordinate of an IWord
  abstract public IWord textFall();

  // checks if the y coordinate of a word is zero
  public boolean onGround() {
    return this.y == BG_HEIGHT;
  }

  // places a text onto a world scene
  public WorldScene drawHelper(WorldScene world) {
    /*
     * FIELDS OF world: none
     * 
     * METHODS: 
     * world.placeImageXY(textImage, int, int) ... WorldScene
     *
     * METHODS FOR FIELDS: none
     */
    return (world.placeImageXY(this.makeText(), this.x, this.y));
  }

  // checks if a word is active
  public abstract boolean isActive();

  // makes an word an active word
  public abstract ActiveWord makeActive();

}

//represents an active word in the ZType game
class ActiveWord extends AWord {

  ActiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /*
   * FIELDS: 
   * this.word... String 
   * this.x... int 
   * this.y... int
   * 
   * METHODS: 
   * this.firstLetter()... String 
   * this.checkFirstLetter(String)... boolean 
   * this.emptyWord()... boolean 
   * this.reduce()... IWord 
   * this.makeText()... WorldImage 
   * this.drawHelper(WorldScene)... WorldScene 
   * this.textFall() ... IWord 
   * this.onGround() ... boolean 
   * this.isActive() ... boolean
   * this.makeActive() ... ActiveWord
   *
   * METHODS FOR FIELDS: 
   * this.word.equals(String) ... boolean
   * 
   */

  // creates a text image of a given word
  public WorldImage makeText() {
    return new TextImage(this.word, TEXT_SIZE, Color.GREEN);
  }

  // subtracts one from the y coordinate of an IWord
  public IWord textFall() {
    return new ActiveWord(this.word, this.x, this.y + 10);
  }

  // takes the first letter off of a word
  public IWord reduce() {
    if (this.word.equals("")) {
      return this;
    }
    else {
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
  }

  // makes a word active
  public ActiveWord makeActive() {
    return this;
  }

  // checks if a word is active
  public boolean isActive() {
    return true;
  }

}

//represents an inactive word in the ZType game
class InactiveWord extends AWord {

  // constructor
  InactiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /*
   * FIELDS: 
   * this.word... String 
   * this.x... int 
   * this.y... int
   * 
   * METHODS: 
   * this.firstLetter()... String 
   * this.checkFirstLetter(String)... boolean 
   * this.emptyWord()... boolean 
   * this.reduce()... IWord 
   * this.makeText()... WorldImage 
   * this.drawHelper(WorldScene)... WorldScene 
   * this.textFall() ... IWord 
   * this.onGround() ... boolean 
   * this.isActive() ... boolean
   * this.makeActive() ... ActiveWord
   *
   * METHODS FOR FIELDS: 
   * this.word.equals(String) ... boolean
   * 
   */

  // creates a text image of a given word
  public WorldImage makeText() {
    return new TextImage(this.word, TEXT_SIZE, Color.BLACK);
  }

  // subtracts one from the y coordinate of an IWord
  public IWord textFall() {
    return new InactiveWord(this.word, this.x, this.y + 10);
  }

  // takes the first letter off of a word
  public IWord reduce() {
    if (this.word.equals("")) {
      return this;
    }
    else {
      return new InactiveWord(this.word.substring(1), this.x, this.y);
    }
  }

  // turns an inactive word into an active word
  public ActiveWord makeActive() {
    return new ActiveWord(this.word, this.x, this.y);
  }

  // checks if a word is active
  public boolean isActive() {
    return false;
  }

}

//all examples and tests for ILoWord
class ExamplesWordLists implements Constants {
  IWord apple = new ActiveWord("apple", 50, 30);
  IWord pple = new ActiveWord("pple", 50, 30);
  IWord banana = new InactiveWord("banana", 80, 10);
  IWord strawberry = new ActiveWord("strawberry", 20, 90);
  IWord kiwi = new ActiveWord("kiwi", 30, 40);
  IWord mango = new InactiveWord("mango", 70, 70);
  IWord orange = new InactiveWord("orange", 50, 600);
  IWord raspberry = new ActiveWord("raspberry", 40, 60);
  IWord mtword = new InactiveWord("", 0, 0);
  IWord grape = new ActiveWord("Grape", 60, 80);
  ILoWord mtlist = new MtLoWord();
  ILoWord list2 = new ConsLoWord(apple, new ConsLoWord(banana, new ConsLoWord(strawberry, mtlist)));
  ILoWord list4 = new ConsLoWord(banana, mtlist);
  ILoWord list5 = new ConsLoWord(mango, new ConsLoWord(orange, new ConsLoWord(raspberry, mtlist)));
  ILoWord list6 = new ConsLoWord(new InactiveWord("banana", 80, 20), mtlist);
  ILoWord list11 = new ConsLoWord(pple, new ConsLoWord(banana, new ConsLoWord(strawberry, mtlist)));
  ILoWord list12 = new ConsLoWord(banana, new ConsLoWord(kiwi, mtlist));
  ILoWord list13 = new ConsLoWord(apple,
      new ConsLoWord(banana, new ConsLoWord(mtword, new ConsLoWord(strawberry, mtlist))));
  TextImage kiwiimage = new TextImage("kiwi", 20, Color.GREEN);
  TextImage bananaimage = new TextImage("banana", 20, Color.BLACK);
  ILoWord list14 = new ConsLoWord(kiwi, list4);
  WorldScene mtworld = new WorldScene(BG_WIDTH, BG_HEIGHT);
  IWord start = new InactiveWord("start", BG_WIDTH / 2, 0);
  IWord start2 = new InactiveWord("start", BG_WIDTH / 2, 10);
  ILoWord startlist = new ConsLoWord(start, mtlist);
  ILoWord startlist2 = new ConsLoWord(start2, mtlist);
  ZTypeWorld INITIAL_STATE = new ZTypeWorld(startlist, 0);
  ZTypeWorld INITIAL_STATE2 = new ZTypeWorld(startlist2, 1);
  ZTypeWorld world1 = new ZTypeWorld(list4, 0);
  ZTypeWorld world2 = new ZTypeWorld(list6, 1);
  ZTypeWorld world3 = new ZTypeWorld(list5, 0);
  ZTypeWorld mtZworld = new ZTypeWorld(mtlist, 0);
  WorldScene end = new WorldScene(BG_WIDTH, BG_HEIGHT)
      .placeImageXY(new TextImage("game over", 20, Color.RED), BG_WIDTH / 2, BG_HEIGHT / 2);

  // tests

  boolean firstLeter(Tester t) {
    return t.checkExpect(this.apple.firstLetter(), "a")
        && t.checkExpect(this.mtword.firstLetter(), "");

  }

  boolean testCheckFirstLetter(Tester t) {
    return t.checkExpect(this.apple.checkFirstLetter("a"), true)
        && t.checkExpect(this.banana.checkFirstLetter("a"), false)
        && t.checkExpect(this.mtword.checkFirstLetter("a"), false);

  }

  boolean testReduce(Tester t) {
    return t.checkExpect(this.apple.reduce(), pple)
        && t.checkExpect(this.mtword.reduce(), this.mtword);

  }

  boolean testAddToEnd(Tester t) {
    return t.checkExpect(this.list4.addToEnd(kiwi), list12)
        && t.checkExpect(this.mtlist.addToEnd(banana), list4);
  }

  boolean testCheckAndReduce(Tester t) {
    return t.checkExpect(this.list2.checkAndReduce("a"), list11)
        && t.checkExpect(this.list5.checkAndReduce("a"), list5)
        && t.checkExpect(this.mtlist.checkAndReduce("a"), mtlist);

  }

  boolean testEmptyWord(Tester t) {
    return t.checkExpect(this.apple.emptyWord(), false)
        && t.checkExpect(this.mtword.emptyWord(), true);
  }

  boolean testFilterOutEmpties(Tester t) {
    return t.checkExpect(this.list13.filterOutEmpties(), list2)
        && t.checkExpect(this.list2.filterOutEmpties(), list2);
  }

  boolean testMakeText(Tester t) {
    return t.checkExpect(this.kiwi.makeText(), kiwiimage)
        && t.checkExpect(this.mtword.makeText(), new TextImage("", 20, Color.BLACK));
  }

  boolean testDrawHelper(Tester t) {
    return t.checkExpect(this.kiwi.drawHelper(mtworld),
        (new WorldScene(BG_WIDTH, BG_HEIGHT).placeImageXY(kiwiimage, 30, 40)))
        && t.checkExpect(this.mtword.drawHelper(mtworld), mtworld);
  }

  boolean testDraw(Tester t) {
    return t.checkExpect(this.mtlist.draw(), mtworld)
        && t.checkExpect(this.list14.draw(), new WorldScene(BG_WIDTH, BG_HEIGHT)
            .placeImageXY(bananaimage, 80, 10).placeImageXY(kiwiimage, 30, 40));
  }

  boolean testTextFall(Tester t) {
    return t.checkExpect(this.apple.textFall(), new ActiveWord("apple", 50, 40));
  }

  boolean testOnGround(Tester t) {
    return t.checkExpect(this.apple.onGround(), false)
        && t.checkExpect(this.orange.onGround(), true);
  }

  boolean testWordsFall(Tester t) {
    return t.checkExpect(this.list4.wordsFall(), list6)
        && t.checkExpect(this.mtlist.wordsFall(), mtlist);
  }

  boolean testAnyOnGround(Tester t) {
    return t.checkExpect(this.list2.anyOnGround(), false)
        && t.checkExpect(this.mtlist.anyOnGround(), false)
        && t.checkExpect(this.list5.anyOnGround(), true);
  }

  boolean testNextWorld(Tester t) {
    return t.checkExpect(INITIAL_STATE.nextWorld(), INITIAL_STATE2)
        && t.checkExpect(world1.nextWorld(), world2)
        && t.checkExpect(world3.nextWorld(), world3.endOfWorld("game over"));
  }

  boolean testAnyActive(Tester t) {
    return t.checkExpect(this.list2.anyActive(), apple)
        && t.checkExpect(this.list4.anyActive(), mtword);
  }

  boolean testInactives(Tester t) {
    return t.checkExpect(this.list2.inactives(), list4)
        && t.checkExpect(this.list4.inactives(), list4);
  }

  boolean testDoNotReduce(Tester t) {
    return t.checkExpect(this.list2.doNotReduce("a"),
        new ConsLoWord(banana, new ConsLoWord(strawberry, mtlist)))
        && t.checkExpect(this.list2.doNotReduce("b"),
            new ConsLoWord(apple, new ConsLoWord(strawberry, mtlist)));
  }

  boolean testIsActive(Tester t) {
    return t.checkExpect(this.apple.isActive(), true)
        && t.checkExpect(this.banana.isActive(), false);
  }

  boolean testMakeActive(Tester t) {
    return t.checkExpect(this.apple.makeActive(), apple)
        && t.checkExpect(this.banana.makeActive(), new ActiveWord("banana", 80, 10));
  }

  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.INITIAL_STATE.makeScene(), this.startlist.draw())
        && t.checkExpect(this.world1.makeScene(), this.list4.draw())
        && t.checkExpect(this.mtZworld.makeScene(), mtworld);
  }

  boolean testLastScene(Tester t) {
    return t.checkExpect(INITIAL_STATE.lastScene("game over"), end)
        && t.checkExpect(world1.lastScene("game over"), end);
  }

  boolean testBigBang(Tester t) {
    World w = INITIAL_STATE;
    int worldWidth = 1000;
    int worldHeight = 700;
    double tickRate = 0.2;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }

}
