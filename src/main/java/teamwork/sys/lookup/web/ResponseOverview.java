package teamwork.sys.lookup.web;

import teamwork.sys.lookup.Lookup;

class ResponseOverview extends Lookup {
    private String inactiveTxt;

    public ResponseOverview() {}

    public String getInactiveTxt() {
        return inactiveTxt;
    }

    public void setInactiveTxt(String inactiveTxt) {
        this.inactiveTxt = inactiveTxt;
    }
}
