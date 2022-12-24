package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

public class InstantAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        jsonWriter.value(instant == null ? 0 : instant.toEpochMilli());
    }

    @Override
    public Instant read(JsonReader jsonReader) throws IOException {
        long millis = Long.parseLong(jsonReader.nextString());
        return millis == 0 ? null : Instant.ofEpochMilli(millis);
    }
}
