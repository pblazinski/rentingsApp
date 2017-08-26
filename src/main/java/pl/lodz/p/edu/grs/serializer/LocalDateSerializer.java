package pl.lodz.p.edu.grs.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(final LocalDate date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
            throws IOException {
        String formattedDate = date.format(DateTimeFormatter.ISO_DATE);
        jsonGenerator.writeString(formattedDate);
    }

}