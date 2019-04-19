package teamwork.common;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanMerger {

    private static final Collection<String> DEFAULT_EXCLUDE = Collections.unmodifiableCollection(BaseEntity.SYSTEM_FIELDS);

    public static BeanMerger from(Object from) {
        return new BeanMerger(from);
    }

    private Object from;
    private Set<String> includes;
    private Set<String> excludes;
    private boolean includeNull;
    private boolean disableDefaultExclude;

    BeanMerger(Object from) {
        this.from = from;
    }

    public BeanMerger include(String... fields) {
        if (includes == null) {
            includes = new HashSet<>();
        }

        includes.addAll(Arrays.asList(fields));

        return this;
    }

    public BeanMerger exclude(String... fields) {
        if (excludes == null) {
            excludes = new HashSet<>();
        }
        excludes.addAll(Arrays.asList(fields));
        return this;
    }

    public BeanMerger includeNull() {
        includeNull = true;
        return this;
    }

    /**
     * 默认忽略 {@link BaseEntity#SYSTEM_FIELDS}，如果想包含进来，调用此方法
     * @return
     */
    public BeanMerger disableDefaultExclude() {
        disableDefaultExclude = true;
        return this;
    }

    public <T> T copyTo(T target) {
        copyProperties(target);
        return target;
    }

    private boolean shouldInclude(String fieldName) {
        if (CollectionUtils.isEmpty(includes) || includes.contains(fieldName)) {
            if (disableDefaultExclude || !DEFAULT_EXCLUDE.contains(fieldName)) {
                return CollectionUtils.isEmpty(excludes) || !excludes.contains(fieldName);
            }
        }

        return false;
    }

    private void copyProperties(Object target) {
        Assert.notNull(from, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {

            Method writeMethod = targetPd.getWriteMethod();

            if (writeMethod != null && shouldInclude(targetPd.getName())) {

                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(from.getClass(), targetPd.getName());

                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(from);

                            if (includeNull || value != null) {
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }

                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }

        }
    }
}
