package tests;

import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

}