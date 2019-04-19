package teamwork.common;

import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ValueGenerationType(generatedBy = CreationUidGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreationUid {
}
