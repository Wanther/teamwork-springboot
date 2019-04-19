package teamwork.common;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class Requests {
    public static class BatchDelete {
        @NotEmpty
        private List<Long> id;

        public List<Long> getId() {
            return id;
        }

        public void setId(List<Long> id) {
            this.id = id;
        }
    }
}
