package service;


import java.security.SecureRandom;
import java.util.Random;

public class RandomSingleton {
    private static RandomSingleton instance;
    private SecureRandom rnd;

    private RandomSingleton() {
        rnd = new SecureRandom();
    }

    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public int nextInt(int n) {
        return rnd.nextInt(n);
    }
}

