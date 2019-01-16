# Parallel-Computing-Breaking-RSA

 Here is a simplified description of the RSA public key encryption algorithm. The public encryption key consists of two integers (e, n), where e is an exponent and n is a modulus. For this project, e = 3 and n is either a prime or the product of two primes. To encrypt a plaintext message m, where m is an integer in the range 0 through n−1, compute the following formula yielding the ciphertext c, where c is also an integer in the range 0 through n−1:

                                                        c = m^3 (mod n)

To decrypt the ciphertext, normally you have to know the private decryption key that corresponds to the public encryption key. However, you can also decrypt the ciphertext without knowing the private key by computing a modular cube root:

                                                         m = c^1/3 (mod n)

If n is a large integer, e.g. a 2048-bit integer, no one knows an efficient algorithm to compute a modular cube root. But if n is small, it becomes possible to compute the modular cube root by brute force search: Given a ciphertext c, compute m^3 (mod n) for every m, 0 ≤ m ≤ n−1, until the answer equals c.

Depending on the particular values of c and n, c might have anywhere from zero to three modular cube roots—that is, zero to three values of m such that c = m^3 (mod n).

You will write a program to compute modular cube root(s) in parallel on the GPU using a brute force search.

Design Hints

   When computing the formula m^3 (mod n), use 64-bit integers (type long in Java). If you use 32-bit integers (type int in Java), the calculation might overflow and yield the wrong result.

   When computing the formula m^3 (mod n), do the (mod n) after every operation. If you do the (mod n) just once at the end, the calculation might overflow and yield the wrong result.

   Study the CUDA documentation on atomic functions and consider which of these you might be able to use. 
   
### Program Input and Output

The program's command line arguments are the ciphertext c and the modulus n.

The program finds all the cube roots of c (mod n).

If there are no cube roots, the program prints one line as shown in the examples below. If there are one or more cube roots, the program prints one line for each cube root as shown in the examples below; the cube roots are printed in ascending order.

```
$ java pj2 ModCubeRoot 2 1033
No cube roots of 2 (mod 1033)
$ java pj2 ModCubeRoot 2 1031
798^3 = 2 (mod 1031)
$ java pj2 ModCubeRoot 6 1033
56^3 = 6 (mod 1033)
387^3 = 6 (mod 1033)
590^3 = 6 (mod 1033)
$ java pj2 ModCubeRoot 43781972 124822021
142857^3 = 43781972 (mod 124822021)
$ java pj2 ModCubeRoot 46054145 124822069
142857^3 = 46054145 (mod 124822069)
27549958^3 = 46054145 (mod 124822069)
97129254^3 = 46054145 (mod 124822069)
```
 Your Java main program must be in a file named ModCubeRoot.java. Your C/CUDA kernel must be in a file named ModCubeRoot.cu. To compile and run your program:

   Log into the kraken computer.

   Set the CLASSPATH, PATH, and LD_LIBRARY_PATH variables as described here.

   Compile the Java main program using this command:

        $ javac ModCubeRoot.java

   Compile the CUDA kernel using this command:

        $ nvcc -ptx --compiler-bindir=/usr/bin/gcc-4.8 -arch compute_20 -o ModCubeRoot.ptx ModCubeRoot.cu

   Run the program using this command (substituting the proper command line arguments):

        $ java pj2 ModCubeRoot <c> <n>

Note: The compiled CUDA module is stored in a .ptx file, not a .cubin file like the examples in the textbook. 

Software Requirements

   The program must be run by typing this command line:

        java pj2 ModCubeRoot <c> <n>

        <c> is the number whose modular cube root(s) are to be found; it must be a decimal integer (type int) in the range 0 ≤ c ≤ n−1.
        <n> is the modulus; it must be a decimal integer (type int) ≥ 2. 

   Note: This means that the program's class must be named ModCubeRoot, this class must not be in a package, and this class must extend class edu.rit.pj2.Task.

   Note: You can assume that n is either a prime or the product of two primes. You do not have to verify this.

   If the command line does not have the required number of arguments, if any argument is erroneous, or if any other error occurs, the program must print an error message on the console and must exit. The error message must describe the problem. The wording of the error message is up to you.

   If there are no cube roots of c (mod n), the program must print the following line on the console:

    No cube roots of <c> (mod <p>)

   where <c> is replaced with the value of c and <p> is replaced with the value of p. There must be no spaces at the beginning or end of the line. The line must be terminated with a newline.

   If there are one or more cube roots of c (mod n), the program must print the following line on the console for each cube root m:

    <m>^3 = <c> (mod <p>)

   where <m> is replaced with the value of m, <c> is replaced with the value of c, and <p> is replaced with the value of p. There must be no spaces at the beginning or end of the line. The line must be terminated with a newline. The lines must be printed in ascending order of m. 
