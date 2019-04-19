package teamwork;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class AppTests {

    @Autowired
    private RequestMappingHandlerMapping mappings;
    @Autowired
    private EntityManagerFactory emf;

    /**
     * 打印所有mvc映射路径和对应的controller#method
     */
    @Test
    public void printMvcMappings() {
        mappings.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            System.out.println(requestMappingInfo + "\t-->\t" + handlerMethod.getBeanType().getSimpleName() + "#" + handlerMethod.getMethod().getName());
        });
    }

    /**
     * 打印数据库中Lookup#type常量，需要手动复制到Consts文件
     */
    @Test
    public void printLookupTypes() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<String> query = em.createQuery("select distinct type from Lookup order by type", String.class);
        query.getResultList().forEach(type -> System.out.println(String.format("String %s = \"%s\";", type.toUpperCase(), type)));
        em.close();
    }

}
