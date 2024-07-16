package com.hajepierre.urlshortner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hajepierre.urlshortner.dtos.Response;
import com.hajepierre.urlshortner.dtos.UrlModel;
import com.hajepierre.urlshortner.entities.Urls;
import com.hajepierre.urlshortner.services.UrlShortnerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@Slf4j
public class ApplicationController {

    @Autowired
    private UrlShortnerService service;

    @Operation(summary = "Register Url to be shortened")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url registered successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "400", description = "Malformed request object", content = @Content),
            @ApiResponse(responseCode = "503", description = "Something happened while handling the request", content = @Content),
            @ApiResponse(responseCode = "409", description = "The specified Id already exists in the system", content = @Content) })
    @PostMapping("/")
    public Response registerUrl(@RequestBody() @Valid() UrlModel data) {
        log.info("Incoming request to register a new url to the shortner. Object received ==> {}", data.toString());
        if (data.getId() != null) {
            boolean isValid = service.isIdValid(data.getId());
            if (!isValid) {
                log.warn(
                        "An existing record with the id: {} was found in the system. Thus a new record cannot be created as it will conflict with the exist one",
                        data.getId());
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "The specified id: " + data.getId()
                                + " is already is use. Specify a different id and try again");
            }
        }
        try {
            String id = service.registerUrl(data);
            return new Response(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, e.getMessage());
        }
    }

    @Operation(summary = "Delete url by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "404", description = "No Url linked with the specified id was found", content = @Content) })
    @DeleteMapping("/{id}")
    public Response unregisterUrl(@PathVariable String id) throws ResponseStatusException {
        log.info("Incoming request to delete/unregister the url presented by the id: {} ", id);
        service.getUrlById(id);

        this.service.deleteUrlById(id);
        return new Response(id);
    }

    @Operation(summary = "Resolve shortened url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Request executed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "404", description = "No Url linked with the specified id was found", content = @Content) })
    @GetMapping("/{id}")
    public void redirectToOriginalUrl(@PathVariable String id, HttpServletResponse resp)
            throws ResponseStatusException {
        log.info("Incoming request to resolve the url presented by the id: {} ", id);
        Urls response = service.getUrlById(id);
        try {
            resp.sendRedirect(response.getUrl());
        } catch (Exception e) {
            log.warn("The following exception was thrown while while handling url resolution: {}", e.getMessage());
        }
    }

}
