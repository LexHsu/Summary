package weatherobservable.observer;

import java.util.Observable;
import java.util.Observer;

import weatherobservable.observable.WeatherData;

public class StatisticsDisplay implements Observer, DisplayElement {
    private float mMaxTemp = 0.0f;
    private float mMinTemp = 200;
    private float mTempSum= 0.0f;
    private int mNumReadings;

    public StatisticsDisplay(Observable observable) {
        observable.addObserver(this);
    }

    public void update(Observable observable, Object arg) {
        if (observable instanceof WeatherData) {
            WeatherData weatherData = (WeatherData)observable;
            float temp = weatherData.getTemperature();
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
    }

    public void display() {
        System.out.println("Avg/Max/Min temperature = " + (mTempSum / mNumReadings)
            + "/" + mMaxTemp + "/" + mMinTemp);
    }
}
