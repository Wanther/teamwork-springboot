package teamwork.common;

/**
 * 业务场景，一般用于校验注解的{@code groups}和 {@link com.fasterxml.jackson.annotation.JsonView}
 *
 * <p>
 *     <a href="https://beanvalidation.org/1.0/spec/">JSR303</a>
 * </p>
 *
 * @see com.fasterxml.jackson.annotation.JsonView
 * @see javax.validation.Validation
 * @see org.hibernate.validator
 */
public interface Scenes {
    /**
     * 新建
     */
    interface Create {}

    /**
     * 更新
     */
    interface Update {}

    /**
     * 概览
     */
    interface Overview {}

    /**
     * 详情
     */
    interface Detail extends Overview {}

    /**
     * 递归输出依赖关系
     */
    interface Recursive extends Detail {}
}
