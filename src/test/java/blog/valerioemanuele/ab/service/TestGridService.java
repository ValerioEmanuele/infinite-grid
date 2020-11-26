package blog.valerioemanuele.ab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import blog.valerioemanuele.ab.exceptions.InvalidStepsException;
import blog.valerioemanuele.ab.model.Direction;
import blog.valerioemanuele.ab.model.InfiniteGrid;
import blog.valerioemanuele.ab.model.Point;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TestGridService {

	@Autowired
	private GridService gridService;
	
	@Autowired
	private MemoryInfiniteGrid memoryGrid;
	
	@Autowired
	private DatabaseInfiniteGrid databaseGrid;
	
	@Test
	void test_moveOfNegative_throwsException() {
		assertThrows(InvalidStepsException.class, () -> gridService.gridFor(BigInteger.ONE.negate()));
	}
	
	@Test
	void test_moveOfNull_throwsException() {
		assertThrows(InvalidStepsException.class, () -> gridService.gridFor(null));
	}
	
	@Test
	void test_initialConditions() {
		assertEquals(BigInteger.ZERO, memoryGrid.countBlackSquares());
	}
	
	
	@Test
	void test_moveOfOneStep() {
		gridService.moveMachine(memoryGrid);
		
		assertEquals(BigInteger.ONE, memoryGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ONE.negate()).equals(gridService.machinePosition()));
		
		assertEquals(Direction.DOWN, gridService.machineDirection());
	}
	
	
	@Test
	void test_moveOfOneStep_fromBlackSquare() throws IOException {
		Point startPoint = Point.of(BigInteger.ZERO, BigInteger.ZERO);
		//set the start point as black square
		memoryGrid.put(startPoint);
		
		gridService.gridFor(BigInteger.ONE);
		
		assertEquals(BigInteger.ZERO, memoryGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ONE).equals(gridService.machinePosition()));
		
		assertEquals(Direction.UP, gridService.machineDirection());
	}
	
	@Test
	void test_moveOfTwoSteps() {
		gridService.moveMachine(memoryGrid);
		gridService.moveMachine(memoryGrid);
		
		assertEquals(BigInteger.valueOf(2), memoryGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate()).equals(gridService.machinePosition()));
		assertEquals(Direction.LEFT, gridService.machineDirection());
	}
	
	@Test
	void test_moveOfXSteps() {
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,0}
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ONE.negate()).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,0},{0,-1}
		assertEquals(BigInteger.valueOf(2), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate()).equals(gridService.machinePosition()));
		assertEquals(Direction.LEFT, gridService.machineDirection());
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,0},{0,-1},{-1,-1}
		assertEquals(BigInteger.valueOf(3), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.ZERO).equals(gridService.machinePosition()));
		assertEquals(Direction.UP, gridService.machineDirection());
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,0},{0,-1},{-1,-1},{-1,0}
		assertEquals(Direction.RIGHT, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(4), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ZERO).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0}
		assertEquals(Direction.UP, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(3), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ONE).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{0,1}
		assertEquals(Direction.RIGHT, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(4), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE, BigInteger.ONE).equals(gridService.machinePosition()));
		
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{0,1},{1,1}
		assertEquals(Direction.DOWN, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(5), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE, BigInteger.ZERO).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{0,1},{1,1},{1,0}
		assertEquals(Direction.LEFT, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(6), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ZERO).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{0,1},{1,1},{1,0},{0,0}
		assertEquals(Direction.UP, gridService.machineDirection());
		assertEquals(BigInteger.valueOf(7), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ZERO, BigInteger.ONE).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{1,1},{1,0},{0,0}
		assertEquals(BigInteger.valueOf(6), databaseGrid.countBlackSquares());
		assertEquals(Direction.LEFT, gridService.machineDirection());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.ONE).equals(gridService.machinePosition()));
		
		gridService.moveMachine(databaseGrid);
		//Black Squares={0,-1},{-1,-1},{-1,0},{1,1},{1,0},{0,0},{-1,1}
		assertEquals(BigInteger.valueOf(7), databaseGrid.countBlackSquares());
		assertEquals(Direction.UP, gridService.machineDirection());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.valueOf(2)).equals(gridService.machinePosition()));
		
	}
	
	
	/**
	 * Pay attention: it takes long time to execute it
	 * @throws IOException 
	 */
	@Test
	@Disabled
	void test_BigStep() throws IOException {
		gridService.gridFor(BigInteger.valueOf(Integer.MAX_VALUE));
		System.out.println("Number of black squares: "+memoryGrid.countBlackSquares());
		//Number of black squares: 247 785 763
		// 30 minutes to complete
	}
	
	@Test
	void test_GridTypeByThreshold() {
		
		InfiniteGrid grid = gridService.gridOf(BigInteger.ONE);
		assertTrue(grid instanceof MemoryInfiniteGrid);
		
		grid = gridService.gridOf(BigInteger.valueOf(65535));
		assertTrue(grid instanceof DatabaseInfiniteGrid);
		
	}
	
	@Test
	void test_moveOfTwoSteps_withDatabaseGrid() {
		gridService.moveMachine(databaseGrid);
		gridService.moveMachine(databaseGrid);
		
		assertEquals(BigInteger.valueOf(2), databaseGrid.countBlackSquares());
		assertTrue(Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate()).equals(gridService.machinePosition()));
		assertEquals(Direction.LEFT, gridService.machineDirection());
	}
	
	/**
	 * Pay attention: it takes long time to execute it
	 * @throws IOException 
	 */
	@Test
	@Disabled
	void test_moveOfManySteps_withDatabaseGrid() throws IOException {
		gridService.gridFor(BigInteger.valueOf(Integer.MAX_VALUE));
		assertTrue(true);
	}

}
