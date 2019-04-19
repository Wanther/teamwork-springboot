package teamwork.sys.lookup.web;

import teamwork.sys.lookup.Lookup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

class RequestCreate extends Lookup {

    public RequestCreate() {
        setInactive(false);
    }

    @NotEmpty
    @Size(min = 1, max = 32)
    @Override
    public String getType() {
        return super.getType();
    }

    @NotEmpty
    @Size(min = 1, max = 32)
    @Override
    public String getValue() {
        return super.getValue();
    }

    @NotEmpty
    @Size(min = 1, max = 32)
    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void setId(Long id) {
        throw new UnsupportedOperationException("id will generate by db");
    }
}
