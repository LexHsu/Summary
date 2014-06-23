package weatherobservable.observer;

import java.util.Observable;
import java.util.Observer;

import weatherobservable.observable.WeatherData;
    
public class CurrentConditionsDisplay implements Observer, DisplayElement {
    Observable mObservable;
    private float mTemperature;
    private float mHumidity;
    
    public CurrentConditionsDisplay(Observable observable) {
        this.mObservable = observable;
        observable.addObserver(this);
    }
    
    public void update(Observable obs, Object arg) {
        if (obs instanceof WeatherData) {
            WeatherData weatherData = (WeatherData)obs;
            this.mTemperature = weatherData.getTemperature();
            this.mHumidity = weatherData.getHumidity();
            display();
        }
    }
    
    public void display() {
        System.out.println("Current conditions: " + mTemperature 
            + "F degrees and " + mHumidity + "% humidity");
    }
}
