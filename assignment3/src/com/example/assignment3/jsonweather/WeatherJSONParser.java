package com.example.assignment3.jsonweather;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
        this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
        throws IOException {
        // TODO -- you fill in here.
        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))){

            return parseJsonWeatherObjectForm(reader);
        }
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArrayForm(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
        reader.beginArray();
        try {
            if (reader.peek() == JsonToken.END_ARRAY) {
                return null;
            } else {
                return parseJsonWeatherObjectForm(reader);
            }
        } finally {
            reader.endArray();
        }
    }

    private List<JsonWeather> parseJsonWeatherObjectForm(JsonReader reader) throws IOException {
        List <JsonWeather> weathers = null;
        reader.beginObject();

        try {
            outerloop:
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name) {
                    case JsonWeather.sys_JSON:
                        JsonWeather w = new JsonWeather();
                        w.setSys(parseSys(reader));
                        weathers.set(0, w);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        }finally {
            reader.endObject();
        }

        return weathers;
    }

    private List<JsonWeather> parseWeatherArrayForm(JsonReader reader)
            throws IOException {
        reader.beginArray();

        try {
            List<JsonWeather> weathers = new ArrayList<JsonWeather>();

            while (reader.hasNext())
                weathers.add(parseJsonWeather(reader));

            return weathers;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader) 
        throws IOException {

        // TODO -- you fill in here.
        return null;
    }
    
    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        return null;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        return null;
    }
    
    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
        return null;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        return null;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        Sys sys = new Sys();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Sys.country_JSON:
                        sys.setCountry(reader.nextString());
                        break;
                    case Sys.message_JSON:
                        sys.setMessage(reader.nextDouble());
                        break;
                    case Sys.sunrise_JSON:
                        sys.setSunrise(reader.nextLong());
                        break;
                    case Sys.sunset_JSON:
                        sys.setSunset(reader.nextLong());
                        break;
                    default:
                        reader.skipValue();
                }
            }
        } finally {
            reader.endObject();
        }

        return sys;
    }
}
