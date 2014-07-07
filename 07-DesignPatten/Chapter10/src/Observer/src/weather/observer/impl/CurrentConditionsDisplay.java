package weather.observer.impl;

import weather.observer.DisplayElement;
import weather.observer.Observer;
import weather.subject.Subject;
    
public class CurrentConditionsDisplay implements Observer, DisplayElement {
    private float mTemperature;
    private float mHumidity;
    private Subject mWeatherData;
    
    public CurrentConditionsDisplay(Subject weatherData) {
        this.mWeatherData = weatherData;
        weatherData.registerObserver(this);
    }
    
    public void update(float temperature, float humidity, float pressure) {
        this.mTemperature = temperature;
        this.mHumidity = humidity;
        display();
    }
    
    public void display() {
        System.out.println("Current conditions: " + mTemperature 
            + "F degrees and " + mHumidity + "% humidity");
    }
}
