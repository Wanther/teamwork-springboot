package teamwork.common;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Set;

public class AttributeConverters {

    @Converter
    public static class CommaStringToSetConverter implements AttributeConverter<Set, String> {

        @Override
        public Set convertToEntityAttribute(String attribute) {
            return StringUtils.commaDelimitedListToSet(attribute);
        }

        @Override
        public String convertToDatabaseColumn(Set attribute) {
            return StringUtils.collectionToCommaDelimitedString(attribute);
        }
    }
}
