package com.hajepierre.urlshortner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final String SINGLES = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * This method allows the printing of a java object as a stringified json object
     * @param map
     * @return
     */
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

    /**
     * The method which generated id candidates of a given  length
     * @param k - The desired id length
     * @return
     */
    public static List<String> getAllCandidateIds(int k) {
        String set[] = SINGLES.split("");

        List<String> response = new ArrayList<>();
        int n = set.length;
        generateKeyHelper(set, "", n, k, response);
        return response;
    }

    /**
     * This method allows the generation of ids like aaa. 
     * This duplication enlengthens the pull of available ids
     * @param set - Array of numbers of characters(lower and upper cases)
     * @param prefix
     * @param n - The default set lenght
     * @param k - The length of the key/id to be generated
     * @param resp - List of generated Ids
     */
    private static void generateKeyHelper(String[] set,
            String prefix,
            int n, int k, List<String> resp) {

        if (k == 0 || k > n) {
            resp.add(prefix);
            return;
        }

        for (int i = 0; i < n; ++i) {
            String newPrefix = prefix + set[i];
            generateKeyHelper(set, newPrefix,
                    n, k - 1, resp);
        }
    }
}
