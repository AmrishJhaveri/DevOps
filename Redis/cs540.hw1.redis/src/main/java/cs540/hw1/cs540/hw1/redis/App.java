package cs540.hw1.cs540.hw1.redis;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import redis.clients.jedis.Jedis;

public class App {
	public static void main(String[] args) throws IOException, ParseException {
		Jedis jedis = new Jedis(args[0]);

		// check whether server is running or not
		System.out.println("Connected to Redis Server");

		String prevData = jedis.hget("repo", args[2]);
		JSONParser parser = new JSONParser();

		if (prevData != null) {
			System.out.println("-----------------------------------------------------\nPrevious data on Redis:");
			JSONObject tempObj = (JSONObject) parser.parse(prevData);
			JSONArray arr = ((JSONArray) tempObj.get("ISSUES"));
			for (int i = 0; i < arr.size(); i++) {

				JSONObject obj = (JSONObject) arr.get(i);
				System.out.println("Issue Number: " + (Long) obj.get("number"));
				System.out.println("Url: " + (String) obj.get("url") + "\nComments: " + (String) obj.get("body")
						+ "\n-------------------------------------------------------------------------------------");

			}

		}

		FileReader fileReader = new FileReader(args[1]);

		JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
		List<JSONObject> issueList = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.size(); i++) {

			JSONObject tempObj = (JSONObject) jsonArray.get(i);
			JSONObject insertObj = new JSONObject();
			insertObj.put("url", (String) tempObj.get("url"));
			insertObj.put("body", (String) tempObj.get("body"));
			insertObj.put("number", (Long) tempObj.get("number"));

			issueList.add(insertObj);
		}

		JSONObject object = new JSONObject();
		object.put("ISSUES", issueList);

		jedis.hset("repo", args[2], object.toJSONString());

		System.out.println("New Issues updated to Redis");
		
		jedis.close();

	}
}