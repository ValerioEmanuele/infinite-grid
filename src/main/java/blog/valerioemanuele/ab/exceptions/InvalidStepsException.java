package blog.valerioemanuele.ab.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidStepsException extends RuntimeException{
	
	public InvalidStepsException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3204552855842631939L;

}
