package com.credit.suisse.event.processor.reader;

import java.io.IOException;

public interface EventLogReader<T> {

    void open();

    boolean hasNext();

    T read();

    void close() throws IOException;
}
