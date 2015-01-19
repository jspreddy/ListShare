package helpers;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sai on 1/18/2015.
 */


public class ItemCreationTextInputParser {
    enum InputCredibility {FULL, MEDIUM, LOW}

    private int count = -1;
    private String itemName = "";
    private int qty = -1;
    private String unit = "";
    private InputCredibility crediblity;
    private List<String> input;

    private int DEFAULT_COUNT = 1;
    private int DEFAULT_QTY = 1;
    private String DEFAULT_UNIT = "";

    ItemCreationTextInputParser(String input) {
        this.parseInputString(input);
    }

    /**
     * Input Formats:
     * "1 item name 3unit"
     * "3 item name 6"
     * "item name 3 unit"
     * "very long item name 4unit"
     * "item name 4"
     * "item name"
     */
    private void parseInputString(String str) {
        String[] array = str.split("\\s", -1);
        if(array.length<=0){
            crediblity = InputCredibility.LOW;
        }
        input = Arrays.asList(array);
        this.count = this.parseOutCountFromString();
        this.qty  = this.parseOutQtyFromString();
    }

    private int parseOutCountFromString(){
        while(input.size()>0){
            String currentString = input.get(0);
            if(currentString.isEmpty()) {
                input.remove(0);
                continue;
            }

            if(currentString.matches("\\d+")){
                input.remove(0);
                return Integer.parseInt(currentString);
            }
            else{
                break;
            }
        }
        return DEFAULT_COUNT;
    }

    private int parseOutQtyFromString(){

        return DEFAULT_QTY;
    }

    public int getCount() {
        return count;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public InputCredibility getCrediblity() {
        return crediblity;
    }
}
