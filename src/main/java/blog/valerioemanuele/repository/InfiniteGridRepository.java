package blog.valerioemanuele.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import blog.valerioemanuele.ab.model.Point;

@Repository
public interface InfiniteGridRepository extends JpaRepository<Point, Long> {

	Point findByXAndYAndExecutionId(BigInteger x, BigInteger y, String executionId);
	
	BigInteger countByExecutionId(String executionId);
	
	long deleteByXAndYAndExecutionId(BigInteger x, BigInteger y, String executionId);
	
	@Query(value = "SELECT DISTINCT p.x from Point p WHERE p.executionId=:executionId AND p.x = (SELECT MIN(p1.x) from Point p1 WHERE p1.executionId=:executionId)")
	BigInteger findMinXByExecutionId(@Param("executionId") String executionId);

	@Query(value = "SELECT DISTINCT p.x from Point p WHERE p.executionId=:executionId AND p.x = (SELECT MAX(p1.x) from Point p1 WHERE p1.executionId=:executionId)")
	BigInteger findMaxXByExecutionId(@Param("executionId") String executionId);

	@Query(value = "SELECT DISTINCT p.y from Point p WHERE p.executionId=:executionId AND p.y = (SELECT MIN(p1.y) from Point p1 WHERE p1.executionId=:executionId)")
	BigInteger findMinYByExecutionId(@Param("executionId") String executionId);

	@Query(value = "SELECT DISTINCT p.y from Point p WHERE p.executionId=:executionId AND p.y = (SELECT MAX(p1.y) from Point p1 WHERE p1.executionId=:executionId)")
	BigInteger findMaxYByExecutionId(@Param("executionId") String executionId);
}
