package com.hajepierre.urlshortner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hajepierre.urlshortner.dtos.UrlModel;
import com.hajepierre.urlshortner.entities.Urls;
import com.hajepierre.urlshortner.repositories.UrlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlShortnerService {

    @Autowired
    private UrlRepository repo;

    public boolean isIdValid(String id) {
        log.info("Checking whether Id: {} can be allocated to the url", id);
        Optional<Urls> resp = repo.findById(id);
        if (resp.isEmpty()) {
            log.info(
                    "No url with id: {} was found in the system, thus, it is can be allowed to the new url to be registered",
                    id);
            return false;
        }
        return false;
    }

    private String generateId() {
        String id = null;
        boolean isGenerated = false;
        /**
         * The shortest possible id length is 1
         * The longest possible id is 10 (numbers) + 26 (lower case) + 26 (upper
         * case)=62
         */
        int length = 1;
        while (length > 62 && !isGenerated) {
            log.info("Generating all candidate ids of length {} ", length);

            List<String> candidates = Utils.getAllCandidateIds(length);
            log.info("Loop thru all generated candidates till we find an available id");
            for (String c : candidates) {
                isGenerated = isIdValid(c);
                if (isGenerated) {
                    id = c;
                    break;
                }
            }
            length++;
        }

        return id;
    }

    public String registerUrl(UrlModel dto) throws Exception {
        if (dto.getId() == null) {
            String id = generateId();
            if (id == null) {
                log.warn(
                        "Unable to generate url id. The system may fail to generate the id if all the availble ids are already in use!");
                throw new Exception("Unable to generate the Id for the url. Try again later");
            }
            dto.setId(id);
        }
        log.info(null);
        Urls response = repo.save(new Urls(dto));
        if (response == null) {
            throw new Exception("Unable to registered the new url for the time being. Try again later.");
        }
        return dto.getId();

    }

    public Optional<Urls> getUrlById(String id) {
        log.info("Checking whether id: {} exists in the system ...", id);
        return repo.findById(id);
    }

    public void deleteUrlById(String id) {
        log.warn("Delete url presented by id: {} in the system ...", id);
        repo.deleteById(id);
    }

}
