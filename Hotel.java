import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hotel {
    private String name;
    private String address;
    private String phoneNumber;
    private List<Administrator> administrators;
    private List<Room> rooms;
    private List<Resident> residents;

    public Hotel(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.administrators = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.residents = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Administrator> getAdministrators() {
        return administrators;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public void addAdministrator(Administrator administrator) {
        administrators.add(administrator);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public int countAdministrators() {
        return administrators.size();
    }

    public int countResidents() {
        return residents.size();
    }

    public int countOccupiedRoomTypes() {
        Set<String> occupiedRoomTypes = new HashSet<>();
        for (Resident resident : residents) {
            occupiedRoomTypes.add(resident.getRoom().getType());
        }
        return occupiedRoomTypes.size();
    }

    public List<String> getResidentsByRoomType(String roomType) {
        List<String> residentsByType = new ArrayList<>();
        for (Resident resident : residents) {
            if (resident.getRoom().getType().equals(roomType)) {
                residentsByType.add(resident.getName());
            }
        }
        return residentsByType;
    }

    public Resident getLongestStayResident() {
        if (residents.isEmpty()) {
            return null;
        }

        Resident longestStayResident = residents.get(0);
        for (Resident resident : residents) {
            if (resident.getStayDuration() > longestStayResident.getStayDuration()) {
                longestStayResident = resident;
            }
        }
        return longestStayResident;
    }

    public String getMostDemandedRoomType() {
        if (residents.isEmpty()) {
            return null;
        }

        Map<String, Integer> roomTypeCounts = new HashMap<>();
        for (Resident resident : residents) {
            String roomType = resident.getRoom().getType();
            roomTypeCounts.put(roomType, roomTypeCounts.getOrDefault(roomType, 0) + 1);
        }

        int maxCount = 0;
        String mostDemandedRoomType = null;
        for (Map.Entry<String, Integer> entry : roomTypeCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostDemandedRoomType = entry.getKey();
            }
        }

        return mostDemandedRoomType;
    }


    public Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
}
