package com.hajepierre.urlshortner.dtos;

import java.util.HashMap;
import java.util.Map;

import com.hajepierre.urlshortner.services.Utils;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlModel {
    @Nullable()
    @Schema(description = "User Specified Url id", name = "id", type = "string", example = "1234", requiredMode = RequiredMode.NOT_REQUIRED)
    private String id;

    @Min(1)
    @Nullable()
    @Schema(description = "User specified expiry time in seconds!", name = "ttl", type = "integer", example = "60", requiredMode = RequiredMode.NOT_REQUIRED)
    private Long ttl;

    @NotBlank
    @Schema(description = "Url", name = "url", type = "string", example = "http://example.com", requiredMode = RequiredMode.REQUIRED)
    @Pattern(regexp = "(https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)?[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,})(\\.[a-zA-Z0-9]{2,})?", message = "Kindly enter a valid url")
    private String url;

    @Override()
    public String toString() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("url", url);

        if(id!=null){
            obj.put("url", url);
        }

        if(ttl!=null){
            obj.put("ttl", ttl);
        }
        return Utils.jsonify(obj);
    }
}