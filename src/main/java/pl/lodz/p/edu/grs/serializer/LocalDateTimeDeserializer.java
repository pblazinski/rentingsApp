package pl.lodz.p.edu.grs.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException {
        String date = jsonParser.getText();
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

}