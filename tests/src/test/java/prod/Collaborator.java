package prod;

public class Collaborator implements ICollaborator {
    public int getNumberFromSeed(int a) {
        return a*2;
    }
    public void sideEffect(int a) {
        System.out.println("a");
    }
}
