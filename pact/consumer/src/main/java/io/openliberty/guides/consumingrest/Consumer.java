// tag::comment[]
/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 // end::comment[]
package io.openliberty.guides.consumingrest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import javax.json.bind.JsonbBuilder;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONObject;

@Path("/artist")
public class Consumer {

    private final String url;

    public Consumer(String url) {
        this.url = url;
    }

    public String getTotal() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(url + "/artists/total").request().get();
        String result = response.readEntity(String.class);
        response.close();
        client.close();
        System.out.println("Total= " + result);
        return result;
    }

    public String getArtists() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(url + "/artists").request().get();
        String result = response.readEntity(String.class);
        response.close();
        client.close();
        System.out.println("Artists: " + result);
        return result;
    }
}
