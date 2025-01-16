import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        MLP multiPer = new MLP(new int[]{2, 1}, 0.03, new TanH());
        HashMap<double[], double[]> entrees = new HashMap<>();
        entrees.put(new double[]{0, 0}, new double[]{0});
        entrees.put(new double[]{0, 1}, new double[]{1});
        entrees.put(new double[]{1, 0}, new double[]{1});
        entrees.put(new double[]{1, 1}, new double[]{1});
        for(int i = 0; i < 10000; i++) {
            for (double[] e : entrees.keySet()) {
                multiPer.backPropagate(e, entrees.get(e));
            }
        }
        for(double[] e : entrees.keySet()){
            afficherTab(e);
            afficherTab(multiPer.execute(e));
            System.out.println("----------");
        }
    }

    public static void afficherTab(double[] tab){
        for(int i = 0; i < tab.length; i++){
            System.out.printf("%f\t", tab[i]);
        }
        System.out.println();
    }
}
