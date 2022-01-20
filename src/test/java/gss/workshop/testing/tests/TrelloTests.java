package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomBoardName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, 200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
    Response resTODOList = RequestFactory.createList(boardId, "TODO");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resTODOList, 200);

    // VP. Validate a list is created: name of list is TODO, "closed" property is false
    ListCreationRes TODOPojo =
         ConvertUtils.convertRestResponseToPojo(resTODOList, ListCreationRes.class);
    ValidationUtils.validateStringEqual("TODO", TODOPojo.getName());
    ValidationUtils.validateStringEqual("false", TODOPojo.getClosed().toString());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, TODOPojo.getIdBoard());

    // -> Store TODOList Id
    String TODOListId = TODOPojo.getId();
    System.out.println(String.format("TODOList Id of the  Board %s is %s", boardId, TODOListId));


    // 3. Create a DONE list
    Response resDONEList = RequestFactory.createList(boardId, "DONE");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDONEList, 200);

    // VP. Validate a list is created: name of list is DONE, "closed" property is false
    ListCreationRes DONEPojo =
          ConvertUtils.convertRestResponseToPojo(resDONEList, ListCreationRes.class);
    ValidationUtils.validateStringEqual("DONE", DONEPojo.getName());
    ValidationUtils.validateStringEqual("false", DONEPojo.getClosed().toString());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, DONEPojo.getIdBoard());

    // -> Store DONEList Id
    String DONEListId = DONEPojo.getId();
    System.out.println(String.format("DONEList Id of the  Board %s is %s", boardId, DONEListId));

    // 4. Create a new Card in TODO list
    String cardName = OtherUtils.randomCardName();
    Response resCard = RequestFactory.createCard(cardName,TODOListId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCard, 200);

    // VP. Validate a card is created: task name, list id, board id
    CardCreationRes cardPojo =
          ConvertUtils.convertRestResponseToPojo(resCard, CardCreationRes.class);
    ValidationUtils.validateStringEqual(cardName, cardPojo.getName());
    ValidationUtils.validateStringEqual(TODOListId, cardPojo.getIdList());
    ValidationUtils.validateStringEqual(boardId, cardPojo.getIdBoard());

    // VP. Validate the card should have no votes or attachments
    ValidationUtils.validateStringEqual("0", cardPojo.getBadges().getVotes().toString());
    ValidationUtils.validateStringEqual("0", cardPojo.getBadges().getAttachments().toString());

    // -> Store Card Id
    String cardId = cardPojo.getId();
    System.out.println(String.format("Card Id %s is created successfully", cardId));

    // 5. Move the card to DONE list
    Response cardUpdate = RequestFactory.updateCard(cardId, DONEListId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(cardUpdate, 200);

    // VP. Validate the card should have new list: list id
    CardUpdateRes cardUpdatePojo =
          ConvertUtils.convertRestResponseToPojo(cardUpdate, CardUpdateRes.class);
    ValidationUtils.validateStringEqual(DONEListId, cardUpdatePojo.getIdList());

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property
    ValidationUtils.validateStringEqual(cardName, cardUpdatePojo.getName());
    ValidationUtils.validateStringEqual(boardId, cardUpdatePojo.getIdBoard());
    ValidationUtils.validateStringEqual("false", cardUpdatePojo.getClosed().toString());

    // 6. Delete board
    Response boardDelete = RequestFactory.deleteBoard(boardId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(boardDelete, 200);
    //VP. Validate the board is deleted successfully
    Response resGetBoard = RequestFactory.getBoardById(boardId);
    ValidationUtils.validateStatusCode(resGetBoard, 404);

  }
}
