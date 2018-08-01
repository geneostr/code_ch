package org.geneostr.ibm.codech.genroot;

public class GenericRoot {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Number argument wasn't found.");
        } else {
            try {
                long genericRoot = calculateGenericRoot(Long.parseLong(args[0]));
                System.out.println("Generic Root of " + args[0] + " is " + genericRoot);
            } catch (NumberFormatException nf) {
                System.out.println("The argument is not number.");
            }
        }
    }

    private static long calculateGenericRoot(long number) {
        long num = Math.abs(number);
        long genRoot = 0;
        do {
            genRoot += Math.floorMod(num, 10);
            num = Math.floorDiv(num, 10);
        } while (num > 0);
        return genRoot;
    }

}
