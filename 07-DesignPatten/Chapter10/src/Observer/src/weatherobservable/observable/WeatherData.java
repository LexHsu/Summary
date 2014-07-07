package weatherobservable.observable;
    
import java.util.Observable;
import java.util.Observer;
    
public class WeatherData extends Observable {
    private float mTemperature;
    private float mHumidity;
    private float mPressure;
    
    public WeatherData() { }
    
    public void measurementsChanged() {
        setChanged();
        notifyObservers();
    }
    
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.mTemperature = temperature;
        this.mHumidity = humidity;
        this.mPressure = pressure;
        measurementsChanged();
    }
    
    public float getTemperature() {
        return mTemperature;
    }
    
    public float getHumidity() {
        return mHumidity;
    }
    
    public float getPressure() {
        return mPressure;
    }
}
