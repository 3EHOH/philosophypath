package PhilosophyPath;

class CircularPathException extends Exception {
    CircularPathException() {
        System.out.printf("Circular links");
    }
}