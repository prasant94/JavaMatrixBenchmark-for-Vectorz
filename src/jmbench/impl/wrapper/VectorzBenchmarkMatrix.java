package jmbench.impl.wrapper;

import jmbench.interfaces.BenchmarkMatrix;
import mikera.matrixx.AMatrix;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;


/**
 * @author Peter Abeles
 */
public class VectorzBenchmarkMatrix implements BenchmarkMatrix {

    AMatrix mat;

    public VectorzBenchmarkMatrix(AMatrix mat) {
        this.mat = mat;
    }

    @Override
    public double get(int row, int col) {
        return mat.get(row,col);
    }

    @Override
    public void set(int row, int col, double value) {
        mat.set(row,col,value);
    }

    @Override
    public int numRows() {
        return mat.rowCount();
    }

    @Override
    public int numCols() {
        return mat.columnCount();
    }

    @Override
    public Object getOriginal() {
        return mat;
    }
}
