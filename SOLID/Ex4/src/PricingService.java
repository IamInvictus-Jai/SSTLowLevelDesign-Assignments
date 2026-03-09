import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class PricingService {
    private Map<Integer, PricingComponent> roomComponentMap;
    private Map<AddOn, PricingComponent> addOnComponentMap;

    public PricingService() {
        this.roomComponentMap = new HashMap<>();
        this.roomComponentMap.put(LegacyRoomTypes.SINGLE, new SingleRoomPricingComponent());
        this.roomComponentMap.put(LegacyRoomTypes.DOUBLE, new DoubleRoomPricingComponent());
        this.roomComponentMap.put(LegacyRoomTypes.TRIPLE, new TripleRoomPricingComponent());

        this.addOnComponentMap = new HashMap<>();
        this.addOnComponentMap.put(AddOn.MESS, new MessAddOnPricingComponent());
        this.addOnComponentMap.put(AddOn.GYM, new GymAddOnPricingComponent());
        this.addOnComponentMap.put(AddOn.LAUNDRY, new LaundryAddOnPricingComponent());
    }

    private List<PricingComponent> getRoomComponent(int roomType) {
        List<PricingComponent> component = new ArrayList<>();
        component.add(this.roomComponentMap.get(roomType));
        return component;
    }

    private List<PricingComponent> getAddOnComponents(List<AddOn> addOns) {
        List<PricingComponent> components = new ArrayList<>();
        for (AddOn i:addOns) {
            components.add(this.addOnComponentMap.get(i));
        }
        return components;
    }

    public List<PricingComponent> getComponents(BookingRequest req) {
        List<PricingComponent> components = getRoomComponent(req.roomType);
        components.addAll(getAddOnComponents(req.addOns));
        return components;
    }
}