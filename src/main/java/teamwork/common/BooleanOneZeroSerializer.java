package teamwork.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class BooleanOneZeroSerializer extends JsonSerializer<Boolean> {
    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(Boolean.TRUE.equals(value) ? 1 : 0);
    }
}
