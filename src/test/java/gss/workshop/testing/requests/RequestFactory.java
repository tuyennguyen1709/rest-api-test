package gss.workshop.testing.requests;

import static gss.workshop.testing.utils.RestUtils.addParams;

import gss.workshop.testing.tests.TestBase;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestFactory extends TestBase {

  private static final Logger logger = Logger.getLogger(String.valueOf(RequestFactory.class));
  private static HashMap<String, String> params = addParams(Map.of("key", key, "token", token));

  // -------------------Board-------------------

  /**
   * Send request to create a new board
   *
   * @param boardName expected board name
   * @return Response of the request
   */
  public static Response createBoard(String boardName) {
    logger.info("Creating a new board.");
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", boardName)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res =
        RestClient.doPostRequestWithParamsAndNoPayload(
            requestPath,
            params); // it calls a method of RestClient "doPostRequestWithParamsAndNoPayload"  to
    // perform the Post request with specific info was prepared.
    logger.info("Finish board creation.");
    return res;
  }

  /**
   * Send request to create a new board without defaultList
   *
   * @param boardName expected board name
   * @param defaultList a board without/with default list
   * @return Response of the request
   */
  public static Response createBoard(String boardName, boolean defaultList) {
    logger.info("Creating a new board.");
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", boardName, "defaultLists", false)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("Create %s is successfully", boardName));
    return res;
  }

  /**
   * Get info of an existing board by its Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response getBoardById(String boardId) {
    logger.info(String.format("Get the board by id %s", boardId));
    HashMap<String, String> params = setCommonQueryParams();
    String requestPath = String.format(prop.getProperty("getBoardPath"), version, boardId);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info(String.format("Get the board by id %s is successfully", boardId));
    return res;
  }

  /**
   * Delete an existing board by Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response deleteBoard(String boardId) {
    logger.info(String.format("Delete the board by id %s", boardId));
    HashMap<String, String> params = setCommonQueryParams();
    String requestPath = String.format(prop.getProperty("deleteBoardPath"), version, boardId);
    Response res = RestClient.doDeleteRequestWithParams(requestPath, params);
    logger.info(String.format("Delete the board by id %s is successfully", boardId));
    return res;
  }

  // -------------------List-------------------

  /**
   * Create a new list in an existing board
   *
   * @param boardId the board id which to be added more list
   * @param listName name of the new list created
   * @return Response of the request
   */
  public static Response createList(String boardId, String listName) {
    logger.info(String.format("Create the new list: %s", listName));
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", listName, "idBoard", boardId)));
    String requestPath = String.format(prop.getProperty("createListPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("Create the %s list is successfully", listName));
    return res;
  }

  // -------------------Card-------------------

  /**
   * @param taskName
   * @param listId
   * @return
   */
  public static Response createCard(String taskName, String listId) {
    logger.info(String.format("Create %s on listId %s", taskName, listId));
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", taskName, "idList", listId)));
    String requestPath = String.format(prop.getProperty("createCardPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("Create %s on listId %s is successfully", taskName, listId));
    return res;
  }

  /**
   * @param cardId
   * @param listId
   * @return
   */
  public static Response updateCard(String cardId, String listId) {
    logger.info(String.format("Move %s to listId %s", cardId, listId));
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("idList", listId)));
    String requestPath = String.format(prop.getProperty("updateCardPath"), version, cardId);
    Response res = RestClient.doPutRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("Move %s to listId %s is successfully", cardId, listId));
    return res;
  }
  
    /**
   * Commom method to set key and token
   */
    private static HashMap setCommonQueryParams() {
    HashMap<String, String> params = addParams(Map.of("key", key,"token", token));
    return params;
  }
}
