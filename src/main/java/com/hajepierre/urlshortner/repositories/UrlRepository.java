package com.hajepierre.urlshortner.repositories;

import org.springframework.data.repository.CrudRepository;

import com.hajepierre.urlshortner.entities.Urls;

public interface UrlRepository extends CrudRepository<Urls, String> {
    
}
