package test;

import j7.tdd.mock.ArgumentCaptor;
import j7.tdd.mock.InOrder;
import j7.tdd.runner.MockRunner;
import j7.tdd.test.*;
import org.junit.runner.RunWith;
import prod.Collaborator;
import prod.UnderTest;

import static j7.tdd.mock.ArgumentMatcher.anyInt;
import static j7.tdd.mock.CallCount.atLeastOnce;
import static j7.tdd.mock.CallCount.times;
import static j7.tdd.mock.EffectfulStubBuilder.when;
import static j7.tdd.mock.EffectlessStubBuilder.doReturn;
import static j7.tdd.mock.MockFactory.*;
import static j7.tdd.mock.VerificationContextFactory.verify;

@RunWith(MockRunner.class)
public class MockTest {

    @Test
    @ConstructedMock(Collaborator.class)
    public void canSetReturn() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(1)).thenReturn(1);
        when(collaborator.getNumberFromSeed(2)).thenReturn(1);

        UnderTest underTest = new UnderTest(collaborator);
        int output = underTest.doTheThing(1,2);

        Assert.equal(output, 2);
    }

    @Test
    public void argumentMatchers() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt())).thenReturn(1);

        UnderTest underTest = new UnderTest(collaborator);

        int output = underTest.doTheThing(1,2);
        Assert.equal(output, 2);
    }

    @Test
    public void argumentCaptors() {
        ArgumentCaptor<Integer> a = new ArgumentCaptor<>(int.class);

        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(a.capture())).thenReturn(2);

        UnderTest underTest = new UnderTest(collaborator);
        int output = underTest.doTheThing(1,2);

        Assert.equal(output, 4);
        Assert.equal(a.get(0), 1);
        Assert.equal(a.get(1), 2);
    }

    @Test
    public void thenThrow() {
        String expMessage = "Hello";
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt())).thenThrow(new RuntimeException(expMessage));

        UnderTest underTest = new UnderTest(collaborator);

        try {
            underTest.doTheThing(1,2);
        }catch (RuntimeException e) {
            Assert.equal(e.getMessage(), expMessage);
            return;
        }

        throw new AssertionError("Exception not thrown");
    }

    @Test
    public void resetWorks() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt())).thenReturn(1);

        UnderTest underTest = new UnderTest(collaborator);
        int output = underTest.doTheThing(1,2);

        Assert.equal(output, 2);

        reset(collaborator);
        when(collaborator.getNumberFromSeed(anyInt())).thenReturn(2);

        output = underTest.doTheThing(1,2);
        Assert.equal(output, 4);
    }

    @Test
    public void simpleVerify() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt())).thenReturn(1);

        UnderTest underTest = new UnderTest(collaborator);

        int output = underTest.doTheThing(1,2);

        verify(collaborator, atLeastOnce()).getNumberFromSeed(1);
        Assert.equal(output, 2);
    }

    @Test
    public void  simpleSpy() {
        Collaborator collaboratorSpy = spy(new Collaborator());
        when(collaboratorSpy.getNumberFromSeed(1)).thenReturn(1);

        UnderTest underTest = new UnderTest(collaboratorSpy);

        int output = underTest.doTheThing(1,2);

        verify(collaboratorSpy, times(2)).sideEffect(anyInt());
        Assert.equal(output, 5);
    }

    @Test
    public void returnChaining() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt()))
                .thenReturn(1)
                .thenReturn(2);

        UnderTest underTest = new UnderTest(collaborator);

        int output = underTest.doTheThing(1,2);

        verify(collaborator, atLeastOnce()).getNumberFromSeed(1);
        verify(collaborator, atLeastOnce()).getNumberFromSeed(2);
        Assert.equal(output, 3);

    }


    public void  effectlessStubbing() {
        Collaborator collaboratorSpy = spy(new Collaborator());
        doReturn(1)
                .thenReturn(2)
                .when(collaboratorSpy).getNumberFromSeed(anyInt());

        UnderTest underTest = new UnderTest(collaboratorSpy);

        int output = underTest.doTheThing(1,2);

        verify(collaboratorSpy, times(2)).sideEffect(anyInt());
        Assert.equal(output, 3);
    }

    @Test
    public void orderedVerification() {
        Collaborator collaborator = mock(Collaborator.class);
        when(collaborator.getNumberFromSeed(anyInt()))
                .thenReturn(3)
                .thenReturn(4);

        UnderTest underTest = new UnderTest(collaborator);

        int output = underTest.doTheThing(1,2);
        InOrder ordered = new InOrder();

        ordered.verify(collaborator, atLeastOnce()).getNumberFromSeed(1);
        ordered.verify(collaborator, atLeastOnce()).getNumberFromSeed(2);
        ordered.verify(collaborator, times(1)).sideEffect(3);
        ordered.verify(collaborator, times(1)).sideEffect(4);
        Assert.equal(output, 7);
    }
}