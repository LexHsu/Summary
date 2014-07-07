package weatherobservable.observer;

import java.util.Observable;
import java.util.Observer;

import weatherobservable.observable.WeatherData;

public class ForecastDisplay implements Observer, DisplayElement {
    private float mCurrentPressure = 29.92f;  
    private float mLastPressure;

    public ForecastDisplay(Observable observable) {
        observable.addObserver(this);
    }

    public void update(Observable observable, Object arg) {
        if (observable instanceof WeatherData) {
            WeatherData weatherData = (WeatherData)observable;
            mLastPressure = mCurrentPressure;
            mCurrentPressure = weatherData.getPressure();
            display();
        }
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
