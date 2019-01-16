import edu.rit.pj2.Task;
import edu.rit.gpu.Kernel;
import edu.rit.gpu.Gpu;
import edu.rit.gpu.GpuIntVbl;
import edu.rit.gpu.GpuIntArray;
import edu.rit.gpu.Module;
import edu.rit.gpu.CacheConfig;

/**
 * Class ModCubeRoot finds all the cube roots of c (mod n).
 * Usage: <TT>java pj2 ModCubeRoot <I>c</I> <I>n</I></TT>
 * <BR><TT><I>c</I></TT> = <c> is the number whose modular cube root(s) are to be found(Integer 0 <= c < n - 1)
 * <BR><TT><I>n</I></TT> = <n> is the modulus(Integer > 1)
 *
 * @author  Arjun Gupta
 * @version 15-Nov-2018
 */
public class ModCubeRoot extends Task {

    /**
     * Interface that relates the Java function to the kernel function
     */
    private static interface modKernel extends Kernel {
        public void modCube (int c, int N);
    }

    /**
     * function sets the parameters to the kernel function and calls the kernel
     *
     * @param args c and n values to compute c (mod n)
     * @throws Exception if input is invalid
     */
    public void main(String[] args) throws Exception {

        //validates the input and throws error if not correct.
        try{
            int c = Integer.parseInt(args[0]);
            int N = Integer.parseInt(args[1]);

            if ((c < 0) || (N < 2) || (c > (N - 2))) usage();

            // Initialize GPU.
            Gpu gpu = Gpu.gpu();

            //ensures compute capability is met
            gpu.ensureComputeCapability(2, 0);

            // Set up GPU counter variable.
            Module module = gpu.getModule("ModCubeRoot.ptx");
            GpuIntVbl count = module.getIntVbl("z");
            GpuIntArray m = module.getIntArray("m", 3);
    

            //transferring the variable to the GPU
            count.item = 0;
            count.hostToDev();

            for (int i = 0; i < 3; i++){
                m.item[i] = 0;
            }
            m.hostToDev();

            //sets the kernel and the class file which should compute the totient
            modKernel kernel = module.getKernel(modKernel.class);
            kernel.setBlockDim(1024);
            kernel.setGridDim(gpu.getMultiprocessorCount());

            //Priority is given to the shared memory as it requires only shared and no cache
            kernel.setCacheConfig (CacheConfig.CU_FUNC_CACHE_PREFER_SHARED);

            //calls kernel to compute
            kernel.modCube(c, N);

            // Print results
            count.devToHost();
            //p.devToHost();
            //q.devToHost();
            //r.devToHost();
            m.devToHost();

            if (count.item == 0){
                System.out.println("No cube roots of " + c +" (mod " + N + ")"  );
            }
            else{
                for(long i = 0; i < count.item; i++){
                    if(i == 0){
                        System.out.println(m.item[0]+"^3 = " + c + " (mod " + N +")");
                    }
                    else if(i == 1){
                        System.out.println(m.item[1]+"^3 = " + c + " (mod " + N +")");
                    }

                    else{
                        System.out.println(m.item[2]+"^3 = " + c + " (mod " + N +")");
                    }
                }
            }
        }
        catch(Exception e){
            usage();
        }    


    }
    
    /**
     * restricting the core to 1
     * @return 1 always
     */
    protected static int coresRequired() {
        return 1;
    }

    /**
     * restricting the GPU to 1
     * @return 1 always
     */
    protected static int gpusRequired() {
        return 1;
    }

    /**
     * Print a usage message and exit.
     */
    private static void usage(){
        
        System.err.println ("Usage: java pj2 ModCubeRoot <c> <n>");
        System.err.println ("<c> = <c> is the number whose modular cube root(s) are to be found(Integer 0 <= c < n - 1)");
        System.err.println ("<n> = <n> is the modulus(Integer > 1)");
        terminate(1);
    }
}