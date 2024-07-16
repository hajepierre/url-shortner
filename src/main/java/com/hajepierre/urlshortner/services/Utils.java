package com.hajepierre.urlshortner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final String SINGLES = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String jsonify(Map<String, Object> map) {
        if (map == null) {
            return "{}";
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static List<String> getAllCandidateIds(int k) {
        String set[] = SINGLES.split("");

        List<String> response = new ArrayList<>();
        int n = set.length;
        computeKey(set, "", n, k, response);
        return response;
    }

    private static void computeKey(String[] set,
            String prefix,
            int n, int k, List<String> resp) {

        if (k == 0 || k > n) {
            resp.add(prefix);
            return;
        }
        
        for (int i = 0; i < n; ++i) {
            String newPrefix = prefix + set[i];
            computeKey(set, newPrefix,
                    n, k - 1, resp);
        }
    }
}
