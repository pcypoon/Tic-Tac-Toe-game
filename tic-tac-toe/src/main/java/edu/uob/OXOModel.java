package edu.uob;

import java.util.ArrayList;

class OXOModel {

  private ArrayList<OXOPlayer> players;
  private ArrayList<ArrayList<OXOPlayer>> cells;
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    // initialize cells with arraylist
    cells = new ArrayList<>();
    players = new ArrayList<>();
    for (int i = 0; i < numberOfRows; i++) {
      cells.add(new ArrayList<>());
      for (int j = 0; j < numberOfColumns; j++) {
        cells.get(i).add(null);
      }
    }
  }

  public int getNumberOfPlayers() {
    return players.size();
  }

  public void addPlayer(OXOPlayer player) {
    players.add(player);
  }

  public OXOPlayer getPlayerByNumber(int number) {
    return players.get(number);
  }

  public OXOPlayer getWinner() {
    return winner;
  }

  public void setWinner(OXOPlayer player) {
    winner = player;
  }

  public int getCurrentPlayerNumber() {
    return currentPlayerNumber;
  }

  public void setCurrentPlayerNumber(int playerNumber) {
    currentPlayerNumber = playerNumber;
  }

  public int getNumberOfRows() {
    return cells.size();
  }

  public int getNumberOfColumns() {
    return cells.get(0).size();
  }

  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
    return cells.get(rowNumber).get(colNumber);
  }

  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    cells.get(rowNumber).set(colNumber, player);
  }

  public void setWinThreshold(int winThresh) {
    winThreshold = winThresh;
  }

  public int getWinThreshold() {
    return winThreshold;
  }

  public void setGameDrawn() {
    gameDrawn = true;
  }

  public boolean isGameDrawn() {
    return gameDrawn;
  }

  public ArrayList<ArrayList<OXOPlayer>> cellGetter() {
    return cells;
  }

  public ArrayList<ArrayList<OXOPlayer>> cellSetter() {
    return cells;
  }
}
