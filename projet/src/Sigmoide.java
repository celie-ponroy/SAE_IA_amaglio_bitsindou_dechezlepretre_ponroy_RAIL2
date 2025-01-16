public class Sigmoide implements TransferFunction{
    //fonction : σ(x) = 1 / (1+e(−x))
    @Override
    public double evaluate(double value) {
        return 1/(1+Math.exp(-value));
    }
    //dérivée : σ'(x) = σ(x)−σ2(x))
    @Override
    public double evaluateDer(double value) {
        var eval = evaluate(value);
        return eval-Math.pow(eval,2);
    }
}
