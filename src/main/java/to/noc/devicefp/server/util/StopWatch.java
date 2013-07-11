/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

public final class StopWatch {

    private long startTime;

    public StopWatch() {
        restart();
    }

    public void restart() {
        startTime = System.currentTimeMillis();
    }

    public long elapsedMs() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public String toString() {
        return "elapsed ms=" + elapsedMs();
    }
}
