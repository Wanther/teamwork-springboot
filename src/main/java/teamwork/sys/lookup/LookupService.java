package teamwork.sys.lookup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import teamwork.common.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * type与value确认唯一值text
 * <table>
 *     <thead>
 *         <tr>
 *             <td>type</td><td>value</td><td>text</td>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>gender</td><td>0</td><td>未知</td>
 *         </tr>
 *         <tr>
 *             <td>gender</td><td>1</td><td>男</td>
 *         </tr>
 *         <tr>
 *             <td>gender</td><td>2</td><td>女</td>
 *         </tr>
 *     </tbody>
 * </table>
 * @see Lookup
 */
public interface LookupService {

    /**
     * @param type not null
     * @param value not null
     * @return type和value对应的值，如果没有查询到，返回{@code value}
     * @throws ServiceException
     */
    String lookup(String type, String value) throws ServiceException;

    /**
     * 保存/更新
     * @param lookup
     * @return 更新后的值
     * @throws ServiceException
     */
    Lookup save(Lookup lookup) throws ServiceException;

    /**
     * 删除/批量删除
     * @param ids
     * @throws ServiceException
     */
    void delete(List<Long> ids) throws ServiceException;

    /**
     * 查询（分页）
     * @param condition 按照Lookup构建查询条件
     * @param pageable 分页参数 {@link org.springframework.data.domain.Pageable}
     * @return
     * @throws ServiceException
     *
     * @see org.springframework.data.jpa.domain.Specification
     * @see org.springframework.data.domain.Pageable
     * @see org.springframework.data.domain.Page
     * @see teamwork.common.PageJacksonSerializer
     */
    Page<Lookup> query(Specification<Lookup> condition, Pageable pageable) throws ServiceException;

    /**
     * 查询所有
     * @param condition
     * @return
     * @throws ServiceException
     *
     * @see Specification
     * @see teamwork.common.SpecificationUtils
     */
    List<Lookup> query(Specification<Lookup> condition) throws ServiceException;

    /**
     * @param id
     * @return
     * @throws ServiceException
     */
    Optional<Lookup> findById(Long id) throws ServiceException;
}
