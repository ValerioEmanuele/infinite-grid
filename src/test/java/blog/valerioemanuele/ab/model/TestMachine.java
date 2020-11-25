package blog.valerioemanuele.ab.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestMachine {

	@Autowired
	private Machine machine;
	
	@BeforeEach
	public void initTestValues() {
		machine.init();
	}
	
	@Test
	void test_initialStatus() {
		assertEquals(Direction.RIGHT, machine.getDirection());
		assertEquals(Point.of(BigInteger.ZERO,BigInteger.ZERO), machine.getPosition());
	}
	
	@Test
	void test_clockwiseMove_rotation() {
		machine.clockwiseMove();
		assertEquals(Direction.DOWN, machine.getDirection());
		
		machine.clockwiseMove();
		assertEquals(Direction.LEFT, machine.getDirection());
		
		machine.clockwiseMove();
		assertEquals(Direction.UP, machine.getDirection());
		
		machine.clockwiseMove();
		assertEquals(Direction.RIGHT, machine.getDirection());
	}
	
	@Test
	void test_counterClockwiseMove_rotation() {
		machine.counterClockwiseMove();
		assertEquals(Direction.UP, machine.getDirection());
		
		machine.counterClockwiseMove();
		assertEquals(Direction.LEFT, machine.getDirection());
		
		machine.counterClockwiseMove();
		assertEquals(Direction.DOWN, machine.getDirection());
		
		machine.counterClockwiseMove();
		assertEquals(Direction.RIGHT, machine.getDirection());
	}
	
	@Test
	void test_spaceMove_fromOrigin() {
		machine.move();
		assertEquals(Point.of(BigInteger.ONE,BigInteger.ZERO), machine.getPosition());
		
		machine.init();
		machine.clockwiseMove();
		assertEquals(Direction.DOWN, machine.getDirection());
		machine.move();
		assertEquals(Point.of(BigInteger.ZERO,BigInteger.ONE.negate()), machine.getPosition());
		
		machine.init();
		machine.counterClockwiseMove();
		assertEquals(Direction.UP, machine.getDirection());
		machine.move();
		assertEquals(Point.of(BigInteger.ZERO, BigInteger.ONE), machine.getPosition());
		
		
		machine.init();
		machine.counterClockwiseMove();
		machine.counterClockwiseMove();
		assertEquals(Direction.LEFT, machine.getDirection());
		machine.move();
		assertEquals(Point.of(BigInteger.ONE.negate(), BigInteger.ZERO), machine.getPosition());
	}
	
	
	@Test
	void test_spaceMove_manyMoves() {		
		IntStream.range(0, 10).forEach(i -> machine.move());
		assertEquals(Point.of(BigInteger.TEN, BigInteger.ZERO), machine.getPosition());
		
		machine.clockwiseMove();
		IntStream.range(0, 5).forEach(i -> machine.move());
		assertEquals(Point.of(BigInteger.TEN, BigInteger.valueOf(-5)), machine.getPosition());
		
		machine.counterClockwiseMove();
		machine.counterClockwiseMove();
		IntStream.range(0, 10).forEach(i -> machine.move());
		assertEquals(Point.of(BigInteger.TEN, BigInteger.valueOf(5)), machine.getPosition());
		
		
		machine.counterClockwiseMove();
		IntStream.range(0, 30).forEach(i -> machine.move());
		assertEquals(Point.of(BigInteger.valueOf(-20), BigInteger.valueOf(5)), machine.getPosition());
	}
	
	@Test
	void test_spaceMove_negativeCoordinates() {
		machine.setPosition(Point.of(BigInteger.ONE.negate(), BigInteger.ONE.negate()));
		machine.setDirection(Direction.LEFT);
		
		machine.move();
		assertEquals(Point.of(BigInteger.TWO.negate(), BigInteger.ONE.negate()), machine.getPosition());
		
		machine.setDirection(Direction.DOWN);
		machine.move();
		assertEquals(Point.of(BigInteger.TWO.negate(), BigInteger.TWO.negate()), machine.getPosition());
		
	}

}
