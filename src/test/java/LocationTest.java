import com.joannholland.minesweeper.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    @Test
    public void test_locationConstructor() {
        Location testLocation = new Location();
        testLocation.setRow(1);
        testLocation.setColumn(3);
        assertEquals(1, testLocation.getRow(), "Location doesn't set the row correctly in the constructor.");
        assertEquals(3, testLocation.getColumn(), "Location doesn't set the column correctly in the constructor.");
    }

    @Test
    public void test_getRow() {
        Location testLocation = new Location();
        testLocation.setRow(3);
        assertEquals(3, testLocation.getRow(), "The expected row is incorrect for the getRow method.");
    }

    @Test
    public void test_setRow() {
        Location testLocation = new Location();
        testLocation.setRow(1);
        assertEquals(1, testLocation.getRow(), "The expected row is incorrect for the setRow method.");
    }

    @Test
    public void test_getColumm() {
        Location testLocation = new Location();
        testLocation.setColumn(3);
        assertEquals(3, testLocation.getColumn(), "The expected column is incorrect for the getColumn method.");
    }

    @Test
    public void test_setColumn() {
        Location testLocation = new Location();
        testLocation.setColumn(1);
        assertEquals(1, testLocation.getColumn(), "The expected column is incorrect for the setColumn method.");
    }

    @Test
    public void test_equals() {
        Location testLocation1 = new Location();
        testLocation1.setRow(1);
        testLocation1.setColumn(3);

        Location testLocation2 = new Location();
        testLocation2.setRow(1);
        testLocation2.setColumn(3);

        assertEquals(true, testLocation1.equals(testLocation2), "The two locations were expected to be equal, but returns false.");
    }

    @Test
    public void test_toString() {
        Location testLocation = new Location();
        testLocation.setRow(1);
        testLocation.setColumn(3);
        assertEquals("[1, 3]", testLocation.toString(), "The Location toString method did not output the expected string.");
    }
}
