package jmbench.impl.memory;

import jmbench.impl.wrapper.VectorzBenchmarkMatrix;
import jmbench.interfaces.BenchmarkMatrix;
import jmbench.interfaces.MemoryFactory;
import jmbench.interfaces.MemoryProcessorInterface;
import mikera.matrixx.AMatrix;
import mikera.matrixx.Matrix;
import mikera.matrixx.algo.Multiplications;
import mikera.matrixx.decompose.ISVDResult;
import mikera.matrixx.solve.Linear;

public class VectorzMemoryFactory implements MemoryFactory {

    @Override
    public BenchmarkMatrix create(int numRows, int numCols) {
        return wrap(Matrix.create(numRows, numCols));
    }

    @Override
    public BenchmarkMatrix wrap(Object matrix) {
        return new VectorzBenchmarkMatrix((AMatrix) matrix);
    }

    @Override
    public MemoryProcessorInterface invertSymmPosDef() {
        return new InvSymmPosDef();
    }

    public static class InvSymmPosDef implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();

            AMatrix result = null;

            for( int i = 0; i < numTrials; i++ )
                result = A.inverse();
        }
    }

    @Override
    public MemoryProcessorInterface mult() {
        return new Mult();
    }

    public static class Mult implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();
            AMatrix B = inputs[1].getOriginal();
            AMatrix C = null;

            for( int i = 0; i < numTrials; i++ )
                C = Multiplications.multiply(A, B);
        }
    }

    @Override
    public MemoryProcessorInterface multTransB() {
        return new MultTransB();
    }

    public static class MultTransB implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();
            AMatrix B = inputs[1].getOriginal();
            AMatrix C = null;

            for( int i = 0; i < numTrials; i++ )
                C = Multiplications.multiply(A, B.getTranspose());
        }
    }

    @Override
    public MemoryProcessorInterface add() {
        return new Add();
    }

    public static class Add implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();
            AMatrix B = inputs[1].getOriginal();
            AMatrix C = null;

            for( int i = 0; i < numTrials; i++ )
                C = A.addCopy(B);
        }
    }

    @Override
    public MemoryProcessorInterface solveEq() {
        return new SolveLinear();
    }

    public static class SolveLinear implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();
            AMatrix y = inputs[1].getOriginal();
            AMatrix x = null;

            for( int i = 0; i < numTrials; i++ )
                x = Linear.solve(A, y);
        }
    }

    @Override
    public MemoryProcessorInterface solveLS() {
        return new SolveLS();
    }

    public static class SolveLS implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();
            AMatrix y = inputs[1].getOriginal();
            AMatrix x = null;

            for( int i = 0; i < numTrials; i++ )
                x = Linear.solveLeastSquares(A, y);
        }
    }

    @Override
    public MemoryProcessorInterface svd() {
        return new SVD();
    }

    public static class SVD implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
            AMatrix A = inputs[0].getOriginal();

            AMatrix U =null,V =null, S=null;
            for( int i = 0; i < numTrials; i++ ) {
                ISVDResult svd = mikera.matrixx.decompose.SVD.decompose(A);

                U = svd.getU();
                V = svd.getV();
                S = svd.getS();
            }
            if( U == null || S == null || V == null )
                throw new RuntimeException("There is a null");
        }
    }

    @Override
    public MemoryProcessorInterface eig() {
        return new Eig();
    }

    public static class Eig implements MemoryProcessorInterface
    {
        @Override
        public void process(BenchmarkMatrix[] inputs, BenchmarkMatrix[] outputs, long numTrials) {
//            DenseMatrix64F A = inputs[0].getOriginal();
//
//            EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(A.numRows,true);
//            DenseMatrix64F v[] =  new DenseMatrix64F[Math.min(A.numRows,A.numCols)];
//            for( int i = 0; i < numTrials; i++ ) {
//                if( !DecompositionFactory.decomposeSafe(eig,A) ) {
//                    throw new RuntimeException("SVD failed?!?");
//                }
//
//                for( int j = 0; j < v.length; j++ ) {
//                    v[j] = eig.getEigenVector(j);
//                }
//            }
//            if( v[0] == null )
//                throw new RuntimeException("There is a null");
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
    
}
