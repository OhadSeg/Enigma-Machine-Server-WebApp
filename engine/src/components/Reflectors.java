package components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import generated.CTEReflector;
import generated.CTEReflectors;
import utils.RomanNumbers;

public class Reflectors implements Serializable {

    private Map<RomanNumbers, Reflector> reflectors = new HashMap<>();
    private RomanNumbers reflectorInUse;
    private int totalReflectors;

    public Reflectors(CTEReflectors cteReflectors) {

        List<CTEReflector> cteReflectorList = cteReflectors.getCTEReflector();

        for (CTEReflector cteReflector : cteReflectorList) {

            Reflector reflector = new Reflector(cteReflector);
            this.reflectors.put(reflector.getId(), reflector);
            totalReflectors++;
        }
    }

    protected int getTotalReflectors() {
        return totalReflectors;
    }

    protected void setReflectorInUseFromDto(RomanNumbers reflectorInUse) {

        this.reflectorInUse = reflectorInUse;
    }

    protected int run(int entryToReflector) {
        return reflectors.get(reflectorInUse).run(entryToReflector);
    }

    public boolean checkIfAllReflectorsIdsValid() {
        for (Map.Entry<RomanNumbers, Reflector> reflector : reflectors.entrySet()) {
            if (reflector.getKey().ordinal() < 0 || reflector.getKey().ordinal() >= reflectors.size() || reflector.getKey().ordinal() == 5) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfAllReflectorsValid(int abcLength) {
        for (Map.Entry<RomanNumbers, Reflector> reflector : reflectors.entrySet()) {
            if (reflector.getValue().isValidReflector(abcLength) == false) {
                return false;
            }
        }
        return true;
    }

    protected void setReflectorInUse(RomanNumbers reflector){
        this.reflectorInUse = reflector;
    }

    @Override
    public String toString() {
        StringBuilder reflectorDetails = new StringBuilder();
        reflectorDetails.append('<');
        reflectorDetails.append(reflectorInUse);
        reflectorDetails.append('>');
        return reflectorDetails.toString();
    }
}
