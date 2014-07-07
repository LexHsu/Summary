package weather.observer.impl;

import weather.observer.DisplayElement;
import weather.observer.Observer;
import weather.subject.impl.WeatherData;

public class ForecastDisplay implements Observer, DisplayElement {
    private float mCurrentPressure = 29.92f;  
    private float mLastPressure;
    private WeatherData mWeatherData;

    public ForecastDisplay(WeatherData weatherData) {
        this.mWeatherData = weatherData;
        weatherData.registerObserver(this);
    }

    public void update(float temp, float humidity, float pressure) {
                mLastPressure = mCurrentPressure;
        mCurrentPressure = pressure;

        display();
    }

    public void display() {
        System.out.print("Forecast: ");
        if (mCurrentPressure > mLastPressure) {
            System.out.println("Improving weather on the way!");
        } else if (mCurrentPressure == mLastPressure) {
            System.out.println("More of the same");
        } else if (mCurrentPressure < mLastPressure) {
            System.out.println("Watch out for cooler, rainy weather");
        }
    }
}
