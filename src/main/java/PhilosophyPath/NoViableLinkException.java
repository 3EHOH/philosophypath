package PhilosophyPath;

class NoViableLinkException extends Exception {
    NoViableLinkException() {
        System.out.printf("No viable link founds.");
    }
}
