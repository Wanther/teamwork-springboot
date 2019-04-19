package teamwork.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageJacksonSerializer extends JsonSerializer<Page> {
    @Override
    public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("total", value.getTotalElements());
        gen.writeNumberField("last_page", value.getTotalPages());
        serializers.defaultSerializeField("data", value.getContent(), gen);
        gen.writeEndObject();
    }
}
