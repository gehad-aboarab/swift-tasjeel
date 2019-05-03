package com.cmp404.cloud_brokerapplication.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Util {
    private enum HttpMethod {GET, POST, PUT, DELETE}

    private static String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

    private static String formatEntry(Map.Entry<String, Object> entry) throws UnsupportedEncodingException {
        return entry.getKey() + "=" + entry.getValue().toString();
    }

    private static String formatParams(Map<String, Object> map) throws UnsupportedEncodingException {
        if (map.isEmpty())
            return "";

        StringBuffer buffer = new StringBuffer();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        Map.Entry<String, Object> entry = iterator.next();
        buffer.append(formatEntry(entry));
        while (iterator.hasNext()) {
            entry = iterator.next();
            buffer.append("&").append(formatEntry(entry));
        }

        return buffer.toString();
    }

    public static Map<String, Object> makeMap(String[] keys, Object[] values) {
        if (keys.length != values.length) {
            String error = "Mismatching sizes: keys.length = " + keys.length
                    + ", values.length = " + values.length + "\n"
                    + "keys = " + Arrays.toString(keys) + "\n"
                    + "values = " + Arrays.deepToString(values);
            throw new IllegalArgumentException(error);
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keys.length; ++i) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    private static void writeParams(HttpURLConnection connection, Map<String, Object> parameters) throws IOException {
        String formattedParamString = formatParams(parameters);
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(formattedParamString);
            writer.close();
            connection.connect();
        }
    }

    private static Response readBody(HttpURLConnection connection) throws IOException {
        StringBuilder builder = new StringBuilder();

        InputStream stream;
        if (connection.getResponseCode() < 400)
            stream = connection.getInputStream();
        else
            stream = connection.getErrorStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }
        }

        return new Response(builder.toString(), connection.getResponseCode());
    }

    private static Response visitHttp(HttpMethod method, String urlString, Map<String, Object> parameters) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);

        if (method != HttpMethod.GET) {
            connection.setDoOutput(true);
            connection.setRequestMethod(method.name());
            writeParams(connection, parameters);
        }

        return readBody(connection);
    }

    public static Response get(String urlString) throws IOException {
        return visitHttp(HttpMethod.GET, urlString, new HashMap<String, Object>());
//
//        URL url = new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        StringBuilder stringBuilder = new StringBuilder();
//
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            char[] buffer = new char[1024];
//            int read;
//            while ((read = reader.read(buffer)) != -1) {
//                stringBuilder.append(buffer, 0, read);
//            }
//        } finally {
//            connection.disconnect();
//        }
//
//        return new Response(stringBuilder.toString(), connection.getResponseCode());
    }

    public static Response post(String urlString, Map<String, Object> parameters) throws IOException {
        return visitHttp(HttpMethod.POST, urlString, parameters);
    }

    public static Response delete(String urlString, Map<String, Object> parameters) throws IOException {
        return visitHttp(HttpMethod.DELETE, urlString, parameters);
    }

    public static Response put(String urlString, Map<String, Object> parameters) throws IOException {
        return visitHttp(HttpMethod.PUT, urlString, parameters);
    }

}