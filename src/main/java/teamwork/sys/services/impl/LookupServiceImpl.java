package teamwork.sys.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamwork.sys.models.Lookup;
import teamwork.sys.repos.LookupRepository;
import teamwork.sys.services.LookupService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class LookupServiceImpl implements LookupService {

    @Autowired
    private LookupRepository lookupRepo;

    @Override
    public List<Lookup> lookup(@NotNull String type) {
        return lookupRepo.findByType(type);
    }

    @Override
    public String lookup(@NotNull String type, @NotNull String value) {
        return lookupRepo.findByTypeAndValue(type, value)
                .map(Lookup::getText)
                .orElse("");
    }

    @Override
    public Lookup save(Lookup lookup) {
        return lookupRepo.save(lookup);
    }
}
