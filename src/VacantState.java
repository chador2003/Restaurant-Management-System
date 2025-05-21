import Restaurant.TableState;
import Restaurant.Table;
public class VacantState implements TableState {
    @Override
    public void handleState(Table table) {
        // Logic to handle vacant state
    }

    @Override
    public String getState() {
        return "Vacant";
    }
    public String toString() {
        return getState();
    }
}
