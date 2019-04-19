package teamwork.sys.lookup.web;

import teamwork.sys.lookup.Lookup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

class RequestUpdate extends Lookup {

    @NotNull
    @Positive
    @Override
    public Long getId() {
        return super.getId();
    }

    @Size(min = 1, max = 32)
    @Override
    public String getType() {
        return super.getType();
    }

    @Size(min = 1, max = 32)
    @Override
    public String getValue() {
        return super.getValue();
    }

    @Size(min = 1, max = 32)
    @Override
    public String getText() {
        return super.getText();
    }
}
