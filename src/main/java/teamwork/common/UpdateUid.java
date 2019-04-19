package teamwork.common;

import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ValueGenerationType(generatedBy = UpdateUidGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUid {
}
