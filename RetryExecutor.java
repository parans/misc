package misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RetryExecutor<T> {
	int retries;
	Callable<T> callable;
	List<Class<?>> retryableExceptions;
	Jitter jitter;
	long backOffdelay;

	T execute() throws Exception {
		T val = null;
		boolean retriableException = false;
		for (;retries > 0; retries--) {
			try {
				val = callable.call();
				break;
			} catch (Exception e) {
				final Class<?> c = e.getClass();
				retriableException = retryableExceptions.stream().anyMatch(re -> {
					return c.equals(re);
				});
				
				if (!retriableException || retries == 1) {
					throw e;
				}
			}
			jitter.backOff();
		}
		return val;
	}
	
	public static void main(String[] args) {
		RetryExecutor<String> exec = new RetryExecutor<>();
		boolean exception = true;
		exec.retries = 3;
		exec.callable = () -> {
			System.out.println("Starting the callable now");
			if (exception) {
				throw new FileNotFoundException("Error doing input and output");
			}
			return null;
		};
		exec.retryableExceptions = new ArrayList<>();
		exec.retryableExceptions.add(IOException.class);
		//exec.retryableExceptions.add(FileNotFoundException.class);
		
		exec.backOffdelay = 5000;
		exec.jitter = () -> {
			try {
				Thread.sleep(exec.backOffdelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		try {
			exec.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

