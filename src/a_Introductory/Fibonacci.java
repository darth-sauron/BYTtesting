package a_Introductory;

public class Fibonacci {
	public int fib(int n) {
		return switch (n) {
			case 0 -> 0;
			case 1 -> 1;
			default -> (fib(n - 1) + fib(n - 2)) + 1;
		};
	}
}
