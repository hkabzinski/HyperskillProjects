package server;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tells the server to stop serving, raised by the request that asks it to exit.
 */
public class ShutdownSignal {
    private final AtomicBoolean raised = new AtomicBoolean();

    public void raise() {
        raised.set(true);
    }

    public boolean isRaised() {
        return raised.get();
    }
}
