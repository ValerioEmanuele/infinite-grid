package blog.valerioemanuele.ab.service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.valerioemanuele.ab.model.InfiniteGrid;
import blog.valerioemanuele.ab.model.Point;
import blog.valerioemanuele.repository.InfiniteGridRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class DatabaseInfiniteGrid extends InfiniteGrid{
	
	private UUID executionId;
	
	@PostConstruct
	public void init() {
		executionId = UUID.randomUUID();
	}
	
	@Autowired
	private InfiniteGridRepository infiniteGridRepository;

	@Override
	public boolean put(Point point) {
		validateInput(point);
		log.debug("Adding point {}", point);
		return infiniteGridRepository.save(point) != null;
	}

	@Override
	public boolean remove(Point point) {
		validateInput(point);
		log.debug("Removing point {}", point);
		return infiniteGridRepository.deleteByXAndYAndExecutionId(point.getX(), point.getY(), point.getExecutionId()) > 0;
	}

	@Override
	public boolean contains(Point point) {
		validateInput(point);
		Point p = infiniteGridRepository.findByXAndYAndExecutionId(point.getX(), point.getY(), point.getExecutionId());
		return p != null;
	}

	@Override
	public BigInteger countBlackSquares() {
		return infiniteGridRepository.countByExecutionId(executionId());
	}
	
	@Override
	public String executionId() {
		return executionId.toString();
	}

	@Override
	public Optional<BigInteger> getMinXPointImpl() {
		return Optional.ofNullable(infiniteGridRepository.findMinXByExecutionId(executionId()));
	}

	@Override
	public Optional<BigInteger> getMaxXImpl() {
		return Optional.ofNullable(infiniteGridRepository.findMaxXByExecutionId(executionId()));
	}

	@Override
	protected Optional<BigInteger> getMinYImpl() {
		return Optional.ofNullable(infiniteGridRepository.findMinYByExecutionId(executionId()));
	}

	@Override
	protected Optional<BigInteger> getMaxYImpl() {
		return Optional.ofNullable(infiniteGridRepository.findMaxYByExecutionId(executionId()));
	}
}
