package pl.icevan;

import java.util.Objects;

public class Record implements Comparable<Record> {
    private final String home;

    private final String away;

    private final Integer scoreHome;

    private final Integer scoreAway;

    private final Long createdAt;

    public Record(String home, String away) {
        this(home, away, 0, 0);
    }

    public Record(String home, String away, Integer scoreHome, Integer scoreAway) {
        if (scoreHome == null || scoreAway == null) {
            throw  new RecordException("Invalid use of record. Scores must be non-null and non-empty");
        }
        this.home = home;
        this.away = away;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.createdAt = System.nanoTime();
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }

    public Integer getScoreHome() {
        return scoreHome;
    }

    public Integer getScoreAway() {
        return scoreAway;
    }

    public Integer getCombinedScore() {
        return getScoreHome() + getScoreAway();
    }

    public Record newScore(Integer scoreHome, Integer scoreAway) {
        if (scoreHome == null || scoreAway == null) {
            throw  new RecordException("Invalid use of record. Scores must be non-null and non-empty");
        }
        return (!scoreHome.equals(getScoreHome()) || !scoreAway.equals(getScoreAway())) ?
                new Record(home, away, scoreHome, scoreAway) : this;
    }

    public static int getHashFromValues(String home, String away){
        return Objects.hash(home, away);
    }

    @Override
    public int compareTo(Record o) {
        int cmp = Integer.compare(o.getCombinedScore().byteValue(), getCombinedScore());
        if (cmp != 0) return cmp;

        cmp = Long.compare(o.createdAt, createdAt);
        if (cmp != 0) return cmp;

        cmp = String.CASE_INSENSITIVE_ORDER.compare(o.getHome(), getHome());
        if (cmp != 0) return cmp;

        return String.CASE_INSENSITIVE_ORDER.compare(o.getAway(), getAway());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(home, record.home) && Objects.equals(away, record.away);
    }

    @Override
    public int hashCode() {
        return Objects.hash(home, away);
    }

    @Override
    public String toString() {
        return String.format("%s %d - %s %d", home, scoreHome, away, scoreAway);
    }
}
