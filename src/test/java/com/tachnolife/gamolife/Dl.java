package com.tachnolife.gamolife;

import okhttp3.*;

import java.io.IOException;

/**
 * @author Abolfazl
 */
public class Dl {
    public static void main(String[] args) throws InterruptedException, IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"phoneNumber\": \"09351844321\"\r\n}");
        Request request = new Request.Builder()
            .url("http://localhost:8558/api/gamers/register")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();

        while (true){
            long l = System.currentTimeMillis();

            Response response = client.newCall(request).execute();
            System.out.println(response.code()+ "   "+(System.currentTimeMillis()-l));

            Thread.sleep(300);

        }
    }
}
