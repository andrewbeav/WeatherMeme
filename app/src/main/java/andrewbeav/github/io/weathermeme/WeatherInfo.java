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

    private JSONObject weatherJSON, weatherData, coordData, windData, rainData;

    // All data from the JSon
    private double temperature, highTemp, lowTemp, pressure, humidity, windSpeed, windDeg;
    private String name;
    private int rainVolume=-1;

    public WeatherInfo(JSONObject weatherJSON) {
        this.weatherJSON = weatherJSON;

        try {
            this.weatherData = new JSONObject(weatherJSON.getString("main"));
            this.coordData = new JSONObject(weatherJSON.getString("coord"));

            this.temperature = Double.parseDouble(weatherData.getString("temp"));
            this.highTemp = Double.parseDouble(weatherData.getString("temp_max"));
            this.lowTemp = Double.parseDouble(weatherData.getString("temp_min"));
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
                this.rainData = new JSONObject(weatherJSON.getString("rain"));
                this.rainVolume = Integer.parseInt(rainData.getString("3h"));
            } catch (JSONException e) {
                this.rainVolume = -1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTemperature() {
        return (int)this.temperature;
    }

    public int getLowTemperature() {
        return (int)this.lowTemp;
    }

    public int getHighTemperature() {
        return (int)this.highTemp;
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

    public int getRainVolume() {
        return this.rainVolume;
    }

    public String getCityName() {
        return this.name;
    }
}