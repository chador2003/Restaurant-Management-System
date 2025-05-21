import Restaurant.*;
public class TwentyPercentDiscount implements DiscountStrategy {
    @Override
    public float applyDiscount(float totalCost) {
        return totalCost * 0.8f;
    }
}
