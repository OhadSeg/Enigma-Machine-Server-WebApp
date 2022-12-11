package components;

import generated.CTEReflect;
import generated.CTEReflector;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import utils.RomanNumbers;

public class Reflector implements Serializable {

    private Map<Integer, Integer> reflector = new HashMap<>();
    private int amountOfMapping;
    private RomanNumbers id;

    public Reflector(CTEReflector cteReflector) {
        for (CTEReflect cteReflect : cteReflector.getCTEReflect()) {
            this.reflector.put(cteReflect.getInput() - 1, cteReflect.getOutput() - 1);
            this.reflector.put(cteReflect.getOutput() - 1, cteReflect.getInput() - 1);
            amountOfMapping += 2;

            try {
                this.id = RomanNumbers.valueOf(cteReflector.getId());
            } catch (IllegalArgumentException e) {
                this.id = RomanNumbers.INVALID;
            }
        }
    }

    public RomanNumbers getId() {
        return id;
    }

    protected int run(int entryToReflector) {
        return reflector.get(entryToReflector);
    }

    protected boolean isValidReflector(int abcLength) {

        if (reflector.size() != abcLength || reflector.size() != amountOfMapping) {
            return false;
        }
        for (Map.Entry<Integer, Integer> value : reflector.entrySet()) {
            if (value.getKey() == value.getValue()) {
                return false;
            }
            if (value.getKey() < 0 || value.getKey() > reflector.size() - 1) {
                return false;
            }
            if (value.getValue() < 0 || value.getValue() > reflector.size() - 1) {
                return false;
            }
        }
        return true;
    }
}
