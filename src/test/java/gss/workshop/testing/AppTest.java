package gss.workshop.testing;

import static org.junit.Assert.assertTrue;

import gss.workshop.testing.utils.ConvertUtils;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/** Unit test for simple App. */
public class AppTest {
  /** Rigorous Test :-) */
  @Test
  public void shouldAnswerWithTrue() throws IOException {
    ConvertUtils.convertJsonToJavaClass(
        new File("src/test/resources/SamplePayloads/BoardDeletionSuccessPayload.json")
            .toURI()
            .toURL(),
        new File("src/main/java/gss/workshop/testing/pojo"),
        "board",
        "BoardDeletionSuccessRes");
    assertTrue(true);
  }
}
