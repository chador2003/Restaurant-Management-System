import java.util.ArrayList;
import java.util.List;
import Restaurant.*;

public class FormFieldObservable {
    private List<FormFieldObserver> observers = new ArrayList<>();

    public void addObserver(FormFieldObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String fieldName, String value) {
        for (FormFieldObserver observer : observers) {
            observer.update(fieldName, value);
        }
    }
}
