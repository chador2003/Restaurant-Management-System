import Restaurant.*;
public class ReservedState implements TableState {
    @Override
    public void handleState(Table table) {
        System.out.println("Table " + table.getTableNo() + " is now reserved.");
        table.setState(this);
    }

    @Override
    public String getState() {
        return "Reserved";
    }
    public String toString() {
        return "Reserved";
    }
}
