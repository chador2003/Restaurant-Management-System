import Restaurant.*;
public class TenPercentDiscount implements DiscountStrategy {
    @Override
    public float applyDiscount(float totalCost) {
        return totalCost * 0.9f;
    }
}
