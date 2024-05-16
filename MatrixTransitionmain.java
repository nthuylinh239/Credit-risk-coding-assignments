/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class MatrixTransitionmain {
    private static String[] rating = { "AAA", "AA", "A", "BBB", "BB", "B", "CCC", "D" };

    // Step 1: finding upperbound
    public static double[][] getUpperbounds(double[][] ratingMatrix) {
        double[][] culmulativeProbability = new double[ratingMatrix.length][ratingMatrix.length
                + 1];
        for (int i = 0; i < ratingMatrix.length; i++) {
            culmulativeProbability[i][0] = ratingMatrix[i][0];
        }
        for (int i = 0; i < ratingMatrix.length; i++) {
            for (int j = 1; j < ratingMatrix.length + 1; j++) {
                culmulativeProbability[i][j] = culmulativeProbability[i][j - 1]
                        + ratingMatrix[i][j];
            }
        }
        double[][] upperbounds = new double[ratingMatrix.length][ratingMatrix.length];
        for (int i = 0; i < ratingMatrix.length; i++) {
            for (int j = 0; j < ratingMatrix.length; j++) {
                upperbounds[i][j] = Gaussian.inverseCDF(1 - culmulativeProbability[i][j]);
            }
        }
        return upperbounds;
    }

    // Step 2
    public static double[] genRandomIndicator(int numofstock) {
        double[] result = new double[numofstock + 1];
        for (int i = 0; i < numofstock + 1; i++) {
            result[i] = Math.random();
            result[i] = Gaussian.inverseCDF(result[i]);
        }
        return result;
    }

    // Step 3: getX
    public static double getX(double rho, double y, double epsilon) {
        return Math.sqrt(rho) * y + Math.sqrt(1 - rho) * epsilon;
    }

    // Step 4.1: Transform old rating to RowIndexForUpperBounds or vice versa
    public static int[] getIndexFromRating(String[] oldRating) {
        int[] result = new int[oldRating.length];
        for (int i = 0; i < oldRating.length; i++) {
            switch (oldRating[i]) {
                case "AAA":
                    result[i] = 0;
                    break;
                case "AA":
                    result[i] = 1;
                    break;
                case "A":
                    result[i] = 2;
                    break;
                case "BBB":
                    result[i] = 3;
                    break;
                case "BB":
                    result[i] = 4;
                    break;
                case "B":
                    result[i] = 5;
                    break;
                case "CCC":
                    result[i] = 6;
                    break;
                default:
                    result[i] = 7;
            }
        }
        return result;
    }

    public static String[] getIndexFromRating(int[] oldRating) {
        String[] result = new String[oldRating.length];
        for (int i = 0; i < oldRating.length; i++) {
            switch (oldRating[i]) {
                case 0:
                    result[i] = "AAA";
                    break;
                case 1:
                    result[i] = "AA";
                    break;
                case 2:
                    result[i] = "A";
                    break;
                case 3:
                    result[i] = "BBB";
                    break;
                case 4:
                    result[i] = "BB";
                    break;
                case 5:
                    result[i] = "B";
                    break;
                case 6:
                    result[i] = "CCC";
                    break;
                default:
                    result[i] = "D";
            }
        }
        return result;
    }

    // Step 4.2
    public static String getNewRating(double[][] upperbounds, int rowIndex,
                                      double creditChangeIndicator) {
        for (int j = 0; j < upperbounds.length; j++) {
            if (creditChangeIndicator > upperbounds[rowIndex][j])
                return rating[j];
        }
        return rating[7];
    }

    // Step 5.1: Return an array of number of securities of each rating invested in original portfolio
    public static double[] calNumOfHoldingsinPF(double[] weights, String[] portfolioRating,
                                                double[] pricevalues, double totalInvestment) {
        if (weights.length != portfolioRating.length) {
            System.out.println("Weights does not match Portfolio Rating!");
        } // Catch error
        int[] portfolioPriceIndex = getIndexFromRating(
                portfolioRating); // get index to use with price array

        double[] numOfHoldingsinPF = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            int index = portfolioPriceIndex[i];
            numOfHoldingsinPF[i] = weights[i] * totalInvestment / pricevalues[index];
        }
        return numOfHoldingsinPF;
    }

    // Step 5.2: Calculate portfolio shocked value, assuming weights array has equal amt of components to rating arrays
    public static double calPortfolioVal(double[] numOfHoldings, String[] portfolioRating,
                                         double[] pricevalues) {
        if (numOfHoldings.length != portfolioRating.length) {
            System.out.println("Holdings array does not match Portfolio Rating array!");
        }
        int[] portfolioPriceIndex = getIndexFromRating(portfolioRating);
        double portfolioVal = 0;
        for (int i = 0; i < numOfHoldings.length; i++) {
            int index = portfolioPriceIndex[i];
            portfolioVal = portfolioVal + pricevalues[index] * numOfHoldings[i];
        }
        return portfolioVal;
    }

    // Putting all together
    public static void main(String[] args) {
        int numofstocks = 3;
        double initialInvestment = 1500.0;
        double[] baseValues = { 99.40, 98.39, 97.22, 92.79, 90.11, 86.60, 77.16 };
        double[] shockedValues = { 99.5, 98.51, 97.53, 92.77, 90.48, 88.25, 77.88, 60.00 };
        double[] weightsP1 = { 0.6, 0.3, 0.1 };
        String[] oldRatingP1 = { "AAA", "AA", "BBB" };
        double[] weightsP2 = { 0.60, 0.35, 0.05 };
        String[] oldRatingP2 = { "BB", "B", "CCC" };
        double[][] ratingMatrix = {
                { 0.91115, 0.08179, 0.00607, 0.00072, 0.00024, 0.00003, 0.00, 0.00 },
                { 0.00844, 0.89626, 0.08954, 0.00437, 0.00064, 0.00036, 0.00018, 0.00021 },
                { 0.00055, 0.02595, 0.91138, 0.05509, 0.00499, 0.00107, 0.00045, 0.00052 },
                { 0.00031, 0.00147, 0.04289, 0.90584, 0.03898, 0.00708, 0.00175, 0.00168 },
                { 0.00007, 0.00044, 0.00446, 0.06741, 0.83274, 0.07667, 0.00895, 0.00926 },
                { 0.00008, 0.00031, 0.0015, 0.0049, 0.05373, 0.82531, 0.07894, 0.03523 },
                { 0.00, 0.00015, 0.00023, 0.00091, 0.00388, 0.0763, 0.83035, 0.08818 }
        };

        double[][] upperbounds = getUpperbounds(ratingMatrix); // Step 1
        int[] indexOldRatingP1 = getIndexFromRating(oldRatingP1); // Step 4.1
        int[] indexOldRatingP2 = getIndexFromRating(oldRatingP2);
        // System.out.println(Arrays.toString(indexOldRatingP1)); // Test print
        double[] numOfHoldingsP1 = calNumOfHoldingsinPF(weightsP1, oldRatingP1, baseValues,
                                                        initialInvestment);
        double[] numOfHoldingsP2 = calNumOfHoldingsinPF(weightsP2, oldRatingP2, baseValues,
                                                        initialInvestment); // Step 5.1
        double rho = Double.parseDouble(args[0]);
        int numofsimulation = 100000;
        // MonteCarlo simulation

        double[] shockValP1
                = new double[numofsimulation]; // create array to store portfolio results
        double[] shockValP2
                = new double[numofsimulation];
        for (int n = 0; n < numofsimulation; n++) {
            double[] indicator = genRandomIndicator(numofstocks); // Step 2
            double[] x = new double[numofstocks];
            for (int i = 0; i < numofstocks; i++) {
                x[i] = getX(rho, indicator[0], indicator[i + 1]); // Step 3
            }
            // System.out.println(Arrays.toString(x)); // Test print
            String[] newRating = new String[numofstocks]; // Step 4.2
            for (int i = 0; i < numofstocks; i++) {
                newRating[i] = getNewRating(upperbounds, indexOldRatingP1[i], x[i]);
            }
            // System.out.println(Arrays.toString(newRatingP1));
            shockValP1[n] = calPortfolioVal(numOfHoldingsP1, newRating, shockedValues); // Step 5
            // System.out.println(shockValP1[n]); // test
        }
        for (int n = 0; n < numofsimulation; n++) {
            double[] indicator = genRandomIndicator(numofstocks); // Step 2
            double[] x = new double[numofstocks];
            for (int i = 0; i < numofstocks; i++) {
                x[i] = getX(rho, indicator[0], indicator[i + 1]); // Step 3
            }
            // System.out.println(Arrays.toString(x)); // Test print
            String[] newRating = new String[numofstocks]; // Step 4.2
            for (int i = 0; i < numofstocks; i++) {
                newRating[i] = getNewRating(upperbounds, indexOldRatingP2[i], x[i]);
            }
            // System.out.println(Arrays.toString(newRatingP2));
            shockValP2[n] = calPortfolioVal(numOfHoldingsP2, newRating, shockedValues); // Step 5
            // System.out.println(shockValP2[n]); // test
        }
        // Step 6: Calculate expected value by taking the average of simulated values
        double expectedValP1 = StdStats.mean(shockValP1);
        System.out.println(
                "The expected value of Portfolio 1 after " + numofsimulation + " simulations is: "
                        + expectedValP1);
        double expectedValP2 = StdStats.mean(shockValP2);
        System.out.println(
                "The expected value of Portfolio 2 after " + numofsimulation + " simulations is: "
                        + expectedValP2);

        // Step 7: find percentiles
    }
}
