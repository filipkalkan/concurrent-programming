import java.math.BigInteger;

import rsa.Factorizer;
import rsa.ProgressTracker;

public class CodeBreakingExample {

    public static void main(String[] args) throws InterruptedException {

        // 'N' from a public key, and an encrypted message.
        BigInteger n = new BigInteger("601759919632188283");
        String code = "345677334782888273.345695849885304069."
                + "298909734370926550.331436443399066079.520810540851135142."
                + "217737308300793769.154174681324732404.594006511430227966."
                + "262063918071765759.217094422218462430.38478330443451968."
                + "17338473467540562.262063918071765759.174081772829234122."
                + "298909734370926550.483390103890685031.276722294109997828."
                + "161853810288187637.276722294109997828.347408990306588026."
                + "298909734370926550.81192449829613396.132648165883269346.";

        ProgressTracker tracker = new Tracker();
        String plaintext = Factorizer.crack(code, n, tracker);

        System.out.println("Decryption complete. The message is \"" + plaintext + "\"");
    }

    // -----------------------------------------------------------------------

    /** ProgressTracker: reports how far factorization has progressed */ 
    private static class Tracker implements ProgressTracker {
        private int totalProgress = 0;
        private int prevPercent = -1;

        /**
         * Called by Factorizer to indicate progress. The total sum of
         * ppmDelta from all calls will add upp to 1000000 (one million).
         * 
         * @param  ppmDelta   portion of work done since last call,
         *                    measured in ppm (parts per million)
         */
        @Override
        public void onProgress(int ppmDelta) {
            totalProgress += ppmDelta;
            int percent = totalProgress / 10000;
            if (percent != prevPercent) {
                System.out.println(percent + "%");
                prevPercent = percent;
            }
        }
    }
}
