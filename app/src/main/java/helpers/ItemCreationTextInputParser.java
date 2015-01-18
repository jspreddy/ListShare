package helpers;

/**
 * Created by Sai on 1/18/2015.
 */


public class ItemCreationTextInputParser {
    enum InputCredibility {FULL, MEDIUM, LOW}

    private int count;
    private String itemName;
    private int qty;
    private String unit;
    private InputCredibility crediblity;


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
    private void parseInputString(String input) {
        String[] array = input.split("\\s", -1);
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
