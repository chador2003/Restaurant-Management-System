import Restaurant.TableState;
import Restaurant.Table;
public class OccupiedState implements TableState {
    @Override
    public void handleState(Table table) {
        System.out.println("Table " + table.getTableNo() + " is now occupied.");
        table.setState(this);
    }

    @Override
    public String getState() {
        return "Occupied";
    }
    public String toString() {
        return "Occupied";
    }
}
