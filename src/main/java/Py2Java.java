import java.util.*;

public class Py2Java {
    private static boolean check(Character[] arr, Character toCheckValue) {
        for (char element : arr) {
            if (element == toCheckValue) {
                return true;
            }
        }
        return false;
    }

    private static boolean check_str(String[] arr, String toCheckValue) {
        for (String element : arr) {
            if (element.equals(toCheckValue)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

//B1
        ArrayList<Character[]> T = new ArrayList<Character[]>();
        T.add(new Character[]{'A', 'C', 'D', 'F'});
        T.add(new Character[]{'A', 'B', 'C'});
        T.add(new Character[]{'C', 'D', 'F', 'G'});
        T.add(new Character[]{'A', 'B', 'C', 'D', 'G'});
        T.add(new Character[]{'A', 'B', 'C', 'D', 'F'});
        T.add(new Character[]{'B', 'C', 'E', 'F'});

        HashMap<Character, Double> I = new HashMap<Character, Double>();
        I.put('A', 0.5);
        I.put('B', 0.3);
        I.put('C', 0.7);
        I.put('D', 0.4);
        I.put('E', 0.8);
        I.put('F', 0.2);
        I.put('G', 0.9);
//B2
        ArrayList<Double> tw = new ArrayList<Double>();
        double TTW = 0;
        for (int i = 0; i < T.size(); i++) {
            double sum = 0;
            for (int j = 0; j < T.get(i).length; j++) {
                sum = sum + I.get(T.get(i)[j]);
            }
            tw.add(sum / (T.get(i).length));
            TTW = TTW + (sum / (T.get(i).length));
        }
        System.out.println("TTW= " + TTW);
        System.out.println("tw= " + tw.toString());
//B3
        TreeMap<Character, Double> ws = new TreeMap<Character, Double>();
        for (Character key : I.keySet()) {
            double s = 0;
            for (int i = 0; i < T.size(); i++) {
                if (check(T.get(i), key)) {
                    s = s + tw.get(i);
                }
            }
            s = s / TTW;
            ws.put(key, s);
        }
        System.out.println("ws= " + ws.toString());
// Generate Items
        TreeMap<String, Double> ITEM = new TreeMap<String, Double>();
        int n_items = 1000;
        for (int i = 0; i < n_items; i++) {
            double r = new Random().nextDouble();
            String key = "item" + i;
            ITEM.put(key, r);
        }
        System.out.println("ITEM= " + ITEM.toString());
//  Generate transactions
        int min_n_items = 1;
        int max_n_items = 30;
        int mean_n_items = 10;
        int n_transactions = 1000;
        Random rd = new Random();
        ArrayList<String[]> TRANS = new ArrayList<String[]>();
        for (int i = 0; i < n_transactions; i++) {
            double r = new Random().nextGaussian() * mean_n_items + mean_n_items;
            int n_items_gen = Math.abs((int) r);
            if (n_items_gen > max_n_items) {
                n_items_gen = max_n_items;
            }
            if (n_items_gen < min_n_items) {
                n_items_gen = min_n_items;
            }
            String[] arr = new String[n_items_gen];
            for (int j = 0; j < arr.length; j++) {
                arr[j] = "item" + rd.nextInt(n_transactions);
            }
            TRANS.add(arr);
        }
        StringBuilder listString = new StringBuilder();
        for (String[] s : TRANS) {
            listString.append(Arrays.toString(s)).append("\t");
        }
        System.out.println("TRANS=" + listString);
//  Prepare transaction weight (offline calculation)
        ArrayList<Double> TW_2 = new ArrayList<Double>();
        double TTW_2 = 0;
        for (int i = 0; i < TRANS.size(); i++) {
            double sum = 0;
            for (int j = 0; j < TRANS.get(i).length; j++) {
                sum = sum + ITEM.get(TRANS.get(i)[j]);
            }
            TW_2.add(sum / (TRANS.get(i).length));
            TTW_2 = TTW_2 + (sum / (TRANS.get(i).length));
        }
        System.out.println("TTW_2=" + TTW_2);
        System.out.println("len(TW_2)=" + TW_2.size());

//  Sliding window
        int window_size = 500;
        int n_window = TRANS.size() - window_size + 1;
        System.out.println(n_window);
        double threshold = 0.01;
        for (int i = 0; i < n_window; i++) {
            System.out.println("Process window " + i + " / " + n_window);
            TreeMap<String, Double> WS = new TreeMap<String, Double>();
            for (String key : ITEM.keySet()) {
                double s_2 = 0;
                for (int k = i; k < i + window_size - 1; k++) {
                    if (check_str(TRANS.get(k), key)) {
                        if (k + i >= 1000) {
                            s_2 = s_2 + TW_2.get(k + i - 1000);
                        } else s_2 = s_2 + TW_2.get(k + i);
                    }
                }
                s_2 = s_2 / TTW_2;
                WS.put(key, s_2);
            }
//  FWP of current window
            List<Double> FWP = new ArrayList<Double>();
            for (String key : ITEM.keySet()) {
                if (WS.get(key) >= threshold) {
                    System.out.println("WS[item]" + WS.get(key));
                    FWP.add(WS.get(key));
                }
            }
            System.out.println("len(FWP[{" + i + "}])=" + FWP.size());

        }

    }


}