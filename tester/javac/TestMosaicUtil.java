import com.ReasoningTechnology.Mosaic;  // Importing the entire Mosaic package

public class TestMosaicUtil {

    public static void main(String[] args) {
        // Sample array of conditions to check with Util.all
        Boolean[] conditions = {true, true, true};

        // Using Mosaic.Util.all() to check if all conditions are true
        boolean result = Mosaic.Util.all(conditions);

        // Print the result
        System.out.println("All conditions are true: " + result);
    }
}
