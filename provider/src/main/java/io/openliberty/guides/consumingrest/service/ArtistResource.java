package io.openliberty.guides.consumingrest.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import io.openliberty.guides.consumingrest.model.Artist;
import io.openliberty.guides.consumingrest.model.Album;
//import io.openliberty.guides.consumingrest.Consumer;

@Path("artists")
public class ArtistResource {

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getArtists() {
        return Reader.getArtists();
    }

    @GET
    @Path("jsonString")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJsonString() {
        Jsonb jsonb = JsonbBuilder.create();

        Artist[] artists = consumeWithJsonb(uriInfo.getBaseUri().toString() +
                "artists");
        String result = jsonb.toJson(artists);

        return result;
    }

    @GET
    @Path("total/{artist}")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalAlbums(@PathParam("artist") String artist) {
        Artist[] artists = consumeWithJsonb(uriInfo.getBaseUri().toString()
                + "artists");

        for (int i = 0; i < artists.length; i++) {
            if (artists[i].name.equals(artist)) {
                return artists[i].albums.length;
            }
        }
        return -1;
    }

    @GET
    @Path("total")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalArtists() {
        return consumeWithJsonp(uriInfo.getBaseUri().toString() +
                "artists").length;
    }
    public static Artist[] consumeWithJsonb(String targetUrl) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(targetUrl).request().get();
        Artist[] artists = response.readEntity(Artist[].class);

        response.close();
        client.close();

        return artists;
    }

    public static Artist[] consumeWithJsonp(String targetUrl) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(targetUrl).request().get();
        JsonArray arr = response.readEntity(JsonArray.class);

        response.close();
        client.close();

        return collectArtists(arr);
    }

    private static Artist[] collectArtists(JsonArray artistArr) {
        List<Artist> artists = artistArr.stream().map(artistJson -> {
            JsonArray albumArr = ((JsonObject) artistJson).getJsonArray("albums");
            Artist artist = new Artist(
                    ((JsonObject) artistJson).getString("name"),
                    collectAlbums(albumArr));
            return artist;
        }).collect(Collectors.toList());

        return artists.toArray(new Artist[artists.size()]);
    }

    private static Album[] collectAlbums(JsonArray albumArr) {
        List<Album> albums = albumArr.stream().map(albumJson -> {
            Album album = new Album(
                    ((JsonObject) albumJson).getString("title"),
                    ((JsonObject) albumJson).getString("artist"),
                    ((JsonObject) albumJson).getInt("ntracks") );
            return album;
        }).collect(Collectors.toList());

        return albums.toArray(new Album[albums.size()]);
    }
}