package service.calorie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:40
 * Purpose: TODO:
 **/
@Service
public class NutritionixService {

    @Value("${url}")
    private String url;
    @Value("${app_id}")
    private String appId;
    @Value("${app_key}")
    private String appKey;
    private final RestTemplate restTemplate;

    @Autowired
    public NutritionixService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int fetchCaloriesFromText(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-app-id", appId);
        headers.add("x-app-key", appKey);

        ObjectNode payload = JsonNodeFactory.instance.objectNode();
        payload.put("query", text);

        ObjectNode root = restTemplate.postForObject(url, payload, ObjectNode.class, headers);

        ArrayNode foods = (ArrayNode) root.get("foods");
        int totalCalories = 0;
        for (JsonNode food : foods) {
            totalCalories += food.get("nf_calories").asInt();
        }
        return totalCalories;
    }
}
