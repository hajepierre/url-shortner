package com.hajepierre.urlshortner.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hajepierre.urlshortner.dtos.Response;
import com.hajepierre.urlshortner.dtos.UrlModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class ApplicationController {

    @Operation(summary = "Register Url to be shortened")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Url registered successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "400", description = "Malformed request object", content = @Content),
            @ApiResponse(responseCode = "409", description = "The specified Id already exists in the system", content = @Content) })
    @PostMapping("/")
    public Response registerUrl(@RequestBody() @Valid UrlModel data) {
        log.info("Incoming request to register a new url to the shortner. Object received ==> {}", data.toString());
        return null;
    }

    @Operation(summary = "Delete url by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "404", description = "No Url linked with the specified id was found", content = @Content) })
    @DeleteMapping("/{id}")
    public Response unregisterUrl(@PathVariable String id) {
        log.info("Incoming request to delete/unregister the url presented by the id: {} ", id);
        return null;
    }

    @Operation(summary = "Resolve shortened url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Request executed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
            @ApiResponse(responseCode = "404", description = "No Url linked with the specified id was found", content = @Content) })
    @GetMapping("/{id}")
    public void redirectToOriginalUrl(@PathVariable String id, HttpServletResponse resp) {
        log.info("Incoming request to resolve the url presented by the id: {} ", id);

    }
}
