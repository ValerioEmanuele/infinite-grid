package blog.valerioemanuele.ab.provider;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import blog.valerioemanuele.ab.model.Point;


public class FourPointsProvider implements ArgumentsProvider{
	

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		List<Point> listPoint = List.of(
				Point.builder().x(BigInteger.valueOf(-2)).y(BigInteger.valueOf(2)).build(),
				Point.builder().x(BigInteger.valueOf(2)).y(BigInteger.valueOf(2)).build(),
				Point.builder().x(BigInteger.valueOf(2)).y(BigInteger.valueOf(-2)).build(),
				Point.builder().x(BigInteger.valueOf(-2)).y(BigInteger.valueOf(-2)).build()				
				);
		return Stream.of(Arguments.of(listPoint));
	}

}
