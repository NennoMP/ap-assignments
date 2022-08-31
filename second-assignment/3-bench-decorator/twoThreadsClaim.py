import functools
import json
import statistics
import threading
from time import perf_counter, sleep
from types import FunctionType

# FILENAMES
OUT_DIR = './outputs/'


def bench(n_threads=1, seq_iter=1, iter=1):
    """Parametric decorator for benchmarking threads execution.
    
    :param <fun>: function to benchmark
    :param <n_threads>: the number of threads
    :param <seq_iter>: the number of times <fun> must be invoked in each thread
    :param <iter>: the number of times the whole execution of <n_threads> is repetead
    """

    def bench_inner(fun: FunctionType) -> FunctionType:

        @functools.wraps(fun)
        def wrapper_bench(*args, **kwargs) -> dict:
            execution_times = [] # time of each execution

            def thread_action():
                """Target action for a thread, invokes function <fun> for    <seq_iter> times.
                """
                for _ in range(seq_iter):
                    fun(*args, **kwargs)


            for _ in range(iter):
                
                # Spawn <n_threads>
                threads = [threading.Thread(target=thread_action) 
                            for _ in range(n_threads)]

                start_time = perf_counter() # starting time

                for t in threads:
                    t.start()
                for t in threads:
                    t.join()

                end_time = perf_counter() # ending time
                execution_times.append(end_time - start_time)

            # Mean
            mean = statistics.mean(execution_times)
            # Variance (0 if less than two data points)
            variance = statistics.variance(execution_times)  if iter > 1 else 0

            return {
                'fun': fun.__name__,
                'args': args,
                'n_threads': n_threads,
                'seq_iter': seq_iter,
                'iter': iter,
                'mean': mean,
                'variance': variance,
            }

        return wrapper_bench

    return bench_inner

def test(iter: int, fun: FunctionType, *args):
    """Utility function for testing a function leveraging the <bench> decorator. Executes function <fun> on <args> with varying number of function invocations and degrees of parallelism:
    
        - 16 invocations, 1 thread
        - 8 invocations, 2 threads
        - 4 invocations, 4 threads
        - 2 invocations, 8 threads

    :param <iter>: number of iterations
    :param <fun>: function to execute
    :param <args>: arguments to provide to <fun>
    """

    # First configuration
    seq_iter, n_threads = 16, 1

    print(f'> Testing <{fun.__name__}> function')
    for _ in range(4):
        print(f'--- n_threads={n_threads}, seq_iter={seq_iter}, iter={iter}')
    
        bench_inner = bench(n_threads=n_threads, seq_iter=seq_iter, iter=iter)
        wrapper = bench_inner(fun)
        result = wrapper(*args)


        file = f'{OUT_DIR}{fun.__name__}_{args}_{n_threads}_{seq_iter}.json'
        with open(file, 'w') as outf:
            json.dump(result, outf, indent=4)

        seq_iter //= 2
        n_threads *= 2

def just_wait(n: int):
    """Testing function for the decorator, performs NOOP for n/10 seconds."""
    sleep(n * 0.1)

def grezzo(n: int):
    """Testing function for the decorator, CPU intensive."""
    for _ in range(2**n):
        pass


if __name__ == '__main__':
    # Variance should be 0 (only 1 data point)
    #test (1, grezzo, 5)

    #test(20, grezzo, 23)
    test(20, just_wait, 10)


"""
--- CONCLUSIONS:
    
    > CPU Intensive function <grezzo>
        From the results it is possible to notice that the increasing number of threads (i.e. parallelism degree) does not correspond to an increase in performance. In fact, the difference in the average (mean) execution time between the different configurations is minimal.
        The reason is that the CPython interpreter ensures that only one thread executes Python bytecode at a time, since a thread must be holding the GIL (Global Interpreter Lock) in order to use the CPU.
        Hence, the GIL, in the case of CPU-based tasks and multi-threadings, introduces a degradation on performances rather than improvements.

    > NOOP function <just_wait>:
        From the results of the second experiment, where a not CPU-based task is considered, the scenario is quite the opposite. The average (mean) execution time halves between the different configuration when the number of threads is doubled.
        The reason lies in the fact that the <just_wait> function does not require the use of the CPU (since it just waits).
"""
