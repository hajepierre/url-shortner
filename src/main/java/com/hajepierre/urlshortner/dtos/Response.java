package com.hajepierre.urlshortner.dtos;

import java.util.HashMap;
import java.util.Map;

import com.hajepierre.urlshortner.services.Utils;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Response {
    @Schema(description = "Response status", name = "status", type = "string", example = "SUCCESS")
    private String status;

    @Schema(description = "Response description", name = "description", type = "string", example = "Request handled successfully")
    private String description;

    @Schema(description = "Url Id", name = "id", type = "string", example = "1234")
    @Nullable()
    private String id;

    @Override()
    public String toString() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("status", status);

        obj.put("description", description);

        if (id != null) {
            obj.put("id", id);
        }
        return Utils.jsonify(obj);
    }
}
