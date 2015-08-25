package ru.fireway.ws;

/**
 * Created by Александр on 06.07.2015.
 */
public class Stats {

    private long rCountClients = 0;
    private long wCountClients = 0;
    private long startMillis;

    public Stats() {
        startMillis = System.currentTimeMillis();
    }

    public void incReaderR() {
        rCountClients++;
    }

    public void incWriterR() {
        wCountClients++;
    }

    public long getTotalRequests() {
        return rCountClients + wCountClients;
    }

    public long getTotalReaders() {
        return rCountClients;
    }

    public long getTotalWriters() {
        return wCountClients;
    }

    public long getRps() {
        long currentMillis = System.currentTimeMillis() - startMillis;
        double rps = 1000 * (rCountClients + wCountClients) / currentMillis;
        return (long)rps;
    }

    public void resetStat() {
        startMillis = System.currentTimeMillis();
        rCountClients = 0;
        wCountClients = 0;
    }
}
