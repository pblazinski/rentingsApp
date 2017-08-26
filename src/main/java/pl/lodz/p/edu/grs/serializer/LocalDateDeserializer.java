package pl.lodz.p.edu.grs.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException {
        String date = jsonParser.getText();
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

}