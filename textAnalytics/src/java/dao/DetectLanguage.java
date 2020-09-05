package dao;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.idiomaModel;
import org.json.JSONObject;

class Document {

    public String id, text;

    public Document(String id, String text) {
        this.id = id;
        this.text = text;
    }
}

class Documents {

    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document>();
    }

    public void add(String id, String text) {
        this.documents.add(new Document(id, text));
    }
}

public class DetectLanguage {

    static String accessKey = "a101baf102404909a6c097504efe0a48";
    static String host = "https://textanalitic123.cognitiveservices.azure.com/";

    static String path = "/text/analytics/v2.0/languages";

    public static String GetLanguage(Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host + path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public void consultar(idiomaModel idi) {
        try {
            Documents documents = new Documents();
            documents.add(idi.getCodigo(), idi.getTexto());
            String response = GetLanguage(documents);

            JSONObject json = new JSONObject(response);
            
            idi.setRespuesta(json.getJSONArray("documents").getJSONObject(0).get("id").toString());
            idi.setName(json.getJSONArray("documents").getJSONObject(0).getJSONArray("detectedLanguages").getJSONObject(0).get("name").toString());
            idi.setScore(json.getJSONArray("documents").getJSONObject(0).getJSONArray("detectedLanguages").getJSONObject(0).get("score").toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        try {
            Documents documents = new Documents();
            documents.add("1", "This is a document written in English.");
            documents.add("2", "Este es un document escrito en Español.");
            documents.add("3", "这是一个用中文写的文件");

            String response = GetLanguage(documents);
            System.out.println(prettify(response));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
