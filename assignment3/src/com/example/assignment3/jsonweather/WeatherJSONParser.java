package com.example.assignment3.jsonweather;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

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
        List <JsonWeather> weathers = new ArrayList<>();
        try {
            //while (reader.hasNext()){
//                String name = reader.nextName();
//                switch (name) {
//                    case JsonWeather.coord_JSON:
//                        reader.nextString();
//                        break;
//                }
//                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
            if(reader.peek() == JsonToken.END_DOCUMENT) {
                return null;
            }
            weathers.add(parseJsonWeather(reader));
//                }
           // }
            return weathers;
        } finally {
            Log.d(TAG, "finish parsing from JSON");
        }
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
        reader.beginObject();
        JsonWeather weather = new JsonWeather();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case JsonWeather.sys_JSON:
                        weather.setSys(parseSys(reader));
                        break;
                    case JsonWeather.weather_JSON:
                        if (reader.peek() == JsonToken.BEGIN_ARRAY){
                            weather.setWeather(parseWeathers(reader));
                        }
                        break;
                    case JsonWeather.base_JSON:
                        weather.setBase(reader.nextString());
                        break;
                    case JsonWeather.main_JSON:
                        weather.setMain(parseMain(reader));
                        break;
                    case JsonWeather.wind_JSON:
                        weather.setWind(parseWind(reader));
                        break;
                    case JsonWeather.dt_JSON:
                        weather.setDt(reader.nextInt());
                        break;
                    case JsonWeather.id_JSON:
                        weather.setId(reader.nextLong());
                        break;
                    case JsonWeather.name_JSON:
                        weather.setName(reader.nextString());
                        break;
                    case JsonWeather.cod_JSON:
                        weather.setCod(reader.nextLong());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }
        return weather;
    }
    
    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginArray();
        try {
            List<Weather> weathers = new ArrayList<>();
            while(reader.hasNext()) {
                weathers.add(parseWeather(reader));
            }
            return weathers;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        Weather weather = new Weather();
        try {
            while(reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Weather.id_JSON:
                        weather.setId(reader.nextLong());
                        break;
                    case Weather.main_JSON:
                        weather.setMain(reader.nextString());
                        break;
                    case Weather.description_JSON:
                        weather.setDescription(reader.nextString());
                        break;
                    case Weather.icon_JSON:
                        weather.setIcon(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }

        return weather;
    }
    
    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        Main main = new Main();
        try {
            while(reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Main.grndLevel_JSON:
                        main.setGrndLevel(reader.nextDouble());
                        break;
                    case Main.humidity_JSON:
                        main.setHumidity(reader.nextLong());
                        break;
                    case Main.pressure_JSON:
                        main.setPressure(reader.nextDouble());
                        break;
                    case Main.seaLevel_JSON:
                        main.setSeaLevel(reader.nextDouble());
                        break;
                    case Main.temp_JSON:
                        main.setTemp(reader.nextDouble());
                        break;
                    case Main.tempMin_JSON:
                        main.setTempMin(reader.nextDouble());
                        break;
                    case Main.tempMax_JSON:
                        main.setTempMax(reader.nextDouble());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }

            }
        } finally {
            reader.endObject();
        }
        return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
        reader.beginObject();
        Wind wind = new Wind();
        try {
            while(reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Wind.deg_JSON:
                        wind.setDeg(reader.nextDouble());
                        break;
                    case Wind.speed_JSON:
                        wind.setSpeed(reader.nextDouble());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }

        return wind;
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
                        break;
                }
            }
        } finally {
            reader.endObject();
        }

        return sys;
    }
}
