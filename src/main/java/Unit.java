import java.util.HashMap;
import java.util.Map;

public class Unit {

    private static final Map<String, Map<UnitEnum, Double>> CONVERSION_MAP = new HashMap<>();

    static {
        // Tomate
        Map<UnitEnum, Double> tomate = new HashMap<>();
        tomate.put(UnitEnum.KG, 1.0);
        tomate.put(UnitEnum.PCS, 10.0);
        CONVERSION_MAP.put("Tomate", tomate);

        // Laitue
        Map<UnitEnum, Double> laitue = new HashMap<>();
        laitue.put(UnitEnum.KG, 1.0);
        laitue.put(UnitEnum.PCS, 2.0);
        CONVERSION_MAP.put("Laitue", laitue);

        // Chocolat
        Map<UnitEnum, Double> chocolat = new HashMap<>();
        chocolat.put(UnitEnum.KG, 1.0);
        chocolat.put(UnitEnum.PCS, 10.0);
        chocolat.put(UnitEnum.L, 2.5);
        CONVERSION_MAP.put("Chocolat", chocolat);

        // Poulet
        Map<UnitEnum, Double> poulet = new HashMap<>();
        poulet.put(UnitEnum.KG, 1.0);
        poulet.put(UnitEnum.PCS, 8.0);
        CONVERSION_MAP.put("Poulet", poulet);

        // Beurre
        Map<UnitEnum, Double> beurre = new HashMap<>();
        beurre.put(UnitEnum.KG, 1.0);
        beurre.put(UnitEnum.PCS, 4.0);
        beurre.put(UnitEnum.L, 5.0);
        CONVERSION_MAP.put("Beurre", beurre);
    }

    public static double convertToKG(String ingredientName, double quantity, UnitEnum fromUnit) {
        if (fromUnit == UnitEnum.KG) {
            return quantity;
        }

        Map<UnitEnum, Double> conversions = CONVERSION_MAP.get(ingredientName);
        if (conversions == null || !conversions.containsKey(fromUnit)) {
            throw new IllegalArgumentException("Conversion impossible pour " + ingredientName +
                    " de " + fromUnit + " vers KG");
        }

        double conversionRate = conversions.get(fromUnit);
        return quantity / conversionRate;
    }

    public static boolean canConvert(String ingredientName, UnitEnum fromUnit, UnitEnum toUnit) {
        if (fromUnit == toUnit) return true;

        Map<UnitEnum, Double> conversions = CONVERSION_MAP.get(ingredientName);
        if (conversions == null) return false;

        return conversions.containsKey(fromUnit) && conversions.containsKey(toUnit);
    }
}