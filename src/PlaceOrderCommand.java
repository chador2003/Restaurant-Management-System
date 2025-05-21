
import Restaurant.*;
public class PlaceOrderCommand implements Command {
    private Order order;
    private OrderDAO orderDAO;

    public PlaceOrderCommand(Order order, OrderDAO orderDAO) {
        this.order = order;
        this.orderDAO = orderDAO;
    }

    @Override
    public void execute() {
        orderDAO.placeOrder(order);
    }

    @Override
    public void undo() {
        // Implement undo logic if needed
    }

}
