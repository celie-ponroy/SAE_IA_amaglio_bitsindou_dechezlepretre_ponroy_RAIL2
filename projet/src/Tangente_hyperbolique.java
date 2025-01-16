public class Tangente_hyperbolique implements TransferFunction{
    // fonction : σ(x) = tanh(x)
    @Override
    public double evaluate(double value) {
        return Math.tanh(value);
    }
    // dérivée : σ'(x) = 1 − σ^2(x)
    @Override
    public double evaluateDer(double value) {
        return 1 - Math.pow(Math.tanh(value), 2);
    }
}
