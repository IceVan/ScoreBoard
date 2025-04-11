package pl.icevan;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class ScoreBoard {

    private final ConcurrentSkipListSet<Record> scores = new ConcurrentSkipListSet<>();
    private final ConcurrentHashMap<Integer, Record> records = new ConcurrentHashMap<>();

    public void startGame(String home, String away) {
        var record = new Record(home, away);
        if(!records.containsKey(record.hashCode())) {
            records.put(record.hashCode(), record);
            scores.add(record);
        } else {
            throw new ScoreBoardException("Game already exists.");
        }
    }

    public void finishGame(String home, String away) {
        var recordHash = Record.getHashFromValues(home, away);
        if(records.containsKey(recordHash)) {
            var record = records.get(recordHash);
            scores.remove(record);
            records.remove(recordHash);
        } else {
            throw new ScoreBoardException("Record not found.");
        }
    }

    public void updateScore(String home, String away, int homeScore, int awayScore) {
        var recordHash = Record.getHashFromValues(home, away);
        if(records.containsKey(recordHash)) {
            var record = records.get(recordHash);
            scores.remove(record);
            var newRecord = record.newScore(homeScore, awayScore);
            scores.add(newRecord);
            records.put(recordHash, newRecord);
        } else {
            throw new ScoreBoardException("Record not found. You should start game first.");
        }
    }

    public String getSummary(){
        return scores.stream().map(Record::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
