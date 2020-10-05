package service;

public class MathService {


    public static double getMedia(int[] numbers) {

        double total = 0;

        for (int i = 0; i < numbers.length; i++) {
            total = total + numbers[i];
        }

        /* arr.length returns the number of elements
         * present in the array
         */
        double average = total / numbers.length;
        return average;
    }


    public static double getMedia(double[] numbers) {

        double total = 0;

        for (int i = 0; i < numbers.length; i++) {
            total = total + numbers[i];
        }

        /* arr.length returns the number of elements
         * present in the array
         */
        double average = total / numbers.length;
        return average;
    }


    public static double getDeviazioneStandard(int[] numbers, double media) {
        double sd = 0;
        for (int i = 0; i < numbers.length; i++) {
            sd = sd + Math.pow(numbers[i] - media, 2);
        }
        return sd;
    }

    public static double getDeviazioneStandard(double[] numbers, double media) {
        double sd = 0;
        for (int i = 0; i < numbers.length; i++) {
            sd = sd + Math.pow(numbers[i] - media, 2);
        }
        return sd;
    }


}
