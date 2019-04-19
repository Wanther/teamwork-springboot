package teamwork.common;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

public class BeanMergerTests {

    @Test
    public void testCopyAll() {
        Entity from = createSource();

        Entity to = new Entity();
        Entity result = BeanMerger.from(from)
                .disableDefaultExclude()
                .copyTo(to);

        Assert.assertSame(to, result);

        Assert.assertEquals(from.getId(), to.getId());
        Assert.assertEquals(from.getCreated(), to.getCreated());
        Assert.assertEquals(from.getCreatedBy(), to.getCreatedBy());
        Assert.assertEquals(from.getUpdated(), to.getUpdated());
        Assert.assertEquals(from.getUpdatedBy(), to.getUpdatedBy());
        Assert.assertEquals(from.getName(), to.getName());
        Assert.assertArrayEquals(from.getTags(), to.getTags());

    }

    @Test
    public void testCopyInclude() {
        Entity from = createSource();

        Entity to = new Entity();
        Entity result = BeanMerger.from(from)
                .include("id", "name")
                .copyTo(to);

        Assert.assertSame(to, result);

        Assert.assertEquals(from.getId(), to.getId());
        Assert.assertNull(to.getCreated());
        Assert.assertNull(to.getCreatedBy());
        Assert.assertNull(to.getUpdated());
        Assert.assertNull(to.getUpdatedBy());
        Assert.assertEquals(from.getName(), to.getName());
        Assert.assertNull(to.getTags());

    }

    @Test
    public void testCopyEclude() {
        Entity from = createSource();

        Entity to = new Entity();
        Entity result = BeanMerger.from(from)
                .exclude("tags")
                .copyTo(to);

        Assert.assertSame(to, result);

        Assert.assertEquals(from.getId(), to.getId());
        Assert.assertNull(to.getCreated());
        Assert.assertNull(to.getCreatedBy());
        Assert.assertNull(to.getUpdated());
        Assert.assertNull(to.getUpdatedBy());
        Assert.assertEquals(from.getName(), to.getName());
        Assert.assertNull(to.getTags());

    }

    @Test
    public void testCopyIncludeAndEclude() {
        Entity from = createSource();

        Entity to = new Entity();
        Entity result = BeanMerger.from(from)
                .include("id", "name")
                .exclude("name", "tags")
                .copyTo(to);

        Assert.assertSame(to, result);

        Assert.assertEquals(from.getId(), to.getId());
        Assert.assertNull(to.getCreated());
        Assert.assertNull(to.getCreatedBy());
        Assert.assertNull(to.getUpdated());
        Assert.assertNull(to.getUpdatedBy());
        Assert.assertNull(to.getName());
        Assert.assertNull(to.getTags());

    }

    private Entity createSource() {
        Entity from = new Entity();
        from.setCreated(LocalDateTime.now());
        from.setCreatedBy(1L);
        from.setUpdated(LocalDateTime.now());
        from.setUpdatedBy(1L);
        from.setTags(new String[]{"tag1", "tag2", "tag3"});

        return from;
    }

    class Entity {
        private LocalDateTime created;
        private Long createdBy;
        private LocalDateTime updated;
        private Long updatedBy;

        private Long id;
        private String name;
        private String[] tags;

        public LocalDateTime getCreated() {
            return created;
        }

        public void setCreated(LocalDateTime created) {
            this.created = created;
        }

        public Long getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Long createdBy) {
            this.createdBy = createdBy;
        }

        public LocalDateTime getUpdated() {
            return updated;
        }

        public void setUpdated(LocalDateTime updated) {
            this.updated = updated;
        }

        public Long getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Long updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "created=" + created +
                    ", createdBy=" + createdBy +
                    ", updated=" + updated +
                    ", updatedBy=" + updatedBy +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", tags=" + Arrays.toString(tags) +
                    '}';
        }
    }
}
