package edu.uob;

import java.util.ArrayList;

class OXOController {

  private int numOfPlayers;
  private int CurrentPlayerCount;
  OXOModel gameModel;

  public OXOController(OXOModel model) {
    gameModel = model;
    this.numOfPlayers = model.getNumberOfPlayers();
    CurrentPlayerCount = 0;
    gameModel.setCurrentPlayerNumber(CurrentPlayerCount);
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
    if (gameModel.getWinner() == null && !gameModel.isGameDrawn()) {
      //turn string into lowercase
      String commandInput = command.toLowerCase();

      //Blocking invalid length
      if (commandInput.length() != 2) {
        throw new OXOMoveException.InvalidIdentifierLengthException(
          commandInput.length()
        );
      }
      //Blocking invalid character
      char characterInput = commandInput.charAt(0);
      char numberInput = commandInput.charAt(1);
      if (!characterCheck(characterInput)) {
        throw new OXOMoveException.InvalidIdentifierCharacterException(
          OXOMoveException.RowOrColumn.ROW, characterInput);
      }
      if (!digitCheck(numberInput)) {
        throw new OXOMoveException.InvalidIdentifierCharacterException(
          OXOMoveException.RowOrColumn.COLUMN, numberInput);
      }

      //converting character into int
      int PlayerCharInput = (characterInput - 'a');
      int PlayerNumInput = (numberInput - '1');

      //Get current player
      int currentPlayer = gameModel.getCurrentPlayerNumber();
      OXOPlayer current_player = gameModel.getPlayerByNumber(currentPlayer);

      //Blocking player input exceeding 9
      if (PlayerCharInput >= 9) {
        throw new OXOMoveException.OutsideCellRangeException(
          OXOMoveException.RowOrColumn.ROW,
          PlayerCharInput
        );
      }

      //Blocking invalid input: player's input exceed cell
      if (PlayerCharInput >= gameModel.getNumberOfRows()) {
        throw new OXOMoveException.OutsideCellRangeException(
          OXOMoveException.RowOrColumn.ROW,
          PlayerCharInput
        );
      }
      if (PlayerNumInput >= gameModel.getNumberOfColumns()) {
        throw new OXOMoveException.OutsideCellRangeException(
          OXOMoveException.RowOrColumn.COLUMN,
          PlayerNumInput
        );
      }

      //Blocking repetitive entry of a cell
      if (gameModel.getCellOwner(PlayerCharInput, PlayerNumInput) != null) {
        throw new OXOMoveException.CellAlreadyTakenException(
          PlayerCharInput,
          PlayerNumInput
        );
      }

      //claim cell
      gameModel.setCellOwner(PlayerCharInput, PlayerNumInput, current_player);

      //Check if win
      if (winCheck(PlayerCharInput, PlayerNumInput)) {
        gameModel.setWinner(current_player);
      }

      if (drawCheck() && gameModel.getWinner() == null) {
        gameModel.setGameDrawn();
      }

      //switch players
      changingPlayer();
    }
  }

  //Check if first character is a valid character
  public boolean characterCheck(char inputCharacter) {
    if (inputCharacter < 'a' || inputCharacter > 'z') {
      return false;
    } else {
      return true;
    }
  }

  //Check if second character is a valid digit
  public boolean digitCheck(char inputDigit) {
    if (inputDigit < '0' || inputDigit > '9') {
      return false;
    } else {
      return true;
    }
  }

  //Change player
  public void changingPlayer() {
    CurrentPlayerCount++;
    if (CurrentPlayerCount == numOfPlayers) {
      CurrentPlayerCount = 0;
    }
    //set player to new player
    gameModel.setCurrentPlayerNumber(CurrentPlayerCount);
  }

  //adding new row
  public void addRow() {
    gameModel.cellGetter().add(new ArrayList<>());
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();
    for (int i = 0; i < current_col; i++) {
      gameModel.cellGetter().get(current_row - 1).add(null);
    }
  }

  //remove row
  public void removeRow() {
    int current_col = gameModel.getNumberOfColumns();
    gameModel.cellGetter().remove(current_col - 1);
  }

  //adding new column
  public void addColumn() {
    int current_row = gameModel.getNumberOfRows();
    for (int i = 0; i < current_row; i++) {
      gameModel.cellGetter().get(i).add(null);
    }
  }

  //remove column
  public void removeColumn() {
    int current_row = gameModel.getNumberOfRows();
    for(int i = 0; i< current_row; i++){
      gameModel.cellGetter().get(i).remove(current_row -1);
    }
  }

  //Check if win
  private boolean winCheck(int row, int col) {
    if (
      horizontalCheck(row) || verticalCheck(col) || LeftDiagonalCheck(row, col) || RightDiagonalCheck(row, col)) {
      return true;
    }
    return false;
  }

  //Check horizontally
  private boolean horizontalCheck(int row) {
    int matchCount = 0;
    int current_col = gameModel.getNumberOfColumns();

    for (int i = 0; i < current_col; i++) {
      if (gameModel.getCellOwner(row, i) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount++;
      } else {
        matchCount = 0;
      }
      if (matchCount >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    return false;
  }

  //Check vertically
  private boolean verticalCheck(int col) {
    int matchCount = 0;
    int current_row = gameModel.getNumberOfRows();
    for (int i = 0; i < current_row; i++) {
      if (gameModel.getCellOwner(i, col) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount++;
      } else {
        matchCount = 0;
      }
      if (matchCount >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    return false;
  }

  //Check from left to right in diagonal
  private boolean LeftDiagonalCheck(int row, int col) {
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();
    int matchCount_1 = 0;
    int matchCount_2 = 0;

    //Check from top to current cell
    for (int x = row, y = col; x >= 0 && y >= 0; x--, y--) {
      if (gameModel.getCellOwner(x, y) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount_1++;
      } else {
        matchCount_1 = 0;
      }

      if (matchCount_1 >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    //Check from bottom to current cell
    for (int i = row, j = col; i < current_row && j < current_col; i++, j++) {
      if (gameModel.getCellOwner(i, j) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount_2++;
      } else {
        matchCount_2 = 0;
      }
      if (matchCount_2 >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    return false;
  }

  //Check from right to left in diagonal
  private boolean RightDiagonalCheck(int row, int col) {
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();
    int matchCount_1 = 0;
    int matchCount_2 = 0;

    //Check from top to current cell
    for (int x = row, y = col; x >= 0 && y < current_col; x--, y++) {
      if (gameModel.getCellOwner(x, y) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount_1++;
      } else {
        matchCount_1 = 0;
      }
      if (matchCount_1 >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    //Check from bottom to current cell
    for (int i = row, j = col; i < current_row && j >= 0; i++, j--) {
      if (gameModel.getCellOwner(i, j) == gameModel.getPlayerByNumber(CurrentPlayerCount)) {
        matchCount_2++;
      } else {
        matchCount_2 = 0;
      }
      if (matchCount_2 >= gameModel.getWinThreshold()) {
        return true;
      }
    }
    return false;
  }

  //Command to increase win threshold
  public void increaseWinThreshold() {
    int current_threshold = gameModel.getWinThreshold();
    current_threshold++;
    gameModel.setWinThreshold(current_threshold);
  }

  //Command to decrease win threshold
  public void decreaseWinThreshold() {
    int current_threshold = gameModel.getWinThreshold();
    current_threshold--;
    gameModel.setWinThreshold(current_threshold);
    if (drawCheckAgain()) {
      gameModel.setGameDrawn();
      gameModel.setWinner(null);
    } else {
      afterDecreaseCheck();
    }
  }

  //After decreasing threshold, check the winner
  public void afterDecreaseCheck() {
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();
    int current = CurrentPlayerCount;
    for (int k = 0; k < gameModel.getNumberOfPlayers(); k++) {
      CurrentPlayerCount = k;
      for (int i = 0; i < current_row; i++) {
        for (int j = 0; j < current_col; j++) {
          if (winCheck(i, j)) {
            if (gameModel.getWinner() == null) {
              gameModel.setWinner(gameModel.getPlayerByNumber(k));
            }
          }
        }
      }
    }
    CurrentPlayerCount = current;
  }

  //After decreasing threshold, check if there is draw already on the board
  public boolean drawCheckAgain() {
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();
    int count = 0;
    for (int k = 0; k < gameModel.getNumberOfPlayers(); k++) {
      for (int n = 0; n < current_row; n++) {
        for (int m = 0; m < current_col; m++) {
          if (gameModel.getCellOwner(n, m) != null) {
            if (gameModel.getPlayerByNumber(k).getPlayingLetter() == gameModel.getCellOwner(n, m).getPlayingLetter()) {
              if (winCheck(n, m)) {
                count++;
              }
            }
          }
        }
      }
    }

    if (count > 1) {
      return true;
    }
    return false;
  }

  //Check if draw
  public boolean drawCheck() {
    int current_row = gameModel.getNumberOfRows();
    int current_col = gameModel.getNumberOfColumns();

    for (int i = 0; i < current_row; i++) {
      for (int j = 0; j < current_col; j++) {
        if (gameModel.getCellOwner(i, j) == null) {
          return false;
        }
      }
    }
    return true;
  }

}
