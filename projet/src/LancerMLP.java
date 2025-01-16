import java.util.Arrays;

public class LancerMLP {
    public static void main(String[] args) {
        int nb_couche=3;
        int[] nb_neurone={2,2,1};
        double learning_rate=0.03;
        int[] couche = new int[nb_couche];

        for (int i = 0; i < nb_couche; i++) {
            couche[i] = nb_neurone[i];
        }
        MLP mlp = new MLP(couche,learning_rate,new Sigmoide());
        //ET, OU et XOR.
        double[][] inputs_xor = {{0,0},{0,1},{1,0},{1,1}};
        double[][] outputs_xor = {{0},{1},{1},{0}};//XOR

        double[][] inputs_et = {{0,0},{0,1},{1,0},{1,1}};
        double[][] outputs_et = {{0},{0},{0},{1}};//ET

        double[][] inputs_ou = {{0,0},{0,1},{1,0},{1,1}};
        double[][] outputs_ou = {{0},{1},{1},{1}};//OU

        for (int i = 0; i < 10000; i++) {
            int j = i%4;
            mlp.backPropagate(inputs_xor[j],outputs_xor[j]);
        }
        Arrays.stream(mlp.execute(inputs_xor[0])).forEach(System.out::println);
        Arrays.stream(mlp.execute(inputs_xor[1])).forEach(System.out::println);
        Arrays.stream(mlp.execute(inputs_xor[2])).forEach(System.out::println);
        Arrays.stream(mlp.execute(inputs_xor[3])).forEach(System.out::println);






    }
}
