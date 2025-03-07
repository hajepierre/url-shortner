package com.hajepierre.urlshortner.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import com.hajepierre.urlshortner.dtos.UrlModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Urls implements Serializable {
    @Id
    private String id;

    @TimeToLive
    private Long ttl;

    private String url;

    public Urls(UrlModel dto) {
        this.id = dto.getId();
        this.ttl = dto.getTtl() == null ? -1L : dto.getTtl();
        this.url = dto.getUrl();
    }

}
