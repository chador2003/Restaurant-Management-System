import Restaurant.*;
public class NoDiscount implements DiscountStrategy {
    @Override
    public float applyDiscount(float totalCost) {
        return totalCost;
    }
}
