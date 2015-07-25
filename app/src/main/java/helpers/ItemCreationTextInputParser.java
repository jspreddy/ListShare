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

    public ItemCreationTextInputParser(String input) {
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
        if (array.length <= 0) {
            crediblity = InputCredibility.LOW;
        }
        input = Arrays.asList(array);
        this.parseOutCountFromString();
        this.parseOutQtyFromString();
    }

    private void parseOutCountFromString() {
        while (input.size() > 0) {
            String currentString = input.get(0);
            if (currentString.isEmpty()) {
                input.remove(0);
                continue;
            }

            if (currentString.matches("\\d+")) {
                input.remove(0);
                this.count =  Integer.parseInt(currentString);
                return;
            } else {
                break;
            }
        }
        this.count = this.DEFAULT_COUNT;
    }

    private void parseOutQtyFromString() {
        //TODO: parse out the quantity from the end of the input string.
        while (input.size() > 0) {
            // get the last item
            String currentString = input.get(input.size());
            String previousString = input.get(input.size() - 1);

            if(currentString.isEmpty()){
                input.remove(input.size());
                continue;
            }

            if(currentString.matches("[a-zA-Z]]*") && previousString.matches("\\d+")){
                input.remove(input.size());
                this.unit = currentString;
                this.qty = Integer.parseInt(previousString);
                return;
            }
            else if(currentString.matches("[0-9]+[a-zA-Z]*]")){
                input.remove(input.size());
                this.splitQtyAndUnit(currentString);
                return;
            }
            else if(currentString.matches("\\d+")){
                this.qty=Integer.parseInt(currentString);
                this.unit = this.DEFAULT_UNIT;
                return;
            }
            else{
                this.qty = this.DEFAULT_QTY;
                this.unit = this.DEFAULT_UNIT;
                return;
            }
        }
        this.qty =  this.DEFAULT_QTY;
        this.unit = this.DEFAULT_UNIT;
    }

    private void splitQtyAndUnit(String currentString) {
        //TODO: split the 9kg into qty=9 and units=kg

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
