package blog.valerioemanuele.ab.provider;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import blog.valerioemanuele.ab.model.Point;


public class RandomPointsProvider implements ArgumentsProvider{

	public static final BigInteger MIN_X = BigInteger.valueOf(-99);
	public static final BigInteger MIN_Y = BigInteger.valueOf(0);
	public static final BigInteger MAX_X = BigInteger.valueOf(100);
	public static final BigInteger MAX_Y = BigInteger.valueOf(32);
	

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		List<Point> listPoint = List.of(
				Point.builder().x(MIN_X).y(BigInteger.valueOf(1)).build(),
				Point.builder().x(BigInteger.valueOf(0)).y(MIN_Y).build(),
				Point.builder().x(BigInteger.valueOf(3)).y(BigInteger.valueOf(10)).build(),
				Point.builder().x(BigInteger.valueOf(4)).y(BigInteger.valueOf(13)).build(),
				Point.builder().x(BigInteger.valueOf(34)).y(BigInteger.valueOf(12)).build(),
				Point.builder().x(BigInteger.valueOf(33)).y(MAX_Y).build(),
				Point.builder().x(BigInteger.valueOf(99)).y(BigInteger.valueOf(15)).build(),
				Point.builder().x(BigInteger.valueOf(32)).y(BigInteger.valueOf(22)).build(),
				Point.builder().x(MAX_X).y(BigInteger.valueOf(11)).build()				
				);
		return Stream.of(Arguments.of(listPoint));
	}

}
