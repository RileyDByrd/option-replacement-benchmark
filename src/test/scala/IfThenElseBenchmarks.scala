package byrd.riley.optionreplacementbenchmark

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.CanEqual.derived
import scala.compiletime.uninitialized

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class IfThenElseBenchmarks:
  given nullCanEqualNullable[A]: CanEqual[Null, A | Null] = derived
  given nullableCanEqualNull[A]: CanEqual[A | Null, Null] = derived

  @Param(Array("1", "10", "100", "1000"))
  var size: Int = uninitialized

  var xs: Array[Int | Null] = uninitialized

  @Setup
  def setup(): Unit =
    xs = new Array[Int | Null](size)
    for c <- 0 until size do
      xs(c) = c

  def flatMapWork(x: Int | Null): Int | Null =
    if x == null
    then null
    else x + 5

  val nullable5: Int | Null = 5

  def mapWork(x: Int | Null): Int | Null =
    if x == null
    then null
    else x + nullable5.nn

  @Benchmark
  def measureFlatMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(flatMapWork(x))

  @Benchmark
  def measureMap(bh: Blackhole): Unit =
    for x <- xs do
      bh.consume(mapWork(x))