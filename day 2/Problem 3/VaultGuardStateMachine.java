enum DoorState {
    OPEN,
    CLOSED,
    LOCKED
}

class IllegalStateTransitionException extends RuntimeException {

    public IllegalStateTransitionException(String message) {
        super(message);
    }
}

class VaultDoor {

    private DoorState state;

    public VaultDoor() {
        state = DoorState.OPEN;
    }

    public void closeDoor() {

        if (state == DoorState.OPEN) {
            state = DoorState.CLOSED;
            System.out.println("Door Closed");
        }
    }

    public void lockDoor() {

        if (state == DoorState.OPEN) {
            throw new IllegalStateTransitionException(
                    "Cannot lock an OPEN door. Close it first."
            );
        }

        if (state == DoorState.CLOSED) {
            state = DoorState.LOCKED;
            System.out.println("Door Locked");
        }
    }

    public void unlockDoor() {

        if (state == DoorState.LOCKED) {
            state = DoorState.CLOSED;
            System.out.println("Door Unlocked");
        }
    }
}

public class VaultGuardStateMachine {

    public static void main(String[] args) {

        VaultDoor door = new VaultDoor();

        try {

            door.lockDoor();

        } catch (IllegalStateTransitionException e) {

            System.out.println(e.getMessage());
        }

        door.closeDoor();
        door.lockDoor();
        door.unlockDoor();
    }
}