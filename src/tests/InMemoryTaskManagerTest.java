package tests;

import manager.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest {

    InMemoryTaskManagerTest() {
        super(Managers.getDefault());
    }

}