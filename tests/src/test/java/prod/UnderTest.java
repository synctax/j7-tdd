package prod;

public class UnderTest {

    ICollaborator collaborator;

    public UnderTest(ICollaborator collaborator) {
        this.collaborator = collaborator;
    }

    public int doTheThing(int a, int b) {
       int newA = this.collaborator.getNumberFromSeed(a);
       int newB = this.collaborator.getNumberFromSeed(b);
       collaborator.sideEffect(newA);
       collaborator.sideEffect(newB);
       return newA + newB;
    }

}
