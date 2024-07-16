package com.hajepierre.urlshortner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hajepierre.urlshortner.dtos.UrlModel;
import com.hajepierre.urlshortner.entities.Urls;
import com.hajepierre.urlshortner.repositories.UrlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlShortnerService {

    @Autowired
    private UrlRepository repo;

    /**
     * The method that checks whether a given id can be allowed to a new record
     * It return False if the id exists in the db, and True if it does not
     * 
     * @param id
     * @return
     */
    public boolean isIdValid(String id) {
        log.info("Checking whether Id: {} can be allocated to the url", id);
        Optional<Urls> resp = repo.findById(id);
        if (resp.isEmpty()) {
            log.info(
                    "No url with id: {} was found in the system, thus, it is can be allowed to the new url to be registered",
                    id);
            return true;
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

    /**
     * The method that registers a new url to the db
     * 
     * @param dto
     * @return
     * @throws Exception
     */
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

    /**
     * The method that retries url from the db given the id
     * 
     * @param id
     * @return
     */
    public Urls getUrlById(String id) throws ResponseStatusException {
        log.info("Checking whether id: {} exists in the system ...", id);
        Optional<Urls> response = repo.findById(id);
        if (response.isPresent()) {
            return response.get();
        }
        log.warn("No record with id {} was found in the system. Thus the record cannot be fulfilled.", id);
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No url with the specified id was found in the system");
    }

    /**
     * The method that delete url record from the db given the id
     * 
     * @param id
     */
    public void deleteUrlById(String id) {
        log.warn("Delete url presented by id: {} in the system ...", id);
        repo.deleteById(id);
    }

}
