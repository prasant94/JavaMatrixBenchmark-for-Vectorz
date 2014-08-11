package jmbench.impl.runtime;

import org.ejml.data.DenseMatrix64F;
import jmbench.impl.wrapper.VectorzBenchmarkMatrix;
import jmbench.interfaces.AlgorithmInterface;
import jmbench.interfaces.BenchmarkMatrix;
import jmbench.interfaces.RuntimePerformanceFactory;
import jmbench.tools.runtime.generator.ScaleGenerator;
import mikera.matrixx.AMatrix;
import mikera.matrixx.Matrix;
import mikera.matrixx.algo.Multiplications;
import mikera.matrixx.decompose.Cholesky;
import mikera.matrixx.decompose.ILUPResult;
import mikera.matrixx.decompose.IQRResult;
import mikera.matrixx.decompose.ISVDResult;
import mikera.matrixx.decompose.LUP;
import mikera.matrixx.solve.Linear;

public class VectorzAlgorithmFactory implements RuntimePerformanceFactory {
    
    @Override
    public BenchmarkMatrix wrap(Object matrix) {
        return new VectorzBenchmarkMatrix((AMatrix)matrix);
    }

    @Override
    public BenchmarkMatrix create(int numRows, int numCols) {
        AMatrix A = Matrix.create(numRows,numCols);
        return wrap(A);
    }

    @Override
    public AlgorithmInterface chol() {
        return new Chol();
    }

    public static class Chol implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix L = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                L = Cholesky.decompose(matA).getL();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(L);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface lu() {
        return new LU();
    }

    public static class LU implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix L = Matrix.create();
            AMatrix U = Matrix.create();
            AMatrix P = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                
                ILUPResult lu = LUP.decompose(matA);

                L = lu.getL();
                U = lu.getU();
                P = lu.getP();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(L);
            outputs[1] = new VectorzBenchmarkMatrix(U);
            outputs[2] = new VectorzBenchmarkMatrix(P);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface svd() {
        return new SVD();
    }

    public static class SVD implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix U = null;
            AMatrix S = null;
            AMatrix V = null;

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                ISVDResult svd = mikera.matrixx.decompose.SVD.decompose(matA);
                U = svd.getU();
                S = svd.getS();
                V = svd.getV();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(U);
            outputs[1] = new VectorzBenchmarkMatrix(S);
            outputs[2] = new VectorzBenchmarkMatrix(V);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface eigSymm() {
        return new MyEig();
    }

    public static class MyEig implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
//            DenseMatrix64F matA = inputs[0].getOriginal();
//
//            EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(matA.numCols, true, true);
//
//            long prev = System.nanoTime();
//
//            for( long i = 0; i < numTrials; i++ ) {
//                if( !DecompositionFactory.decomposeSafe(eig,matA) )
//                    throw new DetectedException("Decomposition failed");
//                // this isn't necessary since eigenvalues and eigenvectors are always computed
//                eig.getEigenvalue(0);
//                eig.getEigenVector(0);
//            }
//
//            long elapsedTime = System.nanoTime() - prev;
//            outputs[0] = new EjmlBenchmarkMatrix(EigenOps.createMatrixD(eig));
//            outputs[1] = new EjmlBenchmarkMatrix(EigenOps.createMatrixV(eig));
//            return elapsedTime;
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    @Override
    public AlgorithmInterface qr() {
        return new QR();
    }

    public static class QR implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix Q = null;
            AMatrix R = null;

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                IQRResult qr = mikera.matrixx.decompose.QR.decompose(matA);

                Q = qr.getQ();
                R = qr.getR();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(Q);
            outputs[1] = new VectorzBenchmarkMatrix(R);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface det() {
        return new Det();
    }

    public static class Det implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                matA.determinant();
            }

            return System.nanoTime() - prev;
        }
    }

    @Override
    public AlgorithmInterface invert() {
        return new Inv();
    }

    public static class Inv implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = matA.inverse();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface invertSymmPosDef() {
        return new InvSymmPosDef();
    }

    public static class InvSymmPosDef implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = matA.inverse();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface add() {
        return new Add();
    }

    public static class Add implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();
            AMatrix matB = inputs[1].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = matA.addCopy(matB);
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface mult() {
        return new Mult();
    }

    public static class Mult implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();
            AMatrix matB = inputs[1].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = Multiplications.multiply(matA, matB);
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface multTransB() {
        return new MulTranB();
    }

    public static class MulTranB implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();
            AMatrix matB = inputs[1].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = Multiplications.multiply(matA, matB.getTransposeView());
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface scale() {
        return new Scale();
    }

    public static class Scale implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = Matrix.create(matA);
                result.scale(ScaleGenerator.SCALE);
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface solveExact() {
        return new SolveExact();
    }
    public static class SolveExact implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();
            AMatrix matB = inputs[1].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = Linear.solve(matA, matB);
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface solveOver() {
        return new SolveOver();
    }

    public static class SolveOver implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();
            AMatrix matB = inputs[1].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = Linear.solveLeastSquares(matA, matB);
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public AlgorithmInterface transpose() {
        return new Transpose();
    }

    public static class Transpose implements AlgorithmInterface {
        @Override
        public long process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix matA = inputs[0].getOriginal();

            AMatrix result = Matrix.create();

            long prev = System.nanoTime();

            for( long i = 0; i < numTrials; i++ ) {
                result = matA.getTranspose();
            }

            long elapsedTime = System.nanoTime() - prev;
            outputs[0] = new VectorzBenchmarkMatrix(result);
            return elapsedTime;
        }
    }

    @Override
    public BenchmarkMatrix convertToLib(DenseMatrix64F input) {
        return new VectorzBenchmarkMatrix(convertToVectorz(input));
    }

    @Override
    public DenseMatrix64F convertToEjml(BenchmarkMatrix input) {
        AMatrix orig = input.getOriginal();
        return vectorzToEjml(orig);
    }
    
    public static AMatrix convertToVectorz( DenseMatrix64F orig )
    {
        AMatrix ret = Matrix.create(orig.getNumRows(),orig.getNumCols());

        for( int i = 0; i < orig.numRows; i++ ) {
            for( int j = 0; j < orig.numCols; j++ ) {
                ret.set(i,j,orig.get(i,j)) ;
            }
        }

        return ret;
    }

    public static DenseMatrix64F vectorzToEjml( AMatrix orig )
    {
        if( orig == null )
            return null;

        DenseMatrix64F ret = new DenseMatrix64F(orig.rowCount(),orig.columnCount());

        for( int i = 0; i < ret.numRows; i++ ) {
            for( int j = 0; j < ret.numCols; j++ ) {
                ret.set(i,j,orig.get(i,j));
            }
        }

        return ret;
    }

}
