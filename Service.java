package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Service {
    public static String API_PATH = "https://api.spotify.com/v1/browse/";
    boolean isAuthorised = false;
    public static String ACCESS_TOKEN = "";



    public void setAuthorization() {
        Authorisation authorisation = new Authorisation();
        authorisation.getAccessCode();
        ACCESS_TOKEN = authorisation.getAccessToken();
        this.isAuthorised = true;
    }

    public List<String> getReleases() {
        if (isAuthorised) {
            String releasesJson = request("new-releases");
            JsonObject jo = JsonParser.parseString(releasesJson).getAsJsonObject();
            JsonObject albums = jo.get("albums").getAsJsonObject();
            JsonArray items = albums.get("items").getAsJsonArray();

            List<String> releasesList = new ArrayList<>();

            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString();


                JsonArray artist = item.getAsJsonObject().get("artists").getAsJsonArray();
                List<String> artistList = new ArrayList<>();

                for (JsonElement art : artist) {
                    JsonObject artObj = art.getAsJsonObject();
                    artistList.add(artObj.get("name").getAsString());
                }



                String url = item.getAsJsonObject().get("external_urls").getAsJsonObject()
                        .get("spotify").getAsString();

                String toAdd = name + "\n" + artistList + "\n" + url + "\n";
                releasesList.add(toAdd);
            }

            return releasesList;
        } else {
            System.out.println("Please, provide access for application.");
            return null;
        }
    }


    public List<String> getFeatured() {
        if (isAuthorised) {
            String featuredJson = request("featured-playlists");
            JsonObject featured = JsonParser.parseString(featuredJson).getAsJsonObject()
                    .get("playlists").getAsJsonObject();
            JsonArray items = featured.get("items").getAsJsonArray();

            List<String> featuredList = new ArrayList<>();
            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString() + "\n";
                String url = item.getAsJsonObject().get("external_urls").getAsJsonObject()
                        .get("spotify").getAsString() + "\n";
                featuredList.add(name + url);
            }

            return featuredList;
        } else {
            System.out.println("Please, provide access for application.");
            return null;
        }
    }



    public String getCategoryId(String searchedName) {
        if (isAuthorised) {
            String categoriesJson = request("categories");
            JsonObject jo = JsonParser.parseString(categoriesJson).getAsJsonObject();
            JsonObject categories = jo.get("categories").getAsJsonObject();
            JsonArray items = categories.get("items").getAsJsonArray();

            Map<String, String> categoriesMap = new HashMap<>();
            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString();
                String id = item.getAsJsonObject().get("id").getAsString();
                categoriesMap.put(name, id);
            }

            return categoriesMap.get(searchedName);

        } else {
            return "Please, provide access for application.";
        }
    }

    public List<String> printCategories() {
        if (isAuthorised) {
            String categoriesJson = request("categories");
            JsonObject jo = JsonParser.parseString(categoriesJson).getAsJsonObject();
            JsonObject categories = jo.get("categories").getAsJsonObject();
            JsonArray items = categories.get("items").getAsJsonArray();


            List<String> categoriesList = new ArrayList<>();

            for (JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString();
                categoriesList.add(name);
            }

            return categoriesList;
        } else {
            System.out.println("Please, provide access for application.");
            return null;
        }
    }


    public List<String> getPlaylists(String name) {
        if (isAuthorised) {
            String categoryId = getCategoryId(name);
            String playlistsJson = "";
            List<String> featuredList = new ArrayList<>();

            if (categoryId != null) {
                playlistsJson = request("categories/" + categoryId + "/playlists");
                System.out.println(playlistsJson);
            }

            if (playlistsJson.contains("error") || categoryId == null) {
                System.out.println("Unknown category name.");
            } else {
                JsonObject playlists = JsonParser.parseString(playlistsJson).getAsJsonObject()
                        .get("playlists").getAsJsonObject();
                JsonArray items = playlists.get("items").getAsJsonArray();


                for (JsonElement item : items) {
                    String nameL = item.getAsJsonObject().get("name").getAsString() + "\n";
                    String url = item.getAsJsonObject().get("external_urls").getAsJsonObject()
                            .get("spotify").getAsString() + "\n";
                    featuredList.add(nameL + url);
                }
            }
            return featuredList;
        } else {
            System.out.println("Please, provide access for application.");
            return null;
        }
    }



    public String request(String uri) {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(API_PATH + uri))
                .GET()
                .build();
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            assert response != null;
            return response.body();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return "error";
    }
}