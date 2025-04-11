import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.icevan.ScoreBoard;
import pl.icevan.ScoreBoardException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void testStartGame() {
        scoreBoard.startGame("Mexico", "Canada");
        String summary = scoreBoard.getSummary();
        assertTrue(summary.contains("Mexico 0 - Canada 0"), "New game should be at 0-0.");
    }

    @Test
    void testUpdateScore() {
        scoreBoard.startGame("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);
        String summary = scoreBoard.getSummary();
        assertTrue(summary.contains("Spain 10 - Brazil 2"), "Score should be updated to 10-2.");
    }

    @Test
    void testFinishGame() {
        scoreBoard.startGame("Germany", "France");
        scoreBoard.updateScore("Germany", "France", 2, 2);
        scoreBoard.finishGame("Germany", "France");
        String summary = scoreBoard.getSummary();
        assertFalse(summary.contains("Germany - France"), "Game should be removed from summary after finishing.");
    }

    @Test
    void testSummaryOrdering() {
        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.startGame("Spain", "Brazil");
        scoreBoard.startGame("Germany", "France");
        scoreBoard.startGame("Uruguay", "Italy");
        scoreBoard.startGame("Argentina", "Australia");

        scoreBoard.updateScore("Mexico", "Canada", 0, 5);
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);
        scoreBoard.updateScore("Germany", "France", 2, 2);
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        String summary = scoreBoard.getSummary();
        List<String> lines = Arrays.asList(summary.split(System.lineSeparator()));

        assertTrue(lines.get(0).contains("Uruguay"),
                "The most recent game state with 12 points should be first.");
        assertTrue(lines.get(1).contains("Spain"),
                "The older game state with 12 points be second.");

        int indexArgentinaAustralia = lines.indexOf(lines.stream().filter(l -> l.contains("Argentina")).findFirst().orElse(""));
        int indexGermanyFrance = lines.indexOf(lines.stream().filter(l -> l.contains("Germany")).findFirst().orElse(""));
        assertTrue(indexArgentinaAustralia < indexGermanyFrance, "The most recent game with the same score should appear first.");
    }

    @Test
    void testErrorOnDuplicateStartShouldThrowScoreBoardException() {
        scoreBoard.startGame("Canada", "San Escobar");
        Exception exception = assertThrows(ScoreBoardException.class, () ->
                scoreBoard.startGame("Canada", "San Escobar"));
        assertEquals("Game already exists.", exception.getMessage());
    }

    @Test
    void testErrorOnUpdateNonExistingGameShouldThrowScoreBoardException() {
        Exception exception = assertThrows(ScoreBoardException.class, () ->
                scoreBoard.updateScore("Fake team", "Fake team 2", 1, 1));
        assertEquals("Record not found. You should start game first.", exception.getMessage());
    }

    @Test
    void testErrorOnFinishNonExistingGameShouldThrowScoreBoardException() {
        Exception exception = assertThrows(ScoreBoardException.class, () ->
                scoreBoard.finishGame("Fake team", "Fake team 2"));
        assertEquals("Record not found.", exception.getMessage());
    }
}
