package context.core.util;

/**
 *
 * @author Aale
 */
public class DistanceUtil {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(editDistance("Hello World", "Hello     Mord"));
    }

    /**
     *
     * @param str1
     * @param str2
     * @return
     */
    public static double editDistance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        str1 = str1.toUpperCase();
        str2 = str2.toUpperCase();
        int mat[][] = new int[m + 1][n + 1];

        if (m == 0 || n == 0) {
            return Math.max(m, n);
        } else {
            for (int k = 0; k < m + 1; k++) {
                mat[k][0] = k;
            }
            for (int k = 0; k < n + 1; k++) {
                mat[0][k] = k;
            }

            for (int k = 1; k < m + 1; k++) {
                for (int l = 1; l < n + 1; l++) {
                    int cost = 0;
                    if (str1.charAt(k - 1) == str2.charAt(l - 1)) {
                        cost = 0;
                    } else {
                        cost = 1;
                    }
                    mat[k][l] = minimum(mat[k - 1][l] + 1, mat[k][l - 1] + 1, mat[k - 1][l - 1] + cost);
                    if (k > 1 && l > 1 && str1.charAt(k - 1) == str2.charAt(l - 2) && str1.charAt(k - 2) == str2.charAt(l - 1)) {
                        mat[k][l] = Math.min(mat[k][l], mat[k - 2][l - 2] + cost);
                    }
                }

            }
            return mat[m][n];
        }
    }

    private static int minimum(int i, int j, int k) {
        return Math.min(i, Math.min(j, k));
    }
}
