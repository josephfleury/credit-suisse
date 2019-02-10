package com.credit.suisse.event.processor.reader;

import com.credit.suisse.event.processor.data.EventData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Component
public class JsonEventLogReader implements EventLogReader<EventData> {

    private InputStreamReader reader;
    private Gson gson;

    private JsonStreamParser parser;

    @Value("${event.log.filepath}")
    private String filepath;

    @Override
    public void open() {
        log.info("Opening json log file: {}", filepath);
        this.reader = new InputStreamReader(TypeReference.class.getResourceAsStream(filepath));
        this.parser = new JsonStreamParser(reader);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public boolean hasNext() {
        return parser.hasNext();
    }

    @Override
    public EventData read() throws JsonSyntaxException {
        JsonElement jsonElement = parser.next();
        EventData event = gson.fromJson(jsonElement, EventData.class);
        log.debug("Found event: {}", event);
        return event;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        log.info("Closed json log file {}", filepath);
    }

}
