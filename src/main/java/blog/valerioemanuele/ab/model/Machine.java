package blog.valerioemanuele.ab.model;

import static blog.valerioemanuele.ab.model.Direction.DOWN;
import static blog.valerioemanuele.ab.model.Direction.LEFT;
import static blog.valerioemanuele.ab.model.Direction.RIGHT;
import static blog.valerioemanuele.ab.model.Direction.UP;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Machine with two properties:
 * - a direction @see blog.valerioemanuele.ab.model.Direction
 * - a position in a two dimensional space (x,y coordinates see blog.valerioemanuele.ab.model.Point)
 * @author Valerio Emanuele
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Component
@RequestScope
public class Machine {
	private Direction direction;
	private Point position;
	private BigInteger unit;
	
	@PostConstruct   
    public void init(){
		direction = RIGHT;
		position = Point.of(BigInteger.ZERO,BigInteger.ZERO);
		unit = BigInteger.ONE;
    }

	public void clockwiseMove() {
		switch(direction) {
			case RIGHT: direction = DOWN; break;
			case DOWN: direction = LEFT; break;
			case LEFT: direction = UP; break;
			case UP: direction = RIGHT; break;
		}
	}

	public void counterClockwiseMove() {
		switch(direction) {
			case RIGHT: direction = UP; break;
			case UP: direction = LEFT; break;
			case LEFT: direction = DOWN; break;
			case DOWN: direction = RIGHT; break;
		}	
	}

	public void move() {
		switch(direction) {
			case RIGHT: position.setX(position.getX().add(unit)); break;
			case LEFT: position.setX(position.getX().add(unit.negate())); break;
			case UP: position.setY(position.getY().add(unit)); break;
			case DOWN: position.setY(position.getY().add(unit.negate())); break;
		}
	}
	
}