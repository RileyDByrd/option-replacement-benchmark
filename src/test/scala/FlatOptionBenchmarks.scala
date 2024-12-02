package byrd.riley.optionreplacementbenchmark

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import byrd.riley.optionreplacement.FlatOptionModule.*
import byrd.riley.optionreplacement.FlatOptionModule.FlatOption.FlatSome
import org.openjdk.jmh.infra.Blackhole

import scala.compiletime.uninitialized

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class FlatOptionBenchmarks:

  @Param(Array("1", "10", "100", "1000"))
  var size: Int = uninitialized

  var xs: Array[FlatOption[Int]] = uninitialized

  @Setup
  def setup(): Unit =
    xs = Array.ofDim[FlatOption[Int]](size)
    for c <- 0 until size do
      xs(c) = FlatSome(c)

  def flatMapWork(x: FlatOption[Int]): FlatOption[Int] = x.flatMap(num => FlatSome(num + 5))

  def mapWork(x: FlatOption[Int]): FlatOption[Int] = x.map(_ + 5)

  @Benchmark
  def measureFlatMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(flatMapWork(x))

  @Benchmark
  def measureMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(mapWork(x))