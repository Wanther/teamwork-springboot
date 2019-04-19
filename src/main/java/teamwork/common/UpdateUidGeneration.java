package teamwork.common;

import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;

public class UpdateUidGeneration implements AnnotationValueGeneration<UpdateUid> {
    @Override
    public void initialize(UpdateUid annotation, Class<?> propertyType) {

    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.ALWAYS;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return new CreationUidGeneration.AuditorGenerator();
    }

    @Override
    public boolean referenceColumnInSql() {
        return false;
    }

    @Override
    public String getDatabaseGeneratedReferencedColumnValue() {
        return null;
    }
}
