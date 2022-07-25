import com.joannholland.minesweeper.Cell;
import com.joannholland.minesweeper.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTest {
    @Test
    public void test_isRevealed() {
        Location testLocation = new Location();
        testLocation.setRow(1);
        testLocation.setColumn(3);

        Cell testCell = new Cell(testLocation, 1, 3, 10, false, false, false);

    }
}
