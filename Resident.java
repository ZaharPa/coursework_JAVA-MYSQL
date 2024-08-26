public class Resident {
    private String name;
    private Room room;
    private int stayDuration;

    public Resident(String name, Room room, int stayDuration) {
        this.name = name;
        this.room = room;
        this.stayDuration = stayDuration;
    }

    public String getName() {
        return name;
    }

    public Room getRoom() {
        return room;
    }

    public int getStayDuration() {
        return stayDuration;
    }
}
