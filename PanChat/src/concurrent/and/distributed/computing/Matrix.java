package concurrent.and.distributed.computing;

import java.util.*;
public class Matrix {
    public static String write(int A[][]){
        StringBuffer s = new StringBuffer();
        for (int j = 0; j < A.length; j++)
            s.append(Util.writeArray(A[j]) + " ");
        return new String(s.toString());
    }
    public static void read(String s, int A[][]) {
        StringTokenizer st = new StringTokenizer(s);
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Integer.parseInt(st.nextToken());
    }
    public static void setZero(int A[][]) {
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = 0;
    }
    public static void setMax(int A[][], int B[][]) {
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Util.max(A[i][j], B[i][j]);
    }
}
