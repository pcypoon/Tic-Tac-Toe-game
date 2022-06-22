package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {

  OXOModel model;
  OXOModel addRowModel;
  OXOModel addColModel;
  OXOModel model_addRowaddCol;
  OXOModel model_minusThreshold;
  OXOModel model_minusDraw;
  OXOModel model_MouseEditColRow;
  OXOModel model_EditThres;

  OXOController controller;
  OXOController addRowcontroller;
  OXOController addColcontroller;
  OXOController addRowaddColcontroller;
  OXOController minusThresholdController;
  OXOController minusDrawController;
  OXOController mouseEditColRowController;
  OXOController editThresController;


  // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
  // and O player
  private static OXOModel createStandardModel() {
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    return model;
  }

  private static OXOModel createAddRowModel(){
    OXOModel addRowModel = new OXOModel(4,3,4);
    addRowModel.addPlayer(new OXOPlayer('X'));
    addRowModel.addPlayer(new OXOPlayer('O'));
    return addRowModel;
  }

  private static OXOModel createAddColModel(){
    OXOModel addColModel = new OXOModel(3,4,4);
    addColModel.addPlayer(new OXOPlayer('X'));
    addColModel.addPlayer(new OXOPlayer('O'));
    return addColModel;
  }

  private static OXOModel createAddRowAddColModel(){
    OXOModel addRowaddColModel = new OXOModel(4, 4,4);
    addRowaddColModel.addPlayer(new OXOPlayer('X'));
    addRowaddColModel.addPlayer(new OXOPlayer('O'));
    return addRowaddColModel;
  }

  private static OXOModel createMinusThresModel(){
    OXOModel model_minusThreshold = new OXOModel(3,3,2);
    model_minusThreshold.addPlayer(new OXOPlayer('X'));
    model_minusThreshold.addPlayer(new OXOPlayer('O'));
    return model_minusThreshold;
  }

  private static OXOModel createMinusDrawModel(){
    OXOModel model_minusDraw = new OXOModel(4,4,3);
    model_minusDraw.addPlayer(new OXOPlayer('X'));
    model_minusDraw.addPlayer(new OXOPlayer('O'));
    return model_minusDraw;
  }

  private static OXOModel createMouseModel(){
    OXOModel model_mouseEditColRow = new OXOModel(3,3,3);
    model_mouseEditColRow.addPlayer(new OXOPlayer('X'));
    model_mouseEditColRow.addPlayer(new OXOPlayer('O'));
    return model_mouseEditColRow;
  }

  private static OXOModel createEditThresModel(){
    OXOModel model_EditThres = new OXOModel(3,3,3);
    model_EditThres.addPlayer(new OXOPlayer('X'));
    model_EditThres.addPlayer(new OXOPlayer('O'));
    return model_EditThres;
  }

  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    model = createStandardModel();
    addRowModel = createAddRowModel();
    addColModel = createAddColModel();
    model_addRowaddCol = createAddRowAddColModel();
    model_minusThreshold = createMinusThresModel();
    model_minusDraw = createMinusDrawModel();
    model_MouseEditColRow = createMouseModel();
    model_EditThres = createEditThresModel();

    controller = new OXOController(model);
    addRowcontroller = new OXOController(addRowModel);
    addColcontroller = new OXOController(addColModel);
    addRowaddColcontroller = new OXOController(model_addRowaddCol);
    minusThresholdController = new OXOController(model_minusThreshold);
    minusDrawController = new OXOController(model_minusDraw);
    mouseEditColRowController = new OXOController(model_MouseEditColRow);
    editThresController = new OXOController(model_EditThres);
  }

  // here's a basic test for the `controller.handleIncomingCommand` method
  @Test
  void testHandleIncomingCommand() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(
      model.getCurrentPlayerNumber()
    );
    controller.handleIncomingCommand("a1");

    // A move has been made for A1 (i.e. the [0,0] cell on the board), let's see if that cell is
    // indeed owned by the player
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
  }

  // here's a complete game where we find out if someone won
  @Test
  void testBasicWinWithA1A2A3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(
      model.getCurrentPlayerNumber()
    );
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a3");

    // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
      firstMovingPlayer,
      model.getWinner(),
      "Winner was expected to be %s but wasn't".formatted(
          firstMovingPlayer.getPlayingLetter()
        )
    );
  }

  //test to see adding new player is possible or not
  @Test
  void testadditionalPlayer() {
    model.addPlayer(new OXOPlayer('Z'));
  }


  //test the addRow function
  @Test
  void testaddRowWin() throws OXOMoveException{

    OXOPlayer second = addRowModel.getPlayerByNumber(addRowModel.getCurrentPlayerNumber()+1);

    addRowcontroller.handleIncomingCommand("a1");
    addRowcontroller.handleIncomingCommand("d1");
    addRowcontroller.handleIncomingCommand("a2");
    addRowcontroller.handleIncomingCommand("c2");
    addRowcontroller.handleIncomingCommand("d3");
    addRowcontroller.handleIncomingCommand("b3");

    assertEquals(second, addRowModel.getWinner(), "Winner was expected to be %s but wasn't".formatted(
            second.getPlayingLetter()
    ));
  }

@Test
  void testaddColWin() throws OXOMoveException{

    OXOPlayer first = addColModel.getPlayerByNumber(addColModel.getCurrentPlayerNumber());

    addColcontroller.handleIncomingCommand("a1");
    addColcontroller.handleIncomingCommand("b4");
    addColcontroller.handleIncomingCommand("a3");
    addColcontroller.handleIncomingCommand("c2");
    addColcontroller.handleIncomingCommand("a4");
    addColcontroller.handleIncomingCommand("b3");
    addColcontroller.handleIncomingCommand("a2");

    assertEquals(first, addColModel.getWinner(),"Winner was expected to be %s but wasn't".formatted(
            first.getPlayingLetter()
    ));
  }

  @Test
  void testaddRowaddColWin() throws OXOMoveException{

    OXOPlayer first = model_addRowaddCol.getPlayerByNumber(model_addRowaddCol.getCurrentPlayerNumber());

    addRowaddColcontroller.handleIncomingCommand("a1");
    addRowaddColcontroller.handleIncomingCommand("b3");
    addRowaddColcontroller.handleIncomingCommand("b2");
    addRowaddColcontroller.handleIncomingCommand("c2");
    addRowaddColcontroller.handleIncomingCommand("c3");
    addRowaddColcontroller.handleIncomingCommand("d1");
    addRowaddColcontroller.handleIncomingCommand("d4");

    assertEquals(first, model_addRowaddCol.getWinner(),"Winner was expected to be %s but wasn't".formatted(
            first.getPlayingLetter()
    ));
  }

  @Test
  void minusThresTest() throws OXOMoveException{

    OXOPlayer first = model_minusThreshold.getPlayerByNumber(model_minusThreshold.getCurrentPlayerNumber());

    minusThresholdController.handleIncomingCommand("b2");
    minusThresholdController.handleIncomingCommand("b1");
    minusThresholdController.handleIncomingCommand("c3");

    assertEquals(first,model_minusThreshold.getWinner(),"Winner was expected to be %s but wasn't".formatted(
            first.getPlayingLetter()
    ));
  }

  @Test
  void minusDrawTest() throws OXOMoveException{
    minusDrawController.handleIncomingCommand("b2");
    minusDrawController.handleIncomingCommand("b1");
    minusDrawController.handleIncomingCommand("c3");
    minusDrawController.handleIncomingCommand("c1");
    minusDrawController.decreaseWinThreshold();
    assert (minusDrawController.drawCheckAgain());
    assert (model_minusThreshold.getWinner()== null);
  }

 @Test
 void MouseEditRowCol(){
    mouseEditColRowController.removeRow();
    mouseEditColRowController.removeColumn();
    assert (model_MouseEditColRow.getNumberOfRows() == 2);
    assert (model_MouseEditColRow.getNumberOfColumns() == 2);

    mouseEditColRowController.addRow();
    mouseEditColRowController.addColumn();
    assert (model_MouseEditColRow.getNumberOfRows() == 3);
    assert (model_MouseEditColRow.getNumberOfColumns() == 3);
 }

 @Test
 void EditThres() throws OXOMoveException{
    editThresController.addRow();
    editThresController.addColumn();
    editThresController.handleIncomingCommand("d1");
    editThresController.handleIncomingCommand("d4");
    editThresController.handleIncomingCommand("c2");
    editThresController.handleIncomingCommand("c3");
    editThresController.increaseWinThreshold();
    editThresController.handleIncomingCommand("b3");
    editThresController.handleIncomingCommand("b2");
    editThresController.decreaseWinThreshold();
    assert (editThresController.drawCheckAgain());
    assert (model_EditThres.getWinner()== null);
 }

@Test
 void charNumTest(){
  assert (controller.characterCheck('e'));
  assert (!controller.characterCheck('?'));
  assert (controller.digitCheck('0'));
  assert (controller.digitCheck('%'));

  assert (addRowcontroller.characterCheck('f'));
  assert (!addRowcontroller.characterCheck('1'));
  assert (addRowcontroller.digitCheck('5'));
  assert (!addRowcontroller.digitCheck('@'));

  assert (addColcontroller.characterCheck('h'));
  assert (!addColcontroller.characterCheck('1'));
  assert (addColcontroller.digitCheck('8'));
  assert (!addColcontroller.digitCheck('d'));

  assert (addRowaddColcontroller.characterCheck('a'));
  assert (!addRowaddColcontroller.characterCheck('!'));
  assert (addRowaddColcontroller.digitCheck('3'));
  assert (!addRowaddColcontroller.digitCheck('g'));
  }

}
