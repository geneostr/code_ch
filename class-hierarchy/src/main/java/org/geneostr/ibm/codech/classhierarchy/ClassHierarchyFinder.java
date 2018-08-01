package org.geneostr.ibm.codech.classhierarchy;

public class ClassHierarchyFinder {

    static String testClassName1 = "javax.management.ListenerNotFoundException";
    static String testClassName2 = "sun.security.ssl.SSLServerSocketImpl";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Class Name argument wasn't found.");
        } else {
            try {
                final Class<?> clazz = Class.forName(args[0]);
                if (clazz.isInterface()) {
                    System.out.println(String.format("%1$s is Interface. Interface is not supported by this exercise", clazz.getName()));
                }
                printClassHierarchy(clazz);
            } catch (ClassNotFoundException cnf) {
                System.out.println(String.format("Class %1$s not found.", args[0]));
            }
        }
    }

    private static void printClassHierarchy(final Class<?> clazz) {
        int depth = 0;
        Class<?> runningClazz = clazz;
        while (runningClazz != null) {
            System.out.println(String.format("%1$s%2$s", charSeq(depth, '-'), runningClazz.getName()));
            runningClazz = runningClazz.getSuperclass();
            depth++;
        }
    }

    private static String charSeq(final int depth, final char rep) {
        final char[] seq = new char[depth];
        for (int i = 0; i < depth; i++) {
            seq[i] = rep;
        }
        return String.valueOf(seq);
    }
}
