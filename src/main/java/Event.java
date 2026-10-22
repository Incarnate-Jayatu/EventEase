import java.time.LocalDate;

public class Event {
    private int id;
    private String name;
    private String category;
    private LocalDate date;
    private String venue;
    private int capacity;

    public Event(int id, String name, String category, LocalDate date, String venue, int capacity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.venue = venue;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getVenue() { return venue; }
    public int getCapacity() { return capacity; }

    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
