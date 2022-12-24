package tests;

import http.KVServer;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;

class HTTPTaskManagerTest extends TaskManagerTest {

    KVServer kvServer = new KVServer();

    HTTPTaskManagerTest() throws IOException, InterruptedException {
        super(null);
    }

    @BeforeEach
    public void startServerKV() throws IOException, InterruptedException{
        kvServer.start();
        sleep(1000);
        taskManager = Managers.getDefault("http://localhost:" + KVServer.PORT);
        sleep(1000);
    }

    @AfterEach
    public void stopServerKV() throws InterruptedException {
        kvServer.stop();
        sleep(1000);
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

}