package weather.observer.impl;

import weather.observer.DisplayElement;
import weather.observer.Observer;
import weather.subject.impl.WeatherData;

public class StatisticsDisplay implements Observer, DisplayElement {
    private float mMaxTemp = 0.0f;
    private float mMinTemp = 200;
    private float mTempSum= 0.0f;
    private int mNumReadings;
    private WeatherData mWeatherData;

    public StatisticsDisplay(WeatherData weatherData) {
        this.mWeatherData = weatherData;
        weatherData.registerObserver(this);
    }

    public void update(float temp, float humidity, float pressure) {
        mTempSum += temp;
        mNumReadings++;

        if (temp > mMaxTemp) {
            mMaxTemp = temp;
        }
 
        if (temp < mMinTemp) {
            mMinTemp = temp;
        }

        display();
    }

    public void display() {
        System.out.println("Avg/Max/Min temperature = " + (mTempSum / mNumReadings)
            + "/" + mMaxTemp + "/" + mMinTemp);
    }
}
