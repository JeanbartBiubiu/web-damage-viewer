package xyz.game.wasm;

import org.teavm.interop.Export;

import java.util.BitSet;

public class TestPower {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long result = fibonacci(45);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("质数数量：" + result);
        System.out.println("执行时间：" + duration + " 毫秒");
    }

    @Export(name = "testPower")
    public static long testPower(int limit) {
        BitSet isPrime = new BitSet(limit + 1);
        // 初始化所有数为质数
        for (int i = 2; i <= limit; i++) {
            isPrime.set(i);
        }

        // 直线筛算法
        for (int i = 2; i * i <= limit; i++) {
            if (isPrime.get(i)) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime.clear(j);
                }
            }
        }

        // 统计质数的数量
        int primeCount = 0;
        for (int i = 2; i <= limit; i++) {
            if (isPrime.get(i)) {
                primeCount++;
            }
        }
        return primeCount;
    }

    @Export(name = "fibonacci")
    // 斐波那契数列
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}