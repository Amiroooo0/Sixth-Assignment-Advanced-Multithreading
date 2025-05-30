package MonteCarloPI;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MonteCarloPi {

    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws InterruptedException {
        // Without Threads
        System.out.println("Single threaded calculation started: ");
        long startTime = System.nanoTime();
        double piWithoutThreads = estimatePiWithoutThreads(NUM_POINTS);
        long endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (single thread): " + piWithoutThreads);
        System.out.println("Time taken (single threads): " + (endTime - startTime) / 1_000_000 + " ms");

        // With Threads
        System.out.printf("Multi threaded calculation started: (your device has %d logical threads)\n",NUM_THREADS);
        startTime = System.nanoTime();
        double piWithThreads = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
        endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (Multi-threaded): " + piWithThreads);
        System.out.println("Time taken (Multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms");
    }

    // Monte Carlo Pi Approximation without threads
    public static double estimatePiWithoutThreads(long numPoints)
    {
        long internalPoints = 0 ;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for ( long i = 0 ; i < numPoints ; i++ ){
            double x = random.nextDouble(-1.0, 1.0);
            double y = random.nextDouble(-1.0, 1.0);
            if ( x*x + y*y <= 1 )
                internalPoints++ ;
        }
        return (4.0*internalPoints)/numPoints;
    }

    // Monte Carlo Pi Approximation with threads
    public static double estimatePiWithThreads(long numPoints, int numThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger internalPoints = new AtomicInteger(0);

        for ( int i = 0 ; i < numThreads ; i++ ){
            Runnable task = () -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                for ( long j = 0 ; j < numPoints/numThreads ; j++ ){
                    double x = random.nextDouble(-1.0, 1.0);
                    double y = random.nextDouble(-1.0, 1.0);
                    if ( x*x + y*y <= 1 )
                        internalPoints.incrementAndGet();
                }
            };
            executor.submit(task);
        }
        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
            System.err.println("Tasks did not finish in time!");
        }

        return 4.0*internalPoints.doubleValue()/numPoints;
    }
}