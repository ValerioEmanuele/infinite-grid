package blog.valerioemanuele.ab.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import blog.valerioemanuele.ab.exceptions.InvalidInputException;
import blog.valerioemanuele.ab.provider.FourPointsProvider;
import blog.valerioemanuele.ab.provider.RandomPointsProvider;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TestInfiniteGrid {

	@Autowired
	private InfiniteGrid memoryInfiniteGrid;
	
	@Autowired
	private InfiniteGrid databaseInfiniteGrid;
	
	private String W;
	private String B;
	private static final String N = System.lineSeparator();
	
	@BeforeEach
	public void initTest() {
		W = databaseInfiniteGrid.getWHITE_SQUARE();
		B = databaseInfiniteGrid.getBLACK_SQUARE();
	}
	
	@Test
	void test_initialStatus() {
		assertNotNull(memoryInfiniteGrid);
		assertNotNull(databaseInfiniteGrid);
		assertTrue(memoryInfiniteGrid.noBlackSquares());
		assertTrue(databaseInfiniteGrid.noBlackSquares());
	}
	
	@Test
	void test_putNullPoint_throwsException() {
		assertThrows(InvalidInputException.class, () -> memoryInfiniteGrid.put(null));
	}
	
	@Test
	void test_putNotValidPoint_throwsException() {
		assertThrows(InvalidInputException.class, () -> memoryInfiniteGrid.put(Point.builder().build()));

		assertThrows(InvalidInputException.class, () -> memoryInfiniteGrid.put(Point.builder().x(BigInteger.ONE).build()));

		assertThrows(InvalidInputException.class, () -> memoryInfiniteGrid.put(Point.builder().y(BigInteger.ONE).build()));
	}
	
	@Test
	void test_putPoint() {
		Point p = Point.of(BigInteger.ONE, BigInteger.valueOf(2));
		assertNotNull(memoryInfiniteGrid.put(p));
	}
	
	@Test
	void test_gridGraphicalRepresentation_gridOfWhiteSquares() throws IOException {
		memoryInfiniteGrid.gridToFile();
		assertEquals("", FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	}
	
	
	@ParameterizedTest
	@ArgumentsSource(RandomPointsProvider.class)
	void test_getMinAndMaxPoints(List<Point> points) {
		//init the grid with the points
		points.stream()
			.peek(p -> p.setExecutionId(databaseInfiniteGrid.executionId()))
			.forEach(p -> {
				databaseInfiniteGrid.put(p);
				memoryInfiniteGrid.put(p);
			});
		
		
		// retrieve MIN X
		Optional<BigInteger> tmpVal = databaseInfiniteGrid.getMinXPoint();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MIN_X, tmpVal.get());
		
		tmpVal = memoryInfiniteGrid.getMinXPoint();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MIN_X, tmpVal.get());
		
		// retrieve MAX X
		tmpVal = databaseInfiniteGrid.getMaxX();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MAX_X, tmpVal.get());
		
		tmpVal = memoryInfiniteGrid.getMaxX();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MAX_X, tmpVal.get());
		
		
		// retrieve MIN Y
		tmpVal = databaseInfiniteGrid.getMinY();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MIN_Y, tmpVal.get());
		
		tmpVal = memoryInfiniteGrid.getMinY();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MIN_Y, tmpVal.get());
		
		// retrieve MAX Y
		tmpVal = databaseInfiniteGrid.getMaxY();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MAX_Y, tmpVal.get());
		
		tmpVal = memoryInfiniteGrid.getMaxY();
		
		assertTrue(tmpVal.isPresent());
		assertEquals(RandomPointsProvider.MAX_Y, tmpVal.get());
	}
	
	
	@Test
	void test_gridGraphicalRepresentation_gridOfOneBlackSquare() throws IOException {
		Point p = Point.of(BigInteger.ONE, BigInteger.valueOf(2), databaseInfiniteGrid.executionId());
		
		assertTrue(databaseInfiniteGrid.put(p));
		
		databaseInfiniteGrid.gridToFile();
		
		/*
		 * Expected grid 0
		 */
		String expectedGrid = B;					
							  

	    assertEquals(expectedGrid, FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	}
	
	
	@Test
	void test_gridGraphicalRepresentation_gridOfTwoBlackSquares() throws IOException {
		Point p = Point.of(BigInteger.ONE, BigInteger.ONE, databaseInfiniteGrid.executionId());
		
		assertTrue(databaseInfiniteGrid.put(p));
		p = Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate(), databaseInfiniteGrid.executionId());
		assertTrue(databaseInfiniteGrid.put(p));
		
		databaseInfiniteGrid.gridToFile();
		
		/*
		 * Expected grid  
		 * 1 1 0
		 * 1 1 1
		 * 0 1 1
		 */
		String expectedGrid = W + W + B + N +
							  W + W + W + N +
							  B + W + W;
							  

	    assertEquals(expectedGrid, FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	}
	
	@Test
	void test_gridGraphicalRepresentation_gridOfThreeBlackSquares() throws IOException {
		Point p = Point.of(BigInteger.ZERO, BigInteger.ZERO, databaseInfiniteGrid.executionId());
		
		assertTrue(databaseInfiniteGrid.put(p));
		p = Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate(), databaseInfiniteGrid.executionId());
		assertTrue(databaseInfiniteGrid.put(p));
		
		p = Point.of(BigInteger.ONE, BigInteger.ONE.negate(), databaseInfiniteGrid.executionId());
		assertTrue(databaseInfiniteGrid.put(p));
		
		databaseInfiniteGrid.gridToFile();
		
		/*
		 * Expected grid  
		 * 1 0 1
		 * 0 1 0
		 */
		String expectedGrid = W + B + W + N +
							  B + W + B;
							  

	    assertEquals(expectedGrid, FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	}
	
	
	@Test
	void test_gridGraphicalRepresentation_gridOfThreeInlineBlackSquares() throws IOException {
		Point p = Point.of(BigInteger.ZERO, BigInteger.ZERO, databaseInfiniteGrid.executionId());
		
		assertTrue(databaseInfiniteGrid.put(p));
		p = Point.of(BigInteger.ONE.negate(), BigInteger.ZERO, databaseInfiniteGrid.executionId());
		assertTrue(databaseInfiniteGrid.put(p));
		
		p = Point.of(BigInteger.ONE, BigInteger.ZERO, databaseInfiniteGrid.executionId());
		assertTrue(databaseInfiniteGrid.put(p));
		
		databaseInfiniteGrid.gridToFile();
		
		/*
		 * Expected grid  
		 * 0 0 0
		 */
		String expectedGrid =  B + B + B;
							  

	    assertEquals(expectedGrid, FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	}
	
	@ParameterizedTest
	@ArgumentsSource(FourPointsProvider.class)
	void test_gridGraphicalRepresentation_gridOfFourBlackSquares(List<Point> points) throws IOException {
		points.stream()
			  .peek(p -> p.setExecutionId(databaseInfiniteGrid.executionId()))
			  .forEach(p -> databaseInfiniteGrid.put(p));
		
		databaseInfiniteGrid.gridToFile();
		
		/*
		 * Expected grid
		 * 0 1 1 1 0
		 * 1 1 1 1 1
		 * 1 1 1 1 1
		 * 1 1 1 1 1
		 * 0 1 1 1 0
		 *  		 
		 * */
		String expectedGrid = B + W + W + W + B + N + 
							  W + W + W + W + W + N +
							  W + W + W + W + W + N +
							  W + W + W + W + W + N +
							  B + W + W + W + B;					
							  

	    assertEquals(expectedGrid, FileUtils.readFileToString(FileUtils.getFile(databaseInfiniteGrid.filePath()), "utf-8"));
	
	}
 
}
