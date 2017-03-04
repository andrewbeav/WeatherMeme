package andrewbeav.github.io.weathermeme;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrewbeav on 3/3/17.
 */

public class WeatherInfo {

    public static final String API_KEY = "bcf98db996d2d93497a184c6af4c3c7a";
    public static final String weatherUrlString = "api.openweathermap.org/data/2.5/weather";

    private JSONObject weatherJSON, weatherData, windData, rainData, mainWeatherData;

    // All data from the JSon
    private double temperature, pressure, humidity, windSpeed, windDeg;
    private String name, main;

    public WeatherInfo(JSONObject weatherJSON) {
        this.weatherJSON = weatherJSON;

        try {
            this.weatherData = new JSONObject(weatherJSON.getString("main"));
            this.temperature = Double.parseDouble(weatherData.getString("temp"));
            this.pressure = Double.parseDouble(weatherData.getString("pressure"));
            this.humidity = Double.parseDouble(weatherData.getString("humidity"));
            this.name = weatherJSON.getString("name");

            try {
                this.windData = new JSONObject(weatherJSON.getString("wind"));
                this.windSpeed = Double.parseDouble(windData.getString("speed"));
                this.windDeg = Double.parseDouble(windData.getString("deg"));
            } catch (JSONException e) {
                this.windSpeed = -1;
                this.windDeg = -1;
            }
            try {
                this.mainWeatherData = new JSONObject(weatherJSON.getString("weather"));
                this.main = mainWeatherData.getString("main");
            } catch (JSONException e) {
                this.main = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTemperature() {
        return (int)this.temperature;
    }

    public double getPressure() {
        return this.pressure;
    }

    public double getHumidity() {
        return this.humidity;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public String getWindDirection() {
        if (windDeg > 0 && windDeg <= 45 || windDeg > 315 && windDeg <= 360) {
            return "N";
        }
        else if (windDeg > 45 && windDeg <= 135) {
            return "E";
        }
        else if (windDeg > 135 && windDeg <= 225) {
            return "S";
        }
        else if (windDeg > 225 && windDeg <= 315) {
            return "W";
        }
        return null;
    }

    public String getMain() {
        return this.main;
    }

    public String getCityName() {
        return this.name;
    }
}