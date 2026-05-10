## j7-tdd
#### A testing and mocking framework in java 7

## Features
- [x] **basic mocking**
  - [x] mock creation
      ```java
      Collaborator collaborator = mock(Collaborator.class);
      UnderTest actual = new UnderTest(collaborator);
      
      // Uses mock collaborator
      actual.doThingWithCollaborator();
    ```
  - [x] method stubbing
      ```java
      Collaborator collaborator = mock(Collaborator.class);
      when(collaborator.collaborate(anyInt())).thenReturn(1);
      
      UnderTest actual = new UnderTest(collaborator);
      
      // Call to collaborator will return stubbed result
      actual.doThingWithCollaborator();
      ```
  - [x] ordered stubbing
    ```java
    Collaborator collaborator = mock(Collaborator.class);
    when(collaborator.collaborate(anyInt()))
        .thenReturn(1)
        .thenReturn(2);

    UnderTest actual = new UnderTest(collaborator);

    // Call to collab returns 1
    actual.doThingWithCollaborator();
    // Call to collab returns 2
    actual.doThingWithCollaborator();
    ```
  - [x] call verification
     ```java
    Collaborator collaborator = mock(Collaborator.class);
    when(collaborator.collaborate(anyInt()));

    UnderTest actual = new UnderTest(collaborator);

    actual.doThingWithCollaborator();
    
    // Throws if this call did not happen
    verify(collaborator, atLeastOnce()).collaborate(eq(1));
    ```
  - [x] ordered verification
     ```java
    Collaborator collaborator = mock(Collaborator.class);
    when(collaborator.collaborate(anyInt()));

    UnderTest actual = new UnderTest(collaborator);

    actual.doThingWithCollaborator();
    actual.doThingWithCollaborator();
    
    InOrder order = new InOrder();

    // Throws if calls did not happen in order
    order.verify(collaborator, times(1)).collaborate(eq(1));
    order.verify(collaborator, times(1)).collaborate(eq(1));
    ```
  - [x] argument captors
     ```java
     ArgumentCaptor<Integer> a = new ArgumentCaptor<>(int.class);
     
     Collaborator collaborator = mock(Collaborator.class);
     when(collaborator.collaborate(a.capture())).thenReturn(2);

     UnderTest actual = new UnderTest(collaborator);
     underTest.doThingWithCollaborator(1);
     
     //capture records passed arguments
     Assert.equal(a.get(0), 1);
     ```
  - [x] spies
     ```java
     Collaborator collaborator = new Collaborator();
     Collaborator collaboratorSpy = spy(collaborator);

     UnderTest actual = new UnderTest(collaboratorSpy);
     underTest.doThingWithCollaborator(1);
     
     //spy tracks invocations. unstubbed calls hit concrete
     verify(collaboratorSpy, times(1)).collaborate(1);
     ``` 
  - [x] effectless stubbing
     ```java
     Collaborator collaborator = new Collaborator();
     Collaborator collaboratorSpy = spy(collaborator);
    
     doReturn(1)
        .thenReturn(2)
        .when(collaboratorSpy).collaborate(anyInt());

     UnderTest actual = new UnderTest(collaboratorSpy);
     underTest.doThingWithCollaborator(1);
     
     // collaborate method calls are stubbed
     verify(collaboratorSpy, times(1)).collaborate(1);
     ``` 
- [ ] inline mocking
  - [ ] constructed mocks
     ```java
     @Test
     @InlineTarget(UnderTest.class)
     public void test(
       @ConstructionMock(Collaborator.class) ConstructionMock<Collaborator> collaborators 
     ){
       collaborators.useInit(new InitMock<Collaborator>() {{
         public void init(Collaborator mock) {
            when(mock.collaborate(anyInt())).thenReturn(1);
         }
       }});

       // Collaborator is constructed inline in UnderTest()
       UnderTest actual = new UnderTest();
       underTest.doThingWithCollaborator(1);
       
       // assert invocations on constructed mocks
       verify(collaborators.get(0), times(1)).collaborate(1);
     }
     ```  
  - [ ] static mocks
    ```java
     @Test
     @InlineTarget(UnderTest.class)
     public void test(
       // non-static interface `CollaboratorStatics` generated at compile time
       @StaticMock(Collaborator.class) CollaboratorStatics staticCollaborator 
     ){
       when(staticCollaborator.staticCollaborate(anyInt())).thenReturn(1);
    
       UnderTest actual = new UnderTest();
       underTest.doThingWithStaticCollaborator(1);
       
       // assert invocations on constructed mocks
       verify(staticCollaborator, times(1)).collaborate(1);
     } 
    ```

More features and usage examples can be found in the tests