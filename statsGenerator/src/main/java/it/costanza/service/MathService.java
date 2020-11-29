package it.costanza.service;

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

        double sum = 0;
        double newSum = 0;


        for (int j = 0; j < numbers.length; j++) {
            // put the calculation right in there
            newSum = newSum + ((numbers[j] - media) * (numbers[j] - media));
        }
        double squaredDiffMean = (newSum) / (numbers.length);
        double standardDev = (Math.sqrt(squaredDiffMean));
        return standardDev;
    }

    public static double getDeviazioneStandard(double[] numbers, double media) {
        double sd = 0;
        for (int i = 0; i < numbers.length; i++) {
            sd = sd + Math.pow(numbers[i] - media, 2);
        }
        return sd;
    }


    //method to get maximum number from array elements
    public static int getMax(int[]inputArray)
    {
        int maxValue=inputArray[0];

        for(int i=1;i<inputArray.length;i++)
        {
            if(inputArray[i]>maxValue)
            {
                maxValue=inputArray[i];
            }
        }
        return maxValue;
    }

    //method to get minimum number from array elements
    public static int getMin(int[]inputArray)
    {
        int minValue=inputArray[0];

        for(int i=1;i<inputArray.length;i++)
        {
            if(inputArray[i]<minValue)
            {
                minValue=inputArray[i];
            }
        }
        return minValue;
    }


}
