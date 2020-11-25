package blog.valerioemanuele.ab.service;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import blog.valerioemanuele.ab.exceptions.InvalidStepsException;
import blog.valerioemanuele.ab.model.Direction;
import blog.valerioemanuele.ab.model.InfiniteGrid;
import blog.valerioemanuele.ab.model.Machine;
import blog.valerioemanuele.ab.model.Point;
import lombok.Data;
import lombok.NonNull;

/**
 * This class contains the logic that make the machine move on the infinity grid
 * @author Valerio Emanuele
 *
 */
@Service
@Data
public class GridService {
	
	@Autowired
	private Machine machine;
	
	@Autowired
	private ApplicationContext context;
	
	/**
	 * This parameter determine which implementation of the InfiniteGrid
	 * will be used at runtime.
	 * 
	 * If the number of steps is under this parameter value then the in memory
	 * implementation will be used, otheriwse the database implementation
	 * will be used
	 */
	@Value("${threshold:65535}")
	private Long threshold;
	
	private InfiniteGrid grid;
	
	public void gridFor(BigInteger numberOfSteps) throws IOException {
		validateInput(numberOfSteps);
		grid = gridOf(numberOfSteps);
		
		for (BigInteger bi = BigInteger.ZERO; bi.compareTo(numberOfSteps) < 0; bi = bi.add(BigInteger.ONE)) {
			moveMachine(grid);
		}
		
		grid.gridToFile();
	}

	public void moveMachine(InfiniteGrid grid) {
		Point p = machinePosition();
		p.setExecutionId(grid.executionId());
		
		boolean isCurrSquareBlack = grid.contains(p);
		
		if(isCurrSquareBlack) {
			machine.counterClockwiseMove();
			grid.remove(p);
		}
		else {
			machine.clockwiseMove();
			grid.put(p);
		}
		machine.move();
	}

	private void validateInput(BigInteger numberOfSteps) {
		if(numberOfSteps == null || numberOfSteps.signum() == -1) {
			throw new InvalidStepsException("The number of steps must be a positive number");
		}
	}
	
	public Direction machineDirection() {
		return machine.getDirection();
	}
	
	public Point machinePosition() {
		machine.getPosition();
		return Point.of(machine.getPosition());
	}

	/**
	 * Returns a different instance of InfiniteGrid based on the number of steps to execute
	 * If the number of step is under a threshold than the in RAM memory representation of the 
	 * grid is used. Otherwise the database stored implementation is used
	 * @param numberOfSteps - the number of step that the Machine will execute
	 * @return an instance of MemoryInfiniteGrid if the num. of steps is under the threshold, 
	 * 		   otherwise an instance of DatabaseInfiniteGrid
	 */
	public InfiniteGrid gridOf(@NonNull BigInteger numberOfSteps) {
		if(numberOfSteps.compareTo(BigInteger.valueOf(threshold)) < 0) {
			grid = context.getBean(MemoryInfiniteGrid.class);
		}
		else {
			grid = context.getBean(DatabaseInfiniteGrid.class);
		}
		return grid;
	}
}
