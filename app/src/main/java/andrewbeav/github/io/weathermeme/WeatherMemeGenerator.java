package andrewbeav.github.io.weathermeme;

/**
 * Created by andrewbeav on 3/4/17.
 */

public class WeatherMemeGenerator {
    public static final int HOT_TRESHOLD = 80;
    public static final int COLD_TRESHOLD = 40;
    public static final int CHILLY_TRESHOLD = 60;
    public static final int WIND_TRESHOLD = 20;
    public static final int RAIN_CODE = 500;
    public static final int SNOW_CODE = 600;
    public static final int THUNDERSTORM_CODE = 200;

    private WeatherInfo weatherInfo;

    public WeatherMemeGenerator(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getImageTitle() {
        int rand = (int)(Math.random()*5) + 1;
        String result = "";

        if (weatherInfo.getWeatherCode() >= THUNDERSTORM_CODE && weatherInfo.getWeatherCode() < RAIN_CODE + 100) {
            result = "rain" + rand;
        }
        else if (weatherInfo.getWeatherCode() >= SNOW_CODE && < SNOW_CODE + 100) {
            result = "snow" + rand;
        }
        else if (weatherInfo.getWindSpeed() > WIND_TRESHOLD) {
                result = "wind" + rand;
        }
        else if (weatherInfo.getTemperature() > HOT_TRESHOLD) {
            result = "hot_weather" + rand;
        }
        else if (weatherInfo.getTemperature() < COLD_TRESHOLD) {
            result = "cold_weather" + rand;
        }
        else if (weatherInfo.getTemperature() < CHILLY_TRESHOLD) {
            result = "chilly" + rand;
        }
        else {
            result = "neutral" + rand;
        }

        return result;
    }
}
