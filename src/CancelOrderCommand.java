import Restaurant.*;
public class CancelOrderCommand implements Command {
    private Order order;
    private OrderDAO orderDAO;

    public CancelOrderCommand(Order order, OrderDAO orderDAO) {
        this.order = order;
        this.orderDAO = orderDAO;
    }

    public void execute() {
        // Remove the order from the database
        orderDAO.deleteOrder(order.getId());
    }

    @Override
    public void undo() {
        
    }

}
