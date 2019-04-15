package teamwork.sys.services;

import teamwork.sys.models.Lookup;

import java.util.List;

public interface LookupService {
    List<Lookup> lookup(String type);
    String lookup(String type, String value);
    Lookup save(Lookup lookup);

}
