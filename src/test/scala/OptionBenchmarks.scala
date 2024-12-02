package byrd.riley.optionreplacementbenchmark

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.compiletime.uninitialized

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class OptionBenchmarks:

  @Param(Array("1", "10", "100", "1000"))
  var size: Int = uninitialized

  var xs: Array[Option[Int]] = uninitialized

  @Setup
  def setup(): Unit =
    xs = Array.ofDim[Option[Int]](size)
    for c <- 0 until size do
      xs(c) = Some(c)

  def flatMapWork(x: Option[Int]): Option[Int] = x.flatMap(num => Some(num + 5))

  def mapWork(x: Option[Int]): Option[Int] = x.map(_ + 5)

  @Benchmark
  def measureFlatMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(flatMapWork(x))

  @Benchmark
  def measureMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(mapWork(x))