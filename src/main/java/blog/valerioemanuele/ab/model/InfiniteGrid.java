package blog.valerioemanuele.ab.model;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import blog.valerioemanuele.ab.exceptions.InvalidInputException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This abstract class is a representation of an infinite grid.
 * 
 * It contains the logic to write a graphical representation of the grid to a file
 * @author Valerio Emanuele
 *
 */
@Getter
@Slf4j
public abstract class InfiniteGrid {

	/**
	 * Graphical representation of an infite grid made of only white squares
	 */
	@Value("${infiniteWhites: }")
	private String INFINITY_WHITE_SQUARES_GRID;

	/**
	 * Graphical representation of the white square
	 */
	@Value("${whiteSquare:1}")
	private String WHITE_SQUARE;

	/**
	 * Graphical representation of the black square
	 */
	@Value("${blackSquare:0}")
	private String BLACK_SQUARE;

	/**
	 * The path of the folder that will contain the file with the grid representation
	 * The value will be set to the user.home directory it it's not set or set to a not
	 * valid directory
	 */
	@Value("${outputFilePath:}")
	private String outputFilePath;

	/**
	 * The name of the file that will contain the grid graphical representation
	 */
	@Value("${fileName:grid.txt}")
	private String fileName;

	@PostConstruct
	public void initGrid() {
		if (StringUtils.isBlank(outputFilePath) || !Files.isDirectory(Paths.get(outputFilePath))) {
			outputFilePath = System.getProperty("user.home");
		}
		
		log.info("The grid will be saved in the following directory {}", outputFilePath);
		log.info("The file containing the grid will be {}", fileName);
		log.info("The WHITE squares will be displayed with the following symbol {}", WHITE_SQUARE);
		log.info("The BLACK squares will be displayed with the following symbol {}", BLACK_SQUARE);
		log.info("An infinite grid of white squares will be represented as {}", INFINITY_WHITE_SQUARES_GRID);
		
	}

	public abstract boolean put(Point point);
	public abstract boolean remove(Point point);
	public abstract boolean contains(Point point);
	public abstract BigInteger countBlackSquares();

	protected abstract Optional<BigInteger> getMinXPointImpl();
	protected abstract Optional<BigInteger> getMaxXImpl();
	protected abstract Optional<BigInteger> getMinYImpl();
	protected abstract Optional<BigInteger> getMaxYImpl();

	public void validateInput(Point point) {
		if (point == null || !point.isSet()) {
			throw new InvalidInputException("The input square is not valid");
		}
	}

	public String executionId() {
		return "";
	}

	public final Optional<BigInteger> getMinXPoint() {
		if (noBlackSquares()) {
			return Optional.empty();
		}

		return getMinXPointImpl();
	}

	public Optional<BigInteger> getMaxX() {
		if (noBlackSquares()) {
			return Optional.empty();
		}

		return getMaxXImpl();
	}

	public Optional<BigInteger> getMinY() {
		if (noBlackSquares()) {
			return Optional.empty();
		}
		return getMinYImpl();
	}

	public Optional<BigInteger> getMaxY() {
		if (noBlackSquares()) {
			return Optional.empty();
		}

		return getMaxYImpl();
	}

	public boolean noBlackSquares() {
		return countBlackSquares().equals(BigInteger.ZERO);
	}

	public void gridToFile() throws IOException {
		File outputFile = FileUtils.getFile(filePath());
		
		if (noBlackSquares()) {
			FileUtils.writeStringToFile(outputFile, INFINITY_WHITE_SQUARES_GRID, Charset.forName("UTF-8"), false);
		}
		else {
			writeGridToFile(outputFile);
		}
		
	}

	/**
	 * Write to a file the graphical representation of the grid.
	 * It will represent only the shape that contains black squares
	 * 
	 * If only a black square is present, then it will write only 0, * and of course, all the infinity surrounding is made of white squares
	 * 
	 * If two black squares are present, it depends on their position on the plan, the final grid representation could be one of the following:
	 *  - 0 0
	 *  
	 *  - 0 1 0
	 *  
	 *  - 1 1 0
	 *    1 1 1
	 *    0 1 1
	 *  - etc.
	 *  
	 *  The principle is that, it will be represented the minimal shape that contains the black squares because the infinity surrounding is made of white squares
	 * @param outputFile
	 * @throws IOException
	 */
	private void writeGridToFile(File outputFile) throws IOException {
		// calcualate the 4 extreme points
		BigInteger minX = getMinXPoint().get();
		BigInteger maxX = getMaxX().get();
		BigInteger minY = getMinY().get();
		BigInteger maxY = getMaxY().get();

		FileUtils.writeStringToFile(outputFile, "", Charset.forName("UTF-8"), false);
		StringBuilder s = new StringBuilder();
		
		for (BigInteger y = maxY; y.compareTo(minY) >= 0; y = y.subtract(BigInteger.ONE)) {
			s.setLength(0);
			for (BigInteger x = minX; x.compareTo(maxX) <= 0; x = x.add(BigInteger.ONE)) {
				appendBWSquare(s, y, x);
			}
			FileUtils.writeStringToFile(outputFile, s.toString(), Charset.forName("UTF-8"), true);

			if (!isLastRow(minY, y)) {
				FileUtils.writeStringToFile(outputFile, System.lineSeparator(), Charset.forName("UTF-8"), true);
			}
		}
	}

	private boolean isLastRow(BigInteger minY, BigInteger y) {
		return y.compareTo(minY) == 0;
	}

	private void appendBWSquare(StringBuilder s, BigInteger y, BigInteger x) {
		if (contains(Point.of(x, y, executionId()))) {
			s.append(BLACK_SQUARE);
		} else {
			s.append(WHITE_SQUARE);
		}
	}
	
	public String filePath() {
		return outputFilePath+File.separator+fileName;
	}

}
