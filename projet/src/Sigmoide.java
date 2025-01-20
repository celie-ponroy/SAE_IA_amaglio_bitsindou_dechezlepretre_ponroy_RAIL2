import java.io.Serializable;

public class Sigmoide implements TransferFunction, Serializable {
    @Override
    public double evaluate(double value) {
        return 1/(1+Math.exp(-value));
    }

    @Override
    public double evaluateDer(double value) {
        return this.evaluate(value)-this.evaluate(value)*this.evaluate(value);
    }
}
