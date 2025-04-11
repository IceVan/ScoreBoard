import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.icevan.Record;
import pl.icevan.RecordException;

import static org.junit.jupiter.api.Assertions.*;

public class RecordTest {

    @ParameterizedTest
    @CsvSource({
            "'Mexico', 'Canada', 0, 5, 'Mexico 0 - Canada 5'",
            "'Germany', 'France', 2, 2, 'Germany 2 - France 2'",
            "'Argentina', 'Canada', 3, 1, 'Argentina 3 - Canada 1'",
    })
    public void testPrint(String home, String away, int scoreHome, int scoreAway, String expected) {
        var record = new Record(home, away, scoreHome, scoreAway);

        assertEquals(expected, record.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "'Mexico', 'Canada', 0, 5, 'Mexico', 'Canada', 0, 5, true",
            "'Mexico', 'Canada', 0, 5, 'Mexico', 'Canada', 1, 5, true",
            "'Germany', 'France', 2, 2, 'Germany', 'Canada', 2, 2, false",
            "'Argentina', 'Canada', 3, 1, 'Germany', 'Mexico', 3, 1, false",
    })
    public void testEquals(String home, String away, int scoreHome, int scoreAway,
                           String home2, String away2, int scoreHome2, int scoreAway2, Boolean expected) {
        var record = new Record(home, away, scoreHome, scoreAway);
        var record2 = new Record(home2, away2, scoreHome2, scoreAway2);
        assertEquals(expected, record2.equals(record));
    }

    @ParameterizedTest
    @CsvSource({
            "'Mexico', 'Canada', 0, 5, 1, 5, 6",
            "'Germany', 'France', 2, 2, 1, 2, 3",
            "'Argentina', 'Canada', 3, 0, 0, 3, 3",
    })
    public void testNewDifferentScores(String home, String away, int initialScoreHome, int initialScoreAway,
                                         int newScoreHome, int newScoreAway, int expectedCombinedScore) {
        var record = new Record(home, away, initialScoreHome, initialScoreAway);
        var record2 = record.newScore(newScoreHome, newScoreAway);

        assertFalse(record2 == record);
        assertEquals(expectedCombinedScore, record2.getCombinedScore());
    }

    @ParameterizedTest
    @CsvSource({
            "'Mexico', 'Canada', 0, 5, 0, 5, 5",
            "'Germany', 'France', 2, 2, 2, 2, 4"
    })
    public void testNewSameScores(String home, String away, int initialScoreHome, int initialScoreAway,
                                         int newScoreHome, int newScoreAway, int expectedCombinedScore) {
        var record = new Record(home, away, initialScoreHome, initialScoreAway);
        var record2 = record.newScore(newScoreHome, newScoreAway);

        assertEquals(record2, record);
        assertEquals(expectedCombinedScore, record2.getCombinedScore());
    }

    @Test
    public void assertNullScoreConstructorShouldThrowRecordException(){
        assertThrows(RecordException.class , () -> new Record("Mexico", "Canada", 2, null));
    }

    @Test
    public void assertNullNewScoreShouldThrowRecordException(){
        var record = new Record("Mexico", "Canada", 2, 1);
        assertThrows(RecordException.class , () -> record.newScore(null, 3));
    }
}
