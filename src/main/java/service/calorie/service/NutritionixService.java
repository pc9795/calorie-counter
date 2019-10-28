package service.calorie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:40
 * Purpose: Service class to interact with Nutritionix API.
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
        try {
            // Setting headers.
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-app-id", appId);
            headers.add("x-app-key", appKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Setting payload.
            ObjectNode payload = JsonNodeFactory.instance.objectNode();
            payload.put("query", text);

            // Getting the response.
            String response = restTemplate.postForObject(url, new HttpEntity<>(payload.toString(), headers), String.class);
            // Converting to JSON.
            ObjectNode root = new ObjectMapper().readValue(response, ObjectNode.class);

            ArrayNode foods = (ArrayNode) root.get("foods");
            int totalCalories = 0;
            for (JsonNode food : foods) {
                totalCalories += food.get("nf_calories").asInt();
            }
            return totalCalories;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
