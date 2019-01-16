#define NT 1024

// Overall counter variables in global memory.

__device__ int z;
__device__ int m[3];




extern "C" __global__ void modCube (int c, int N){

	//Variable declarations
    int thr, size, rank;
    

    //Rank and size computations
    thr = threadIdx.x;
    size = gridDim.x*NT;
    rank = blockIdx.x*NT + thr;

    for (int x = rank; x < N; x += size){

        if (((((x*x)%N)*x)%N) == c){

        	m[z] = x;
        	atomicAdd (&z, 1);
    		
        }
        
    }			

    __syncthreads();


}