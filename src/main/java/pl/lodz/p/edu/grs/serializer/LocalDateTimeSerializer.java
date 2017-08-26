package pl.lodz.p.edu.grs.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(final LocalDateTime date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
            throws IOException {
        String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);
        jsonGenerator.writeString(formattedDate);
    }

}