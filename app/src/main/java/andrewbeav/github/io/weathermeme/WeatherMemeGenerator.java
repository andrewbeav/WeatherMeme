package andrewbeav.github.io.weathermeme;

/**
 * Created by andrewbeav on 3/4/17.
 */

public class WeatherMemeGenerator {
    public static final int HOT_TRESHOLD = 80;
    public static final int COLD_TRESHOLD = 65;

    private WeatherInfo weatherInfo;

    public WeatherMemeGenerator(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getImageTitle() {
        int rand = (int)(Math.random()*5) + 1;
        String result = "";

        if (weatherInfo.getRainVolume() != -1) {
            result = "rain" + rand;
        }
        else {
            if (weatherInfo.getWindSpeed() > 25) {
                result = "wind" + rand;
            }
            else {
                if (weatherInfo.getTemperature() > HOT_TRESHOLD) {
                    result = "hot_weather" + rand;
                } else if (weatherInfo.getTemperature() < COLD_TRESHOLD) {
                    result = "cold_weather" + rand;
                } else {
                    result = "neutral" + rand;
                }
            }
        }

        return result;
    }
}
